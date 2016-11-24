package me.codpoe.gankio.picture;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.codpoe.gankio.R;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureActivity extends AppCompatActivity implements PictureContract.View {


    @BindView(R.id.meizhi_img)
    ImageView mMeizhiImg;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.copy)
    ImageButton mCopyBtn;
    @BindView(R.id.save)
    ImageButton mSaveBtn;
    @BindView(R.id.share)
    ImageButton mShareBtn;
    @BindView(R.id.bottom_bar)
    LinearLayout mBottomBar;

    @Inject
    PicturePresenter mPresenter;

    private static final String EXTRA_IMG_URL = "img_url";
    private static final String EXTRA_IMG_ID = "img_id";

    private PhotoViewAttacher mAttacher;
    private String mImgUrl;
    private String mImgId;
    private Uri mImgUri;
    private boolean mIsHide = false;

    public static Intent newIntent(Context context, String url, String id) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_IMG_URL, url);
        intent.putExtra(EXTRA_IMG_ID, id);
        return intent;
    }

    private void parseIntent() {
        mImgUrl = getIntent().getStringExtra(EXTRA_IMG_URL);
        mImgId = getIntent().getStringExtra(EXTRA_IMG_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ButterKnife.bind(this);
        DaggerPictureComponent.builder().pictureModule(new PictureModule(this)).build().inject(this);

        RxPermissions.getInstance(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                    }
                });

        parseIntent();

        // set up progress bar
        mProgressBar.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.show();
            }
        });

        // set up image view
        Glide.with(this)
                .load(mImgUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mProgressBar.hide();
                        mMeizhiImg.setImageBitmap(resource);
                        mAttacher = new PhotoViewAttacher(mMeizhiImg, true);
                        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                            @Override
                            public void onViewTap(View view, float x, float y) {
                                if (!mIsHide) {
                                    hide();
                                } else {
                                    show();
                                }
                            }
                        });
                    }
                });

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * tap to hide app bar, bottom bar and system ui(status bar and navigation bar)
     */
    public void hide() {
        mIsHide = true;
        Animator bottomBarAnim = AnimatorInflater.loadAnimator(this, R.animator.picture_bottombar_bottom_out);
        bottomBarAnim.setTarget(mBottomBar);
        bottomBarAnim.start();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    /**
     * tap to show app bar, bottom bar and system ui(status bar and navigation bar)
     */
    public void show() {
        mIsHide = false;
        Animator bottomBarAnim = AnimatorInflater.loadAnimator(this, R.animator.picture_bottombar_bottom_in);
        bottomBarAnim.setTarget(mBottomBar);
        bottomBarAnim.start();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @OnClick({R.id.copy, R.id.save, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copy:
                mPresenter.copyUrl(mImgUrl);
                break;
            case R.id.save:
                mPresenter.savePic(mImgUrl, mImgId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Uri>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                showMsg(e.getMessage());
                            }

                            @Override
                            public void onNext(Uri uri) {
                                mImgUri = uri;
                                showMsg(getString(R.string.save_success_to) + new File(uri.getPath()).getAbsolutePath());
                            }
                        });
                break;
            case R.id.share:
                if (mImgUri == null) {
                    mPresenter.savePic(mImgUrl, mImgId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Uri>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    showMsg(e.getMessage());
                                }

                                @Override
                                public void onNext(Uri uri) {
                                    mImgUri = uri;
                                    mPresenter.sharePic(mImgUri);
                                }
                            });
                } else {
                    mPresenter.sharePic(mImgUri);
                }
                break;
        }
    }

}
