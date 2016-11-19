package me.codpoe.gankio.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.codpoe.gankio.R;
import me.codpoe.gankio.data.bean.SearchBean;
import me.codpoe.gankio.search.adapter.SearchRvAdapter;
import me.codpoe.gankio.util.SoftInputUtil;
import me.codpoe.gankio.web.WebActivity;

public class SearchActivity extends AppCompatActivity implements
        SearchContract.View,
        SearchRvAdapter.OnSearchItemClickListener {

    @BindView(R.id.search_edit)
    EditText mSearchEdit;
    @BindView(R.id.search_btn)
    ImageButton mSearchBtn;
    @BindView(R.id.search_toolbar)
    Toolbar mSearchToolbar;
    @BindView(R.id.search_rv)
    RecyclerView mSearchRv;
    @BindView(R.id.search_refresh_lay)
    SwipeRefreshLayout mRefreshLay;
    @BindView(R.id.activity_search_coorlay)
    CoordinatorLayout mCoorLay;

    @Inject
    SearchPresenter mPresenter;

    public static final String TO_SEARCH = "to_search";

    private List<SearchBean.ResultsBean> mSearchList;
    private SearchRvAdapter mRvAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mPage = 1;
    private String mToSearch;

    public static Intent newIntent(Context context, String toSearch) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(TO_SEARCH, toSearch);
        return intent;
    }

    public void parseIntent() {
        mToSearch = getIntent().getStringExtra(TO_SEARCH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        DaggerSearchComponent.builder().searchModule(new SearchModule(this)).build().inject(this);

        // set up presenter
        mPresenter = new SearchPresenter(this);

        // get the text searched
        parseIntent();

        // set up toolbar
        mSearchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // set up edit text
        mSearchEdit.setText(mToSearch);
        mSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    mToSearch = mSearchEdit.getText().toString();
                    if (!mToSearch.equals("")) {
                        SoftInputUtil.from(mSearchEdit).hide();
                        mRefreshLay.setRefreshing(true);
                        mPage = 1;
                        mPresenter.loadSearch(mToSearch, "all", mPage);
                        return true;
                    }
                }
                return false;
            }
        });

        // set up refreshLay
        mRefreshLay.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mRefreshLay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mToSearch.equals("")) {
                    mRefreshLay.setRefreshing(false);
                } else {
                    mPage = 1;
                    mPresenter.loadSearch(mToSearch, "all", mPage);
                }

            }
        });
        mRefreshLay.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLay.setRefreshing(true);
                mPresenter.loadSearch(mToSearch, "all", mPage);
            }
        });


        // set up RecyclerView
        mSearchList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mRvAdapter = new SearchRvAdapter(this, mSearchList);
        mSearchRv.setLayoutManager(mLayoutManager);
        mSearchRv.setAdapter(mRvAdapter);
        mSearchRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvAdapter.setOnItemClickListener(this);
        mSearchRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mLayoutManager.findLastVisibleItemPosition() >= mLayoutManager.getItemCount() - 2 && dy > 0) {
                    mPresenter.loadSearch(mToSearch, "all", ++mPage);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public void showSearch(List<SearchBean.ResultsBean> list) {
        if (mPage == 1) {
            mSearchList.clear();
            mRvAdapter.notifyDataSetChanged();
        }
        for (SearchBean.ResultsBean bean : list) {
            mSearchList.add(bean);
            mRvAdapter.notifyItemInserted(mSearchList.size() - 1);
        }
        mRefreshLay.setRefreshing(false);
    }

    @Override
    public void showMsg(String msg) {
        Snackbar.make(mCoorLay, msg, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.search_btn)
    public void onClick() {
        mSearchEdit.setText("");
    }

    @Override
    public void onSearchItemClick(View view, int position) {
        Intent intent = WebActivity.newIntent(
                SearchActivity.this,
                mSearchList.get(position).getUrl(),
                mSearchList.get(position).getDesc());
        startActivity(intent);
    }

    @Override
    public void onSearchItemLongClick(View view, int position) {
        if (!mPresenter.isExist(mSearchList.get(position).getGanhuo_id())) {
            mPresenter.saveData(mSearchList.get(position));
            showMsg(getString(R.string.mark_success));
        } else {
            mPresenter.deleteataById(mSearchList.get(position).getGanhuo_id());
            showMsg(getString(R.string.mark_cancel_already));
        }
    }
}
