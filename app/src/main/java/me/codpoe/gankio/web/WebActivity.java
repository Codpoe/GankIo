package me.codpoe.gankio.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.codpoe.gankio.R;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.coor_lay)
    CoordinatorLayout mCoorLay;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private static final String EXTRA_WEB_URL = "web_url";
    private static final String EXTRA_WEB_TITLE = "web_title";

    private String mWebUrl;
    private String mWebTitle;

    public static Intent newIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_WEB_URL, url);
        intent.putExtra(EXTRA_WEB_TITLE, title);
        return intent;
    }

    public void parseIntent() {
        mWebUrl = getIntent().getStringExtra(EXTRA_WEB_URL);
        mWebTitle = getIntent().getStringExtra(EXTRA_WEB_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.bind(this);

        parseIntent();

        // set up toolbar
        mToolbar.inflateMenu(R.menu.web_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.open_with_browser:
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mWebUrl));
                        startActivity(intent);
                        break;
                    case R.id.copy_url:
                        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData data = ClipData.newPlainText("url", mWebUrl);
                        manager.setPrimaryClip(data);
                        Snackbar.make(mCoorLay, getResources().getString(R.string.copy_success), Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        // set up title TextView
        mTitleTv.setSelected(true);
        mTitleTv.setText(mWebTitle);

        // set up WebView
        mWebView.loadUrl(mWebUrl);
        // set up WebSettings
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // set up WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress != 100) {
                    if (mProgressBar.getVisibility() == View.INVISIBLE) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                onBackPressed();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
