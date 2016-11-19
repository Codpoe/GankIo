package me.codpoe.gankio.main.gank.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;
import me.codpoe.gankio.data.bean.AllBean;
import me.codpoe.gankio.main.MainActivity;
import me.codpoe.gankio.main.gank.GankFrag;
import me.codpoe.gankio.main.gank.adapter.CommonRvAdapter;
import me.codpoe.gankio.main.gank.item.GankHeadItem;
import me.codpoe.gankio.main.gank.item.GankHeadItemViewProvider;
import me.codpoe.gankio.main.gank.item.GankImgSubItem;
import me.codpoe.gankio.main.gank.item.GankImgSubItemViewProvider;
import me.codpoe.gankio.main.gank.item.GankSubItem;
import me.codpoe.gankio.main.gank.item.GankSubItemViewProvider;
import me.codpoe.gankio.util.DialogUtil;
import me.codpoe.gankio.web.WebActivity;
import me.drakeet.multitype.Item;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Codpoe on 2016/11/13.
 */

public class HomeFrag extends Fragment implements
        HomeContract.View,
        GankHeadItemViewProvider.OnGankHeadClickListener,
        GankSubItemViewProvider.OnGankItemClickListener,
        CommonRvAdapter.OnCommonItemClickListener {

    @BindView(R.id.home_rv)
    RecyclerView mHomeRv;
    @BindView(R.id.refresh_lay)
    SwipeRefreshLayout mRefreshLay;

    @Inject
    HomePresenter mPresenter;

    private MultiTypeAdapter mAdapter;
    private Items mItems;

    private BottomSheetBehavior behavior;
    private TextView mSheetTypeTv;
    private ContentLoadingProgressBar mProgressBar;
    private RecyclerView mSheetRv;
    private LinearLayoutManager mSheetLayoutManager;
    private List<AllBean.ResultsBean> mSheetList;
    private CommonRvAdapter mSheetRvAdapter;
    private int mSheetPage;
    private String mSheetType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        DaggerHomeComponent.builder().homeModule(new HomeModule(this)).build().inject(this);

        // set up refresh layout
        mRefreshLay.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mItems.clear();
                mPresenter.loadData(mPresenter.loadDate());
            }
        });

        // set up recycler view
        mItems = new Items();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(GankHeadItem.class, new GankHeadItemViewProvider(this));
        mAdapter.register(GankSubItem.class, new GankSubItemViewProvider(this));
        mAdapter.register(GankImgSubItem.class, new GankImgSubItemViewProvider(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mHomeRv.setLayoutManager(layoutManager);
        mHomeRv.setAdapter(mAdapter);

        // set up sheet recyclerview
        mSheetTypeTv = (TextView) getActivity().findViewById(R.id.type_tv);
        mProgressBar = (ContentLoadingProgressBar) getActivity().findViewById(R.id.progress_bar);
        mSheetRv = (RecyclerView) getActivity().findViewById(R.id.rv);
        mSheetList = new ArrayList<>();
        mSheetLayoutManager = new LinearLayoutManager(getContext());
        mSheetRvAdapter = new CommonRvAdapter(getContext(), mSheetList);
        mSheetRv.setLayoutManager(mSheetLayoutManager);
        mSheetRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        mSheetRv.setAdapter(mSheetRvAdapter);
        mSheetRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = mSheetLayoutManager.findLastVisibleItemPosition();
                if (lastPosition >= mSheetLayoutManager.getItemCount() - 2 && dy > 0) {
                    mPresenter.loadDataByType(mSheetType, ++mSheetPage);
                }
            }
        });
        mSheetRvAdapter.setOnItemClickListener(this);

        // set up bottom sheet behavior
        View v = getActivity().findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(v);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (mSheetTypeTv.getVisibility() == View.INVISIBLE) {
                    Animator animator = ViewAnimationUtils.createCircularReveal(
                            mSheetTypeTv,
                            (mSheetTypeTv.getLeft() + mSheetTypeTv.getRight()) / 2,
                            + mSheetTypeTv.getBottom(),
                            0,
                            mSheetTypeTv.getWidth()
                    ).setDuration(500);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mSheetTypeTv.setText(mSheetType);
                            mSheetTypeTv.setVisibility(View.VISIBLE);
                            mProgressBar.show();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mSheetPage = 1;
                            mPresenter.loadDataByType(mSheetType, mSheetPage);
                        }
                    });
                    animator.start();
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mSheetTypeTv.setVisibility(View.INVISIBLE);
                    mSheetList.clear();
                    mSheetRvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        // load data
        view.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLay.setRefreshing(true);
                mItems.clear();
                mPresenter.loadData(mPresenter.loadDate());
            }
        });

        return view;

    }

    @Override
    public void showData(List<AllBean.ResultsBean> list) {
        if (mSheetPage == 1) {
            mSheetList.clear();
        }
        mProgressBar.hide();
        mSheetList.addAll(list);
        mSheetRvAdapter.notifyDataSetChanged();
    }

    @Override
    public void showItem(Item item) {
        mItems.add(item);
    }

    @Override
    public void showAllItem(List<Item> itemList) {
        mItems.addAll(itemList);
    }

    @Override
    public void showMsg(String msg) {
        ((MainActivity) getActivity()).showMsg(msg);
    }

    @Override
    public void complete() {
        mAdapter.notifyDataSetChanged();
        mRefreshLay.setRefreshing(false);
    }

    public void scrollToTop() {
        mHomeRv.smoothScrollToPosition(0);
    }

    @Override
    public void onGankHeadClick(String type) {
        switch (type) {
            case "iOS":
                ((GankFrag) getParentFragment()).scrollToPosition(1);
                break;
            case "Android":
                ((GankFrag) getParentFragment()).scrollToPosition(2);
                break;
            case "前端":
                ((GankFrag) getParentFragment()).scrollToPosition(3);
                break;
            case "拓展资源":
                mSheetType = getString(R.string.tuozhan);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case "App":
                mSheetType = getString(R.string.app);
                mSheetPage = 1;
                mPresenter.loadDataByType(mSheetType, mSheetPage);
                mSheetTypeTv.setText(mSheetType);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case "瞎推荐":
                mSheetType = getString(R.string.xiatuijian);
                mSheetPage = 1;
                mPresenter.loadDataByType(mSheetType, mSheetPage);
                mSheetTypeTv.setText(mSheetType);
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    @Override
    public void onGankItemLongClick(final GankSubItem item) {
        final boolean isExist = mPresenter.isExist(item.mId);
        DialogUtil.showGankBottomDialog(getContext(), isExist, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.mark_tv:
                        if (!isExist) {
                            mPresenter.saveData(item);
                            showMsg(getResources().getString(R.string.mark_success));
                        } else {
                            mPresenter.deleteDataById(item.mId);
                            showMsg(getResources().getString(R.string.mark_cancel_already));
                        }
                        break;
                    case R.id.share_tv:
                        mPresenter.share(getContext(), item.mUrl, item.mTitle);
                        break;
                }
                DialogUtil.hideBottomDialog();
            }
        });
    }

    @Override
    public void onCommonItemClick(View view, int position) {
        Intent intent = WebActivity.newIntent(
                getActivity(),
                mSheetList.get(position).getUrl(),
                mSheetList.get(position).getDesc());
        startActivity(intent);
    }

    @Override
    public void onCommonItemLongClick(View view, final int position) {
        final boolean isExist = mPresenter.isExist(mSheetList.get(position).getId());
        DialogUtil.showGankBottomDialog(getContext(), isExist, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.mark_tv:
                        if (!isExist) {
                            mPresenter.saveSheetData(mSheetList.get(position));
                            showMsg(getResources().getString(R.string.mark_success));
                        } else {
                            mPresenter.deleteDataById(mSheetList.get(position).getId());
                            showMsg(getResources().getString(R.string.mark_cancel_already));
                        }
                        break;
                    case R.id.share_tv:
                        mPresenter.share(getContext(), mSheetList.get(position).getUrl(), mSheetList.get(position).getDesc());
                        break;
                }
                DialogUtil.hideBottomDialog();
            }
        });
    }

}
