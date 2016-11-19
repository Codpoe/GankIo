package me.codpoe.gankio.main.gank.meizhi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import me.codpoe.gankio.main.gank.meizhi.adapter.MeizhiRvAdapter;
import me.codpoe.gankio.picture.PictureActivity;

/**
 * Created by Codpoe on 2016/10/24.
 */

public class MeizhiFrag extends Fragment implements MeizhiContract.View {

    @BindView(R.id.meizhi_rv)
    RecyclerView mMeizhiRv;
    @BindView(R.id.meizhi_refresh_lay)
    SwipeRefreshLayout mRefreshLay;

    @Inject
    MeizhiPresenter mPresenter;

    private List<AllBean.ResultsBean> mMeizhiList;
    private MeizhiRvAdapter mMeizhiRvAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private boolean mIsFirstVisible = true;
    private int mPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_meizhi, container, false);
        ButterKnife.bind(this, view);
        DaggerMeizhiComponent.builder().meizhiModule(new MeizhiModule(this)).build().inject(this);

        // set up presenter
        mPresenter = new MeizhiPresenter(this);

        // set up refresh layout
        mRefreshLay.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mPresenter.loadMeizhi(mPage);
            }
        });

        // set up recycler view
        mMeizhiList = new ArrayList<>();
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mMeizhiRvAdapter = new MeizhiRvAdapter(getActivity(), mMeizhiList);
        mMeizhiRv.setLayoutManager(mLayoutManager);
        mMeizhiRv.setAdapter(mMeizhiRvAdapter);
        mMeizhiRvAdapter.setOnItemClickListener(new MeizhiRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = PictureActivity.newIntent(
                        getContext(),
                        mMeizhiList.get(position).getUrl(),
                        mMeizhiList.get(position).getId()
                );
                startActivity(intent);
            }
        });
        mMeizhiRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastPos = mLayoutManager.findLastVisibleItemPositions(new int[mLayoutManager.getSpanCount()]);
                if (Math.max(lastPos[0], lastPos[1]) > mLayoutManager.getItemCount() - 2) {
                    mRefreshLay.setRefreshing(true);
                    mPresenter.loadMeizhi(++mPage);
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
            mPresenter.loadMeizhi(mPage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter = null;
    }

    @Override
    public void showMeizhi(List<AllBean.ResultsBean> meizhiList) {
        if (mPage == 1) {
            mMeizhiList.clear();
            mMeizhiRvAdapter.notifyDataSetChanged();
        }
        if (mMeizhiList.size() == 0) {
            mMeizhiList.addAll(meizhiList);
            mMeizhiRvAdapter.notifyDataSetChanged();
        } else {
            for (AllBean.ResultsBean resultsBean : meizhiList) {
                mMeizhiList.add(resultsBean);
                if (mMeizhiList.size() == 1) {
                    mMeizhiRvAdapter.notifyDataSetChanged();
                } else {
                    mMeizhiRvAdapter.notifyItemInserted(mMeizhiList.size() - 1);
                }
            }
        }
        mRefreshLay.setRefreshing(false);
    }

    @Override
    public void showMsg(String msg) {
        ((MainActivity) getActivity()).showMsg(msg);
    }

    public void scrollToTop() {
        mMeizhiRv.smoothScrollToPosition(0);
    }
}
