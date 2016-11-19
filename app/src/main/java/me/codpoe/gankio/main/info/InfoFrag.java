package me.codpoe.gankio.main.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.app.GankIoApplication;
import me.codpoe.gankio.main.MainActivity;
import me.codpoe.gankio.main.info.item.DeveloperItem;
import me.codpoe.gankio.main.info.item.DeveloperItemViewProvider;
import me.codpoe.gankio.main.info.item.HeadItem;
import me.codpoe.gankio.main.info.item.HeadItemViewProvider;
import me.codpoe.gankio.main.info.item.IntroduceItem;
import me.codpoe.gankio.main.info.item.IntroduceItemViewProvider;
import me.codpoe.gankio.main.info.item.OpenSourceItem;
import me.codpoe.gankio.main.info.item.OpenSourceItemViewProvider;
import me.codpoe.gankio.main.info.item.SettingItem;
import me.codpoe.gankio.main.info.item.SettingItemViewProvider;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Codpoe on 2016/10/8.
 */

public class InfoFrag extends Fragment implements InfoContract.View {

    @BindView(R.id.version_tv)
    TextView mVersionTv;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar_lay)
    CollapsingToolbarLayout mToolbarLay;
    @BindView(R.id.app_bar_lay)
    AppBarLayout mAppBarLay;
    @BindView(R.id.info_rv)
    RecyclerView mInfoRv;

    @Inject
    InfoPresenter mPresenter;

    private static final String DAY_NIGHT_MODE = "day_night_mode";
    private static final String NIGHT_MODE = "night_mode";

    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private boolean mIsFirstVisible = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_info, container, false);
        ButterKnife.bind(this, view);
        DaggerInfoComponent.builder().infoModule(new InfoModule(this)).build().inject(this);

        // set up toolbar

        // set up recycler view
        mInfoRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(HeadItem.class, new HeadItemViewProvider());
        mAdapter.register(
                IntroduceItem.class,
                new IntroduceItemViewProvider(new IntroduceItemViewProvider.OnClickListener() {
                    @Override
                    public void onShareBtnClick() {
                        mPresenter.share(getString(R.string.share_application_content));
                    }
                })
        );
        mAdapter.register(
                SettingItem.class,
                new SettingItemViewProvider(new SettingItemViewProvider.OnClickListener() {
                    @Override
                    public void onNightModeSwitch(boolean isChecked) {
                        SharedPreferences sp = getContext().getSharedPreferences(DAY_NIGHT_MODE, Context.MODE_PRIVATE);
                        sp.edit().putBoolean(NIGHT_MODE, isChecked).commit();
                        GankIoApplication.getInstance().changeDayNightMode(isChecked);
                        getActivity().recreate();
                    }
                })
        );
        mAdapter.register(DeveloperItem.class, new DeveloperItemViewProvider());
        mAdapter.register(OpenSourceItem.class, new OpenSourceItemViewProvider());
        mInfoRv.setAdapter(mAdapter);
        mInfoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy != 0) {
                    ((MainActivity) getActivity()).showOrHideBottomNavigation(dy < 0);
                }
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mIsFirstVisible) {
            mIsFirstVisible = false;
            mItems.add(new HeadItem(getString(R.string.introduce)));
            mItems.add(new IntroduceItem(getString(R.string.introduce_content)));
            mItems.add(new HeadItem(getString(R.string.setting)));
            mItems.add(new SettingItem(getContext().getSharedPreferences(DAY_NIGHT_MODE, Context.MODE_PRIVATE).getBoolean(NIGHT_MODE, false)));
            mItems.add(new HeadItem(getString(R.string.developer)));
            mItems.add(new DeveloperItem(
                    R.drawable.avatar,
                    getString(R.string.codpoe),
                    getString(R.string.codpoe_weibo_url),
                    getString(R.string.codpoe_github_url),
                    getString(R.string.codpoe_mail)
            ));
            mItems.add(new HeadItem(getString(R.string.open_source_license)));
            mItems.add(new OpenSourceItem(getString(R.string.butterknife)));
            mItems.add(new OpenSourceItem(getString(R.string.rxjava)));
            mItems.add(new OpenSourceItem(getString(R.string.rxandroid)));
            mItems.add(new OpenSourceItem(getString(R.string.rxpermissions)));
            mItems.add(new OpenSourceItem(getString(R.string.retrofit)));
            mItems.add(new OpenSourceItem(getString(R.string.glide)));
            mItems.add(new OpenSourceItem(getString(R.string.dagger2)));
            mItems.add(new OpenSourceItem(getString(R.string.multitype)));
            mItems.add(new OpenSourceItem(getString(R.string.photoview)));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showMsg(String msg) {
        ((MainActivity) getActivity()).showMsg(msg);
    }

}
