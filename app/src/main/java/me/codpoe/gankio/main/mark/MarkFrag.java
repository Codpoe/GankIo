package me.codpoe.gankio.main.mark;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import me.codpoe.gankio.R;
import me.codpoe.gankio.data.bean.MarkBean;
import me.codpoe.gankio.main.MainActivity;
import me.codpoe.gankio.main.mark.adapter.MarkRvAdapter;
import me.codpoe.gankio.util.DialogUtil;
import me.codpoe.gankio.util.DpPxUtil;
import me.codpoe.gankio.web.WebActivity;
import me.codpoe.gankio.widget.ArrowView;

/**
 * Created by Codpoe on 2016/10/8.
 */

public class MarkFrag extends Fragment implements
        MarkContract.View,
        MarkRvAdapter.OnMarkItemClickListener {

    @BindView(R.id.mark_rv)
    RecyclerView mMarkRv;
    @BindView(R.id.mark_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.mark_arrow_view)
    ArrowView mArrowView;
    @BindView(R.id.app_bar_lay)
    AppBarLayout mAppBarLay;
    @BindView(R.id.mark_drawer)
    DrawerLayout mDrawerLay;
    @BindView(R.id.type_nav)
    NavigationView mTypeNav;

    @Inject
    MarkPresenter mPresenter;

    private List<MarkBean> mMarkList;
    private MarkRvAdapter mMarkRvAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private String[] mTypes;
    private boolean mIsFirstVisible = true;
    private boolean mIsAnimEnd = true;
    private int mCurType = 0;
    private int mAppBarHeight;
    private View mNoContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_mark, container, false);
        ButterKnife.bind(this, view);
        DaggerMarkComponent.builder().markModule(new MarkModule(this)).build().inject(this);

        // set up presenter
        mPresenter = new MarkPresenter(this);

        mTypes = new String[] {
                getString(R.string.all),
                getString(R.string.ios_name),
                getString(R.string.android_name),
                getString(R.string.web_name),
                getString(R.string.app),
                getString(R.string.xiatuijian),
                getString(R.string.tuozhan),
                getString(R.string.fuli)
        };

        // set up Toolbar
        mAppBarHeight = DpPxUtil.dp2px(getActivity(), 56);
        mAppBarLay.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ((MainActivity)getActivity()).showOrHideBottomNavigation(verticalOffset != -mAppBarHeight);
            }
        });
        mToolbar.inflateMenu(R.menu.mark_toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mark_help_menu_item:
                        DialogUtil.showNormalDialog(getContext(), getString(R.string.mark_help_msg));
                        break;
                }
                return true;
            }
        });

        // set up ArrowView
        mArrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mDrawerLay.isDrawerOpen(mTypeNav)) {
                    mDrawerLay.openDrawer(mTypeNav);
                } else {
                    mDrawerLay.closeDrawers();
                }
            }
        });

        // set up drawer
        mDrawerLay.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mArrowView.setOffset(slideOffset);
            }
        });
        mTypeNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                mDrawerLay.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.all_menu_item:
                        changeToolbarTitle(0);
                        mPresenter.loadDataByType(mTypes[0]);
                        break;
                    case R.id.ios_menu_item:
                        changeToolbarTitle(1);
                        mPresenter.loadDataByType(mTypes[1]);
                        break;
                    case R.id.android_menu_item:
                        changeToolbarTitle(2);
                        mPresenter.loadDataByType(mTypes[2]);
                        break;
                    case R.id.web_menu_item:
                        changeToolbarTitle(3);
                        mPresenter.loadDataByType(mTypes[3]);
                        break;
                    case R.id.app_menu_item:
                        changeToolbarTitle(4);
                        mPresenter.loadDataByType(mTypes[4]);
                        break;
                    case R.id.xiatuijian_menu_item:
                        changeToolbarTitle(5);
                        mPresenter.loadDataByType(mTypes[5]);
                        break;
                    case R.id.tuozhan_menu_item:
                        changeToolbarTitle(6);
                        mPresenter.loadDataByType(mTypes[6]);
                        break;
                    case R.id.fuli_menu_item:
                        changeToolbarTitle(7);
                        mPresenter.loadDataByType(mTypes[7]);
                        break;
                }
                return true;
            }
        });
        mTypeNav.setCheckedItem(R.id.all_menu_item);

        // set up RecyclerView
        mMarkList = new ArrayList<>();
        mMarkRvAdapter = new MarkRvAdapter(getActivity(), mMarkList);
        mMarkRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMarkRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mMarkRv.setAdapter(mMarkRvAdapter);
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.END);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                mPresenter.deleteDataById(mMarkList.get(pos).getId());
                mMarkList.remove(pos);
                mMarkRvAdapter.notifyItemRemoved(pos);
                mTitleTv.setText(mTypes[mCurType] + "（" + mMarkList.size() + "）");
                showMsg(getResources().getString(R.string.mark_cancel_already));
            }
        });
        mItemTouchHelper.attachToRecyclerView(mMarkRv);
        mMarkRvAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter = null;
    }

    /**
     * Lazyload
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mIsFirstVisible) {
            mIsFirstVisible = false;
            mPresenter.loadDataByType("All");
        }
    }

    @Override
    public void showData(RealmResults<MarkBean> markBean) {
        mMarkList.clear();
        mMarkList.addAll(markBean);
        mMarkRvAdapter.notifyDataSetChanged();
        if (mIsAnimEnd) {
            mTitleTv.setText(mTypes[mCurType] + "（" + mMarkList.size() + "）");
        }
        if (mMarkList.size() == 0) {
            showNoContent();
        } else if (mNoContentView != null) {
            mNoContentView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoContent() {
        if (mNoContentView == null) {
            ViewStub viewStub = (ViewStub) getView().findViewById(R.id.no_content_stub);
            mNoContentView = viewStub.inflate();
        }
        mNoContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMsg(String msg) {
        ((MainActivity)getActivity()).showMsg(msg);
    }

    /**
     * change toolbar title
     * @param type
     */
    public void changeToolbarTitle(final int type) {
        if (type != mCurType) {
            mCurType = type;
            Animator animator1 = AnimatorInflater.loadAnimator(getContext(), R.animator.title_left_out);
            final Animator animator2 = AnimatorInflater.loadAnimator(getActivity(), R.animator.title_left_in);
            animator1.setTarget(mTitleTv);
            animator2.setTarget(mTitleTv);
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mIsAnimEnd = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mTitleTv.setText(mTypes[type] + "（" + mMarkList.size() + "）");
                    animator2.start();
                }
            });
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsAnimEnd = true;
                }
            });
            animator1.start();
        }
    }

    public void scrollToTop() {
        mMarkRv.smoothScrollToPosition(0);
    }

    @Override
    public void onMarkItemClick(View view, int position) {
        Intent intent = WebActivity.newIntent(
                getActivity(),
                mMarkList.get(position).getUrl(),
                mMarkList.get(position).getDesc()
        );
        startActivity(intent);
    }

}
