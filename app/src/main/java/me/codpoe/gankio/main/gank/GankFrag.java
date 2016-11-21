package me.codpoe.gankio.main.gank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.main.MainActivity;
import me.codpoe.gankio.main.gank.adapter.GankPagerAdapter;
import me.codpoe.gankio.main.gank.fragment.AndroidFrag;
import me.codpoe.gankio.main.gank.fragment.IOSFrag;
import me.codpoe.gankio.main.gank.fragment.WebFrag;
import me.codpoe.gankio.main.gank.home.HomeFrag;
import me.codpoe.gankio.main.gank.meizhi.MeizhiFrag;
import me.codpoe.gankio.util.DialogUtil;
import me.codpoe.gankio.util.DpPxUtil;
import me.codpoe.gankio.widget.SearchBarHelper;

/**
 * Created by Codpoe on 2016/10/8.
 */

public class GankFrag extends Fragment {

    @BindView(R.id.search_bar)
    ViewGroup mSearchBar;
    @BindView(R.id.gank_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.gank_tab_lay)
    TabLayout mTabLay;
    @BindView(R.id.gank_pager)
    ViewPager mGankPager;
    @BindView(R.id.gank_app_bar_lay)
    AppBarLayout mAppBarLay;

    private List<Fragment> mFragments;
    private String[] mTitles;
    private HomeFrag mHomeFrag;
    private IOSFrag mIOSFrag;
    private AndroidFrag mAndroidFrag;
    private WebFrag mWebFrag;
    private MeizhiFrag mMeizhiFrag;
    private GankPagerAdapter mGankPagerAdapter;
    private int mAppBarHeight;
    private SearchBarHelper mSearchBarHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_gank, container, false);
        ButterKnife.bind(this, view);

        // set up app bar
        mAppBarHeight = DpPxUtil.dp2px(getActivity(), 56);
        mAppBarLay.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ((MainActivity)getActivity()).showOrHideBottomNavigation(verticalOffset != -mAppBarHeight);
            }
        });

        // set up toolbar
        mToolbar.inflateMenu(R.menu.gank_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search_menu_item:
                        mSearchBarHelper.show();
                        break;
                    case R.id.gank_help_menu_item:
                        DialogUtil.showNormalDialog(getActivity(), getString(R.string.gank_help_msg));
                        break;
                }
                return true;
            }
        });

        // set up search bar
        mSearchBarHelper = new SearchBarHelper(getContext(), mSearchBar);

        // set up fragments
        FragmentManager fm = getChildFragmentManager();
        mFragments = fm.getFragments();
        if (mFragments == null) {
            mHomeFrag = new HomeFrag();
            mIOSFrag = new IOSFrag();
            mAndroidFrag = new AndroidFrag();
            mWebFrag = new WebFrag();
            mMeizhiFrag = new MeizhiFrag();
            mFragments = new ArrayList<>();
            mFragments.add(mHomeFrag);
            mFragments.add(mIOSFrag);
            mFragments.add(mAndroidFrag);
            mFragments.add(mWebFrag);
            mFragments.add(mMeizhiFrag);
        } else {
            mHomeFrag = (HomeFrag) mFragments.get(0);
            mIOSFrag = (IOSFrag) mFragments.get(1);
            mAndroidFrag = (AndroidFrag) mFragments.get(2);
            mWebFrag = (WebFrag) mFragments.get(3);
            mMeizhiFrag = (MeizhiFrag) mFragments.get(4);
        }
        mTitles = new String[]{
                getString(R.string.home),
                getString(R.string.ios_name),
                getString(R.string.android_name),
                getString(R.string.web_name),
                getString(R.string.fuli)
        };

        // set up ViewPager
        mGankPagerAdapter = new GankPagerAdapter(getChildFragmentManager(), mFragments, mTitles);
        mGankPager.setAdapter(mGankPagerAdapter);
        mGankPager.setOffscreenPageLimit(4);
        mTabLay.setupWithViewPager(mGankPager);

        return view;

    }

    public void scrollToTop() {
        switch (mGankPager.getCurrentItem()) {
            case 0:
                mHomeFrag.scrollToTop();
                break;
            case 1:
                mIOSFrag.scrollToTop();
                break;
            case 2:
                mAndroidFrag.scrollToTop();
                break;
            case 3:
                mWebFrag.scrollToTop();
                break;
            case 4:
                mMeizhiFrag.scrollToTop();
                break;
        }
    }

    public void scrollToPosition(int position) {
        mGankPager.setCurrentItem(position);
    }

    /**
     * is used in onBackPressed()
     * @return
     */
    public boolean isSearchBarShow() {
        return mSearchBarHelper.isSearchBarShow();
    }

    /**
     * is used in onBackPressed()
     */
    public void hideSearchBar() {
        mSearchBarHelper.hide();
    }

}
