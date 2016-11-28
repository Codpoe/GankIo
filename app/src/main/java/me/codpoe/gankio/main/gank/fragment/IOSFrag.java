package me.codpoe.gankio.main.gank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class IOSFrag extends Fragment implements
        GankContract.View,
        CommonRvAdapter.OnCommonItemClickListener {

    @BindView(R.id.ios_rv)
    RecyclerView mIOSRv;
    @BindView(R.id.refresh_lay)
    SwipeRefreshLayout mRefreshLay;

    @Inject
    GankPresenter mPresenter;

    private static final String IOS = "iOS";

    private List<AllBean.ResultsBean> mIOSList;
    private LinearLayoutManager mLayoutManager;
    private CommonRvAdapter mIOSRvAdapter;
    private AnimationAdapter mRvAdapter;
    private int mPage;
    private boolean mIsFirstVisible = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_ios, container, false);
        ButterKnife.bind(this, view);
        DaggerGankComponent.builder().gankModule(new GankModule(this)).build().inject(this);

        // set up SwipeRefreshLayout
        mRefreshLay.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mPresenter.loadData(IOS, mPage);
            }
        });

        // set up RecyclerView
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mIOSList = new ArrayList<>();
        mIOSRvAdapter = new CommonRvAdapter(getActivity(), mIOSList);
        mRvAdapter = new AnimationAdapter(mIOSRvAdapter);
        mIOSRv.setLayoutManager(mLayoutManager);
        mIOSRv.setAdapter(mRvAdapter);
        mIOSRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mIOSRvAdapter.setOnItemClickListener(this);
        mIOSRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (lastPosition == mLayoutManager.getItemCount() - 1 && dy > 0) {
                    mPresenter.loadData(IOS, ++mPage);
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
            mPresenter.loadData(IOS, mPage);
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
            mIOSList.clear();
            mRvAdapter.notifyDataSetChanged();
        }
        for (AllBean.ResultsBean resultsBean : iOSList) {
            mIOSList.add(resultsBean);
            mRvAdapter.notifyItemInserted(mIOSList.size() - 1);
        }
        if (mRefreshLay.isRefreshing()) {
            mRefreshLay.setRefreshing(false);
        }
    }

    @Override
    public void showMsg(String msg) {
        ((MainActivity)getActivity()).showMsg(msg);
    }

    public void scrollToTop() {
        mIOSRv.smoothScrollToPosition(0);
    }

    @Override
    public void onCommonItemClick(View view, int position) {
        Intent intent = WebActivity.newIntent(
                getActivity(),
                mIOSList.get(position).getUrl(),
                mIOSList.get(position).getDesc());
        startActivity(intent);
    }

    @Override
    public void onCommonItemLongClick(View view, final int position) {
        final boolean isExist = mPresenter.isExist(mIOSList.get(position).getId());
        DialogUtil.showGankBottomDialog(getContext(), isExist, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.mark_tv:
                        if (!isExist) {
                            mPresenter.saveData(mIOSList.get(position));
                            showMsg(getResources().getString(R.string.mark_success));
                        } else {
                            mPresenter.deleteDataById(mIOSList.get(position).getId());
                            showMsg(getResources().getString(R.string.mark_cancel_already));
                        }
                        break;
                    case R.id.share_tv:
                        mPresenter.share(getContext(), mIOSList.get(position).getUrl(), mIOSList.get(position).getDesc());
                        break;
                }
                DialogUtil.hideBottomDialog();
            }
        });
    }
}
