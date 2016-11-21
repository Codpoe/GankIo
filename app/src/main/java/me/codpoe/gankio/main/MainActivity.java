package me.codpoe.gankio.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.main.gank.GankFrag;
import me.codpoe.gankio.main.info.InfoFrag;
import me.codpoe.gankio.main.mark.MarkFrag;
import me.codpoe.gankio.widget.ScrollBottomNavigationView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static me.codpoe.gankio.R.id.main_frame_lay;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_nav)
    ScrollBottomNavigationView mBottomNav;
    @BindView(R.id.main_coor_lay)
    CoordinatorLayout mMainCoorLay;

    private Fragment[] mFragments;
    private GankFrag mGankFrag;
    private MarkFrag mMarkFrag;
    private InfoFrag mInfoFrag;
    private int mCurPos = 0;
    private BottomSheetBehavior behavior;
    private boolean mIsFirst = true;
    private boolean mCanExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if it is the first time to create the MainActivity,
        // sleep 1000 ms to let the users stay in the first screen for 1000 ms.
        if (savedInstanceState == null) {
            SystemClock.sleep(1000);
        }
        // when the first screen is gone, switch the theme before setContentView(...).
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // set up fragments
        FragmentManager fm = getSupportFragmentManager();
        mGankFrag = (GankFrag) fm.findFragmentByTag("gank");
        mMarkFrag = (MarkFrag) fm.findFragmentByTag("mark");
        mInfoFrag = (InfoFrag) fm.findFragmentByTag("info");
        if (mGankFrag == null) {
            mGankFrag = new GankFrag();
            fm.beginTransaction()
                    .add(main_frame_lay, mGankFrag, "gank")
                    .commit();
        }
        if (mMarkFrag == null) {
            mMarkFrag = new MarkFrag();
            fm.beginTransaction()
                    .add(main_frame_lay, mMarkFrag, "mark")
                    .commit();
        }
        if (mInfoFrag == null) {
            mInfoFrag = new InfoFrag();
            fm.beginTransaction()
                    .add(main_frame_lay, mInfoFrag, "info")
                    .commit();
        }
        mFragments = new Fragment[] {mGankFrag, mMarkFrag, mInfoFrag};
        fm.beginTransaction()
                .hide(mMarkFrag)
                .hide(mInfoFrag)
                .show(mGankFrag)
                .commit();

        // set up BottomNavigationView
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = item.getOrder();
                if (mCurPos != position) {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_NONE)
                            .hide(mFragments[mCurPos])
                            .show(mFragments[position])
                            .commit();
                    mCurPos = position;
                } else {
                    if (mCurPos == 0) {
                        mGankFrag.scrollToTop();
                    } else if (mCurPos == 1) {
                        mMarkFrag.scrollToTop();
                    }
                }
                return true;
            }
        });
        mBottomNav.setSaveEnabled(true);

    }

    /**
     * double click to close
     */
    @Override
    public void onBackPressed() {
        if (mCurPos == 0 && mGankFrag.isSearchBarShow()) {
            mGankFrag.hideSearchBar();
        } else if (mCanExit) {
            super.onBackPressed();
        } else {
            mCanExit = true;
            showMsg(getString(R.string.press_once_more));
            Observable.timer(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            mCanExit = false;
                        }
                    });
        }
    }

    public void showMsg(String msg) {
        Snackbar snackbar = Snackbar.make(mMainCoorLay, msg, Snackbar.LENGTH_SHORT);
        if (!mBottomNav.isHidden()) {
            ViewGroup.LayoutParams vl = snackbar.getView().getLayoutParams();
            CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(vl.width, vl.height);
            cl.gravity = Gravity.BOTTOM;
            cl.bottomMargin = 168;
            snackbar.getView().setLayoutParams(cl);
        }
        snackbar.getView().setVisibility(View.VISIBLE);
        snackbar.show();
    }

    /**
     * true to show, false to hide
     * @param show
     */
    public void showOrHideBottomNavigation(boolean show) {
        if (!show && !mBottomNav.isHidden()) {
            mBottomNav.hide();
        } else if (show && mBottomNav.isHidden()){
            mBottomNav.show();
        }

    }

}
