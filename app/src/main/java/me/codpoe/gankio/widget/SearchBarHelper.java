package me.codpoe.gankio.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.codpoe.gankio.R;
import me.codpoe.gankio.search.SearchActivity;
import me.codpoe.gankio.util.DpPxUtil;
import me.codpoe.gankio.util.SoftInputUtil;

/**
 * Created by Codpoe on 2016/10/23.
 */

public class SearchBarHelper {

    @BindView(R.id.search_close_btn)
    ImageButton mCloseBtn;
    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_btn)
    ImageButton mSearchBtn;

    private Context mContext;
    private ViewGroup mSearchBar;
    private View.OnClickListener mOnClickListener;
    private Animator mShowAnim;
    private Animator mHideAnim;

    public SearchBarHelper(Context context, ViewGroup searchBar) {
        ButterKnife.bind(this, searchBar);
        mContext = context;
        mSearchBar = searchBar;

        mSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    search(mSearchEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(mSearchEdit.getText().toString());
            }
        });

    }

    @OnClick({R.id.search_close_btn, R.id.search_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_close_btn:
                hide();
                break;
            case R.id.search_btn:
                search(mSearchEdit.getText().toString());
                break;
        }
    }

    /**
     * search
     * when on search button pressed
     * or on keycode_enter pressed
     * @param toSearch
     */
    private void search(String toSearch) {
        if (toSearch != null && !toSearch.equals("")) {
            hide();
            Intent intent = SearchActivity.newIntent(mSearchBar.getContext(), toSearch);
            mSearchBar.getContext().startActivity(intent);
        }
    }

    public void show() {
        mShowAnim = makeSearchBarAnim(false);
        mShowAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSearchBar.setVisibility(View.VISIBLE);
                mSearchEdit.requestFocus();
                SoftInputUtil.from(mSearchEdit).show();
            }
        });
        mShowAnim.start();
    }

    public void hide() {
        mHideAnim = makeSearchBarAnim(true);
        mHideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSearchBar.setVisibility(View.INVISIBLE);
            }
        });
        mHideAnim.start();
        mSearchBar.clearFocus();
        SoftInputUtil.from(mSearchEdit).hide();
    }

    private Animator makeSearchBarAnim(boolean reverse) {
        int width = mSearchBar.getWidth();
        int centerX = mSearchBar.getRight()
                + mSearchBar.getPaddingRight()
                - DpPxUtil.dp2px(mSearchBar.getContext().getApplicationContext(), 68);
        int centerY = (mSearchBar.getTop() + mSearchBar.getBottom()) / 2;

        Animator animator = ViewAnimationUtils.createCircularReveal(
                mSearchBar,
                centerX, centerY,
                reverse ? width : 0,
                reverse ? 0 : width
        );
        animator.setDuration(mSearchBar.getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime));
        return animator;
    }

    public boolean isSearchBarShow() {
        return mSearchBar.getVisibility() == View.VISIBLE;
    }

}
