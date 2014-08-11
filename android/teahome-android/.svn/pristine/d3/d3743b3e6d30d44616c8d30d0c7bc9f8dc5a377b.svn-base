package com.haorenao.app.ui.th;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haorenao.app.AppConfig;
import com.haorenao.app.AppContext;
import com.haorenao.app.AppException;
import com.haorenao.app.R;
import com.haorenao.app.adapter.ListViewCommentAdapter;
import com.haorenao.app.bean.Comment;
import com.haorenao.app.bean.CommentList;
import com.haorenao.app.bean.FavoriteList;
import com.haorenao.app.bean.News;
import com.haorenao.app.bean.Notice;
import com.haorenao.app.bean.Result;
import com.haorenao.app.bean.News.Relative;
import com.haorenao.app.common.StringUtils;
import com.haorenao.app.common.UIHelper;
import com.haorenao.app.ui.BaseActivity;
import com.haorenao.app.widget.BadgeView;
import com.haorenao.app.widget.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 茶友之家
 * 新闻详情
 * 
 */
public class ArticleDetail extends BaseActivity {
	
	private String articleUrl;
	private String articleName;

	private FrameLayout mHeader;
	private ImageView mHome;
	private ImageView mRefresh;
	private TextView mHeadTitle;
	private ProgressBar mProgressbar;
	private ScrollView mScrollView;


	private TextView mTitle;
	private TextView mAuthor;

	private WebView mWebView;
	private Handler mHandler;


	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;

	private GestureDetector gd;
	private boolean isFullScreen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_detail);

		articleUrl = getIntent().getStringExtra("article_url");
		articleName = getIntent().getStringExtra("article_name");
		
		this.initView();
		this.initData();

		// 注册双击全屏事件
		this.regOnDoubleEvent();
	}
	
	
	// 初始化视图控件
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {

		mHeader = (FrameLayout) findViewById(R.id.news_detail_header);
		mHome = (ImageView) findViewById(R.id.news_detail_home);
		mRefresh = (ImageView) findViewById(R.id.news_detail_refresh);
		mHeadTitle = (TextView) findViewById(R.id.news_detail_head_title);
		//mHeadTitle.setText(articleName);
		
		mProgressbar = (ProgressBar) findViewById(R.id.news_detail_head_progress);
		mScrollView = (ScrollView) findViewById(R.id.article_detail_scrollview);

		mTitle = (TextView) findViewById(R.id.article_detail_title);
		mAuthor = (TextView) findViewById(R.id.article_detail_author);

		mWebView = (WebView) findViewById(R.id.article_detail_webview);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultFontSize(15);
        UIHelper.addWebImageShow(this, mWebView);
		
		mHome.setOnClickListener(homeClickListener);
		mRefresh.setOnClickListener(refreshClickListener);
		mAuthor.setOnClickListener(authorClickListener);
	
	}

	// 初始化控件数据
	private void initData() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					headButtonSwitch(DATA_LOAD_COMPLETE);

					mTitle.setText(articleName);
					
					// 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
					boolean isLoadImage;
					AppContext ac = (AppContext) getApplication();
					if (AppContext.NETTYPE_WIFI == ac.getNetworkType()) {
						isLoadImage = true;
					} else {
						isLoadImage = ac.isLoadImage();
					}
					mWebView.loadUrl(articleUrl);
					mWebView.setWebViewClient(UIHelper.getWebViewClient());

					// 发送通知广播
					if (msg.obj != null) {
						UIHelper.sendBroadCast(ArticleDetail.this,
								(Notice) msg.obj);
					}
				} else if (msg.what == 0) {
					headButtonSwitch(DATA_LOAD_FAIL);

					UIHelper.ToastMessage(ArticleDetail.this,
							R.string.msg_load_is_null);
				} else if (msg.what == -1 && msg.obj != null) {
					headButtonSwitch(DATA_LOAD_FAIL);

					((AppException) msg.obj).makeToast(ArticleDetail.this);
				}
			}
		};

		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
		
	}


	/**
	 * 头部按钮展示
	 * 
	 * @param type
	 */
	private void headButtonSwitch(int type) {
		switch (type) {
		case DATA_LOAD_ING:
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.VISIBLE);
			mRefresh.setVisibility(View.GONE);
			break;
		case DATA_LOAD_COMPLETE:
			mScrollView.setVisibility(View.VISIBLE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		case DATA_LOAD_FAIL:
			mScrollView.setVisibility(View.GONE);
			mProgressbar.setVisibility(View.GONE);
			mRefresh.setVisibility(View.VISIBLE);
			break;
		}
	}

	private View.OnClickListener homeClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			UIHelper.showHome(ArticleDetail.this);
		}
	};

	private View.OnClickListener refreshClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			
		}
	};

	private View.OnClickListener authorClickListener = new View.OnClickListener() {
		public void onClick(View v) {
//			UIHelper.showUserCenter(v.getContext(), newsDetail.getAuthorId(),
//					newsDetail.getAuthor());
		}
	};

	private View.OnClickListener shareClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// 分享到
//			UIHelper.showShareDialog(ArticleDetail.this, articleName,
//					articleUrl);
		}
	};


	/**
	 * 注册双击全屏事件
	 */
	private void regOnDoubleEvent() {
		gd = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onDoubleTap(MotionEvent e) {
						isFullScreen = !isFullScreen;
						if (!isFullScreen) {
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
							getWindow().setAttributes(params);
							getWindow()
									.clearFlags(
											WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
							mHeader.setVisibility(View.VISIBLE);
						} else {
							WindowManager.LayoutParams params = getWindow()
									.getAttributes();
							params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
							getWindow().setAttributes(params);
							getWindow()
									.addFlags(
											WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
							mHeader.setVisibility(View.GONE);
						}
						return true;
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (isAllowFullScreen()) {
			gd.onTouchEvent(event);
		}
		return super.dispatchTouchEvent(event);
	}
}
