package me.codpoe.gankio.picture;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import me.codpoe.gankio.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Codpoe on 2016/10/27.
 */

public class PicturePresenter implements PictureContract.Presenter {

    private PictureContract.View mView;
    private Context mContext;

    @Inject
    public PicturePresenter(PictureContract.View view) {
        mView = view;
        mContext = (Context) mView;
    }

    @Override
    public void copyUrl(String url) {
        ClipboardManager clipManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("url", url);
        clipManager.setPrimaryClip(clipData);
        mView.showMsg(mContext.getString(R.string.copy_success));
    }

    @Override
    public Observable<Uri> savePic(final String url, final String id) {

        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(final Subscriber<? super Bitmap> subscriber) {
                Glide.with(mContext)
                        .load(url)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                subscriber.onNext(resource);
                            }
                        });

            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Bitmap, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(Bitmap bitmap) {
                        File dir = new File(Environment.getExternalStorageDirectory(), "Meizhi");
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        Log.d("save", "dir: " + dir.toString());
                        String fileName = id + ".png";
                        File f = new File(dir, fileName);
                        try {
                            FileOutputStream fos = new FileOutputStream(f);
                            assert bitmap != null;
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Uri uri = Uri.fromFile(f);
                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                        ((Context) mView).sendBroadcast(intent);
                        Log.d("save", uri.toString());
                        return Observable.just(uri);
                    }
                });

    }

    @Override
    public void sharePic(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_pic)));
    }

}
