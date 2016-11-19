package me.codpoe.gankio.main.gank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.main.MainActivity;
import me.codpoe.gankio.main.gank.DaggerGankComponent;
import me.codpoe.gankio.main.gank.GankContract;
import me.codpoe.gankio.main.gank.GankModule;
import me.codpoe.gankio.main.gank.GankPresenter;
import me.codpoe.gankio.main.gank.adapter.CommonRvAdapter;
import me.codpoe.gankio.util.DialogUtil;
import me.codpoe.gankio.web.WebActivity;
import me.codpoe.gankio.widget.AnimationAdapter;

/**
 * Created by Codpoe on 2016/10/9.
 */

public class AndroidFrag extends Fragment implements
        GankContract.View,
        CommonRvAdapter.OnCommonItemClickListener {

    @BindView(R.id.android_rv)
    RecyclerView mAndroidRv;
    @BindView(R.id.refresh_lay)
    SwipeRefreshLayout mRefreshLay;

    @Inject
    GankPresenter mPresenter;

    private static final String ANDROID = "Android";

    private List<AllBean.ResultsBean> mAndroidList;
    private LinearLayoutManager mLayoutManager;
    private CommonRvAdapter mAndroidRvAdapter;
    private AnimationAdapter mRvAdapter;
    private int mPage;
    private boolean mIsFirstVisible = true;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_android, container, false);
        ButterKnife.bind(this, view);
        DaggerGankComponent.builder().gankModule(new GankModule(this)).build().inject(this);

        // set up presenter
        mPresenter = new GankPresenter(this);

        // set up SwipeRefreshLayout
        mRefreshLay.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mPresenter.loadData(ANDROID, mPage);
            }
        });

        // set up RecyclerView
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAndroidList = new ArrayList<>();
        mAndroidRvAdapter = new CommonRvAdapter(getActivity(), mAndroidList);
        mRvAdapter = new AnimationAdapter(mAndroidRvAdapter);
        mAndroidRv.setLayoutManager(mLayoutManager);
        mAndroidRv.setAdapter(mRvAdapter);
        mAndroidRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mAndroidRvAdapter.setOnItemClickListener(this);
        mAndroidRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (lastPosition >= mLayoutManager.getItemCount() - 2 && dy > 0) {
                    mRefreshLay.setRefreshing(true);
                    mPresenter.loadData(ANDROID, ++mPage);
                }
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && isVisibleToUser && mIsFirstVisible) {
            mIsFirstVisible = false;
            mRefreshLay.setRefreshing(true);
            mPage = 1;
            mPresenter.loadData(ANDROID, mPage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter = null;
    }

    @Override
    public void showData(List<AllBean.ResultsBean> iOSList) {
        if (mPage == 1) {
            mAndroidList.clear();
            mRvAdapter.notifyDataSetChanged();
        }
        for (AllBean.ResultsBean resultsBean : iOSList) {
            mAndroidList.add(resultsBean);
            mRvAdapter.notifyItemInserted(mAndroidList.size() - 1);
        }
        if (mRefreshLay.isRefreshing()) {
            mRefreshLay.setRefreshing(false);
        }
    }

    @Override
    public void showMsg(String msg) {
        ((MainActivity)getActivity()).showMsg(msg);
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void scrollToTop() {
        mAndroidRv.smoothScrollToPosition(0);
    }

    @Override
    public void onCommonItemClick(View view, int position) {
        Intent intent = WebActivity.newIntent(
                getActivity(),
                mAndroidList.get(position).getUrl(),
                mAndroidList.get(position).getDesc());
        startActivity(intent);
    }

    @Override
    public void onCommonItemLongClick(View view, final int position) {
        final boolean isExist = mPresenter.isExist(mAndroidList.get(position).getId());
        DialogUtil.showGankBottomDialog(getContext(), isExist, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.mark_tv:
                        if (!isExist) {
                            mPresenter.saveData(mAndroidList.get(position));
                            showMsg(getResources().getString(R.string.mark_success));
                        } else {
                            mPresenter.deleteDataById(mAndroidList.get(position).getId());
                            showMsg(getResources().getString(R.string.mark_cancel_already));
                        }
                        break;
                    case R.id.share_tv:
                        mPresenter.share(getContext(), mAndroidList.get(position).getUrl(), mAndroidList.get(position).getDesc());
                        break;
                }
                DialogUtil.hideBottomDialog();
            }
        });
    }

}
