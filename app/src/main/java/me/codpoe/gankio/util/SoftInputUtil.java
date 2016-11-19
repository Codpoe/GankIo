package me.codpoe.gankio.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Codpoe on 2016/10/23.
 */

public class SoftInputUtil {

    private View mView;
    private InputMethodManager mInputManager;

    private SoftInputUtil(View view) {
        mView = view;
        mInputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static SoftInputUtil from(View view) {
        return new SoftInputUtil(view);
    }

    public void show() {
        mInputManager.showSoftInput(mView, 0);
    }

    public void hide() {
        mInputManager.hideSoftInputFromWindow(mView.getWindowToken(), 0);
    }

}
