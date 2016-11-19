package me.codpoe.gankio.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import me.codpoe.gankio.R;

/**
 * Created by Codpoe on 2016/10/23.
 */

public class DialogUtil {

    private static BottomSheetDialog mBottomSheetDialog;

    public static void showNormalDialog(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setMessage(msg)
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    public static void showGankBottomDialog(Context context, boolean isExist, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.sheet_gank, null);
        TextView markTv = (TextView) view.findViewById(R.id.mark_tv);
        TextView shareTv = (TextView) view.findViewById(R.id.share_tv);
        if (isExist) {
            markTv.setText(R.string.cancel_mark);
        }
        markTv.setOnClickListener(onClickListener);
        shareTv.setOnClickListener(onClickListener);
        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
    }

    public static void showDeveloperBottomDialog(Context context, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_about_codpoe, null);
        TextView weiboTv = (TextView) view.findViewById(R.id.weibo_tv);
        TextView githubTv = (TextView) view.findViewById(R.id.github_tv);
        TextView mailTv = (TextView) view.findViewById(R.id.mail_tv);
        weiboTv.setOnClickListener(onClickListener);
        githubTv.setOnClickListener(onClickListener);
        mailTv.setOnClickListener(onClickListener);
        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
    }

    public static void hideBottomDialog() {
        if (mBottomSheetDialog != null) {
            mBottomSheetDialog.hide();
            mBottomSheetDialog = null;
        }
    }

}
