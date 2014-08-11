package com.haorenao.app.ui;

import greendroid.widget.MyQuickAction;
import greendroid.widget.QuickActionGrid;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.haorenao.app.AppConfig;
import com.haorenao.app.AppContext;
import com.haorenao.app.AppException;
import com.haorenao.app.AppManager;
import com.haorenao.app.R;
import com.haorenao.app.adapter.ListViewArticlesAdapter;
import com.haorenao.app.adapter.ListViewBBSAdapter;
import com.haorenao.app.adapter.ListViewItemAdapter;
import com.haorenao.app.adapter.ListViewItemCommentAdapter;
import com.haorenao.app.adapter.ListViewPromotionAdapter;
import com.haorenao.app.adapter.ListViewQuestionAdapter;
import com.haorenao.app.adapter.ListViewShopAdapter;
import com.haorenao.app.bean.Active;
import com.haorenao.app.bean.ActiveList;
import com.haorenao.app.bean.BlogList;
import com.haorenao.app.bean.NewsList;
import com.haorenao.app.bean.Notice;
import com.haorenao.app.bean.Post;
import com.haorenao.app.bean.PostList;
import com.haorenao.app.bean.Result;
import com.haorenao.app.bean.Tweet;
import com.haorenao.app.bean.TweetList;
import com.haorenao.app.bean.th.Article;
import com.haorenao.app.bean.th.ArticleList;
import com.haorenao.app.bean.th.Item;
import com.haorenao.app.bean.th.ItemComment;
import com.haorenao.app.bean.th.ItemCommentList;
import com.haorenao.app.bean.th.ItemList;
import com.haorenao.app.bean.th.Promotion;
import com.haorenao.app.bean.th.PromotionList;
import com.haorenao.app.bean.th.Question;
import com.haorenao.app.bean.th.QuestionList;
import com.haorenao.app.bean.th.Shop;
import com.haorenao.app.bean.th.ShopList;
import com.haorenao.app.bean.th.Threads;
import com.haorenao.app.bean.th.ThreadsList;
import com.haorenao.app.common.StringUtils;
import com.haorenao.app.common.UIHelper;
import com.haorenao.app.widget.BadgeView;
import com.haorenao.app.widget.NewDataToast;
import com.haorenao.app.widget.PullToRefreshListView;
import com.haorenao.app.widget.ScrollLayout;

/**
 * 应用程序首页
 */
public class Main extends BaseActivity {
	
	public static final int QUICKACTION_LOGIN_OR_LOGOUT = 0;
	public static final int QUICKACTION_USERINFO = 1;
	public static final int QUICKACTION_SOFTWARE = 2;
	public static final int QUICKACTION_SEARCH = 3;
	public static final int QUICKACTION_SETTING = 4;
	public static final int QUICKACTION_EXIT = 5;

	/**
	 * 内容左右滑动视图
	 */
	private ScrollLayout mScrollLayout;
	/**
	 * 顶部分类导航按钮
	 */
	private RadioButton[] mButtons;
	/**
	 * 顶部模块分类标签
	 */
	private String[] mHeadTitles;
	/**
	 * 视图总数
	 */
	private int mViewCount;
	/**
	 * 当前选中的视图
	 */
	private int mCurSel;

	/**
	 * 顶部Logo图片
	 */
	private ImageView mHeadLogo;
	/**
	 * 顶部模块名
	 */
	private TextView mHeadTitle;
	private ProgressBar mHeadProgress;
	/**
	 * 搜索按钮
	 */
	private ImageButton mHead_search;
	/**
	 * 发帖按钮
	 */
	private ImageButton mHeadPub_bbs;
	/**
	 * 提问按钮
	 */
	private ImageButton mHeadPub_question;

	/**
	 * 每一个子类下的分类
	 */
	private int curArticleCatalog = ArticleList.CATALOG_ALL;
	private int curBBSCatalog = ThreadsList.CATALOG_LIFE;
	private int curQuestionCatalog = QuestionList.CATALOG_ALL;
	private int curShopCatalog = ShopList.SHOPTYPE_PROMOT;

	private PullToRefreshListView lvArticles;
	private PullToRefreshListView lvBBS;
	private PullToRefreshListView lvQuestion;
	private PullToRefreshListView lvShop_Promot;
	private PullToRefreshListView lvShop_Shop;
	private PullToRefreshListView lvShop_Item;
	private PullToRefreshListView lvShop_ItemComment;
	
	

	
	private ListViewArticlesAdapter lvArticlesAdapter;
	private ListViewBBSAdapter lvBBSAdapter;
	private ListViewQuestionAdapter lvQuestionAdapter;
	private ListViewPromotionAdapter lvShopPromotionAdapter;
	private ListViewShopAdapter lvShopShopAdapter;
	private ListViewItemAdapter lvShopItemAdapter;
	private ListViewItemCommentAdapter lvShopItemCommentAdapter;

	private List<Article> lvArticlesData = new ArrayList<Article>();
	private List<Threads> lvBBSData = new ArrayList<Threads>();
	private List<Question> lvQuestionData = new ArrayList<Question>();
	private List<Promotion> lvShopPromotionData = new ArrayList<Promotion>();
	private List<Shop> lvShopShopData = new ArrayList<Shop>();
	private List<Item> lvShopItemData = new ArrayList<Item>();
	private List<ItemComment> lvShopItemCommentData = new ArrayList<ItemComment>();

	private Handler lvArticlesHandler;
	private Handler lvBBSHandler;
	private Handler lvQuestionHandler;
	private Handler lvShopPromotionHandler;
	private Handler lvShopShopHandler;
	private Handler lvShopItemHandler;
	private Handler lvShopItemCommentHandler;

	private int lvArticlesSumData;
	private int lvBBSSumData;
	private int lvQuestionSumData;
	private int lvShopPromotionSumData;
	private int lvShopShopSumData;
	private int lvShopItemSumData;
	private int lvShopItemCommentSumData;
	
	private RadioButton fbArticles;
	private RadioButton fbBBS;
	private RadioButton fbQuestion;
	private RadioButton fbShop;
	private ImageView fbHome;

	/**
	 * 资讯页面下的分类按钮
	 */
	private Button framebtn_Article_lastest;
	private Button framebtn_Article_blog;
	private Button framebtn_Article_recommend;
	
	/**
	 * 问答页面下的分类按钮
	 */
	private Button framebtn_Question_ask;
	private Button framebtn_Question_other;
	
	/**
	 * 社区页面下的分类按钮
	 */
	private Button framebtn_BBS_life;
	private Button framebtn_BBS_admin;
	
	/**
	 * 商店分类
	 */
	private Button framebtn_Shop_promot;
	private Button framebtn_Shop_shop;
	private Button framebtn_Shop_item;
	private Button framebtn_Shop_comment;

	private View lvArticles_footer;
	private View lvBBS_footer;
	private View lvQuestion_footer;
	private View lvShopPromotion_footer;
	private View lvShopShop_footer;
	private View lvShopItem_footer;
	private View lvShopItemComment_footer;

	private TextView lvArticles_foot_more;
	private TextView lvBBS_foot_more;
	private TextView lvQuestion_foot_more;
	private TextView lvShopPromotion_foot_more;
	private TextView lvShopShop_foot_more;
	private TextView lvShopItem_foot_more;
	private TextView lvShopItemComment_foot_more;

	private ProgressBar lvArticles_foot_progress;
	private ProgressBar lvBBS_foot_progress;
	private ProgressBar lvQuestion_foot_progress;
	private ProgressBar lvShopPromotion_foot_progress;
	private ProgressBar lvShopShop_foot_progress;
	private ProgressBar lvShopItem_foot_progress;
	private ProgressBar lvShopItemComment_foot_progress;

	public static BadgeView bv_active;
	public static BadgeView bv_atme;
	public static BadgeView bv_review;

	private QuickActionWidget mGrid;// 快捷栏控件

	private boolean isClearNotice = false;
	private int curClearNoticeType = 0;

	private BBSPostReceiver bbsPostReceiver;// 帖子发布发布接收器
	private AppContext appContext;// 全局Context
	
	private DoubleClickExitHelper mDoubleClickExitHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mDoubleClickExitHelper = new DoubleClickExitHelper(this);
		
		// 注册广播接收器
		bbsPostReceiver = new BBSPostReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.haorenao.app.action.APP_BBSPUB");
		registerReceiver(bbsPostReceiver, filter);

		appContext = (AppContext) getApplication();
		// 网络连接判断
		if (!appContext.isNetworkConnected())
			UIHelper.ToastMessage(this, R.string.network_not_connected);
		// 初始化登录
		//appContext.initLoginInfo();

		this.initHeadView();
		this.initFootBar();
		this.initPageScroll();
		this.initFrameButton();
		this.initBadgeView();
		this.initQuickActionGrid();
		this.initFrameListView();

		// 启动轮询通知信息
		this.foreachUserNotice();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mViewCount == 0)
			mViewCount = 4;
		if (mCurSel == 0 && !fbArticles.isChecked()) {
			fbArticles.setChecked(true);
			fbBBS.setChecked(false);
			fbQuestion.setChecked(false);
			fbShop.setChecked(false);
		}
		// 读取左右滑动配置
		mScrollLayout.setIsScroll(appContext.isScroll());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bbsPostReceiver);
	}
	
	

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (intent.getBooleanExtra("LOGIN", false)) {
			// 加载动弹、动态及留言(当前动弹的catalog大于0表示用户的uid)
			if (lvQuestionData.isEmpty() && curQuestionCatalog > 0 && mCurSel == 2) {
				this.loadLvQuestionData(curQuestionCatalog, 0, lvQuestionHandler,UIHelper.LISTVIEW_ACTION_INIT);
			} else if (mCurSel == 3) {
				if (lvShopPromotionData.isEmpty()) {
					this.loadLvShopPromotionData(curShopCatalog, 0, lvShopPromotionHandler,UIHelper.LISTVIEW_ACTION_INIT);
				}
				if (lvShopShopData.isEmpty()) {
					this.loadLvShopShopData(0,0, lvShopShopHandler,UIHelper.LISTVIEW_ACTION_INIT);
				}
			}
		} else if (intent.getBooleanExtra("NOTICE", false)) {
			// 查看最新信息
			mScrollLayout.scrollToScreen(3);
		}
	}

	public class BBSPostReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, Intent intent) {
			int what = intent.getIntExtra("MSG_WHAT", 0);
			if (what == 1) {
				Result res = (Result) intent.getSerializableExtra("RESULT");
				UIHelper.ToastMessage(context, res.getErrorMessage(), 1000);
				if (res.OK()) {
					// 发送通知广播
					if (res.getNotice() != null) {
						UIHelper.sendBroadCast(context, res.getNotice());
					}
					// 发完动弹后-刷新最新动弹、我的动弹&最新动态(当前界面必须是动弹|动态)
					if (curQuestionCatalog >= 0 && mCurSel == 2) {
						loadLvQuestionData(curQuestionCatalog, 0, lvQuestionHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
					} else if (curShopCatalog == ActiveList.CATALOG_LASTEST&& mCurSel == 3) {
						
						loadLvShopPromotionData(curShopCatalog, 0, lvShopPromotionHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
					}
				}
			} else {
				final Tweet tweet = (Tweet) intent
						.getSerializableExtra("TWEET");
				final Handler handler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 1) {
							Result res = (Result) msg.obj;
							UIHelper.ToastMessage(context,res.getErrorMessage(), 1000);
							if (res.OK()) {
								// 发送通知广播
								if (res.getNotice() != null) {
									UIHelper.sendBroadCast(context,res.getNotice());
								}
								// 发完动弹后-刷新最新、我的动弹&最新动态
								if (curQuestionCatalog >= 0 && mCurSel == 2) {
									loadLvQuestionData(curQuestionCatalog, 0,lvQuestionHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
								} else if (curShopCatalog == ActiveList.CATALOG_LASTEST&& mCurSel == 3) {
									loadLvShopPromotionData(curShopCatalog, 0,lvShopPromotionHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
								}
								if (TweetPub.mContext != null) {
									// 清除动弹保存的临时编辑内容
									appContext.removeProperty(AppConfig.TEMP_TWEET,AppConfig.TEMP_TWEET_IMAGE);
									((Activity) TweetPub.mContext).finish();
								}
							}
						} else {
							((AppException) msg.obj).makeToast(context);
							if (TweetPub.mContext != null&&TweetPub.mMessage != null)
								TweetPub.mMessage.setVisibility(View.GONE);
						}
					}
				};
				Thread thread = new Thread() {
					public void run() {
						Message msg = new Message();
						try {
							Result res = appContext.pubTweet(tweet);
							msg.what = 1;
							msg.obj = res;
						} catch (AppException e) {
							e.printStackTrace();
							msg.what = -1;
							msg.obj = e;
						}
						handler.sendMessage(msg);
					}
				};
				if (TweetPub.mContext != null)
					UIHelper.showResendTweetDialog(TweetPub.mContext, thread);
				else
					UIHelper.showResendTweetDialog(context, thread);
			}
		}
	}

	/**
	 * 初始化快捷栏
	 */
	private void initQuickActionGrid() {
		mGrid = new QuickActionGrid(this);
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_login,
				R.string.main_menu_login));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_myinfo,
				R.string.main_menu_myinfo));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.ic_menu_software, R.string.main_menu_software));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_sweep,
				R.string.main_menu_sweep));
		mGrid.addQuickAction(new MyQuickAction(this,
				R.drawable.ic_menu_setting, R.string.main_menu_setting));
		mGrid.addQuickAction(new MyQuickAction(this, R.drawable.ic_menu_exit,
				R.string.main_menu_exit));

		mGrid.setOnQuickActionClickListener(mActionListener);
	}

	/**
	 * 快捷栏item点击事件
	 */
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
		public void onQuickActionClicked(QuickActionWidget widget, int position) {
			switch (position) {
			case QUICKACTION_LOGIN_OR_LOGOUT:// 用户登录-注销
				UIHelper.loginOrLogout(Main.this);
				break;
			case QUICKACTION_USERINFO:// 我的资料
				UIHelper.showUserInfo(Main.this);
				break;
			case QUICKACTION_SOFTWARE:// 
				UIHelper.showSoftware(Main.this);
				break;
			case QUICKACTION_SEARCH:// 
				UIHelper.showCapture(Main.this);
				break;
			case QUICKACTION_SETTING:// 设置
				UIHelper.showSetting(Main.this);
				break;
			case QUICKACTION_EXIT:// 退出
				AppManager.getAppManager().finishActivity(Main.this);
				break;
			}
		}
	};
	
	
	/**
	 * 初始化所有ListView
	 */
	private void initFrameListView() {
		// 初始化listview控件
		this.initArticlesListView();
		this.initBBSListView();
		this.initQuestionListView();
		this.initShopPromotionListView();
		this.initShopShopListView();
		this.initShopItemListView();
		this.initShopItemCommentListView();
		// 加载listview数据
		this.initFrameListViewData();
	}

	/**
	 * 初始化所有ListView数据
	 */
	private void initFrameListViewData() {
		// 初始化Handler
		lvArticlesHandler = this.getLvHandler(lvArticles, lvArticlesAdapter,
				lvArticles_foot_more, lvArticles_foot_progress, AppContext.PAGE_SIZE);
		lvBBSHandler = this.getLvHandler(lvBBS, lvBBSAdapter,
				lvBBS_foot_more, lvBBS_foot_progress,
				AppContext.PAGE_SIZE);
		lvQuestionHandler = this.getLvHandler(lvQuestion, lvQuestionAdapter,
				lvQuestion_foot_more, lvQuestion_foot_progress, AppContext.PAGE_SIZE);
		lvShopPromotionHandler = this.getLvHandler(lvShop_Promot, lvShopPromotionAdapter,
				lvShopPromotion_foot_more, lvShopPromotion_foot_progress,
				AppContext.PAGE_SIZE);
		lvShopShopHandler = this.getLvHandler(lvShop_Shop, lvShopShopAdapter, lvShopShop_foot_more,
				lvShopShop_foot_progress, AppContext.PAGE_SIZE);
		lvShopItemHandler = this.getLvHandler(lvShop_Item, lvShopItemAdapter, lvShopItem_foot_more,
				lvShopItem_foot_progress, AppContext.PAGE_SIZE);
		lvShopItemCommentHandler = this.getLvHandler(lvShop_ItemComment, lvShopItemCommentAdapter, lvShopItemComment_foot_more,
				lvShopItemComment_foot_progress, AppContext.PAGE_SIZE);

		// 加载资讯数据
		if (lvArticlesData.isEmpty()) {
			loadLvArticlesData(curArticleCatalog, 0, lvArticlesHandler,
					UIHelper.LISTVIEW_ACTION_INIT);
		}
	}

	/**
	 * 初始化新闻列表
	 */
	private void initArticlesListView() {
		lvArticlesAdapter = new ListViewArticlesAdapter(this, lvArticlesData,
				R.layout.article_listitem);
		lvArticles_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvArticles_foot_more = (TextView) lvArticles_footer
				.findViewById(R.id.listview_foot_more);
		lvArticles_foot_progress = (ProgressBar) lvArticles_footer
				.findViewById(R.id.listview_foot_progress);
		lvArticles = (PullToRefreshListView) findViewById(R.id.frame_listview_article);
		lvArticles.addFooterView(lvArticles_footer);// 添加底部视图 必须在setAdapter前
		lvArticles.setAdapter(lvArticlesAdapter);
		lvArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvArticles_footer)
					return;

				Article news = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					news = (Article) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.article_listitem_title);
					news = (Article) tv.getTag();
				}
				if (news == null)
					return;

				// 跳转到新闻详情
				UIHelper.showArticleDetail(view.getContext(), news);
			}
		});
		lvArticles.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvArticles.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvArticlesData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvArticles_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvArticles.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvArticles.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvArticles_foot_more.setText(R.string.load_ing);
					lvArticles_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvArticlesSumData / AppContext.PAGE_SIZE;
					loadLvArticlesData(curArticleCatalog, pageIndex, lvArticlesHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvArticles.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvArticles.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadLvArticlesData(curArticleCatalog, 0, lvArticlesHandler,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}

	

	/**
	 * 初始化帖子列表
	 */
	private void initBBSListView() {
		lvBBSAdapter = new ListViewBBSAdapter(this, lvBBSData,
				R.layout.bbs_listitem);
		lvBBS_footer = getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		lvBBS_foot_more = (TextView) lvBBS_footer
				.findViewById(R.id.listview_foot_more);
		lvBBS_foot_progress = (ProgressBar) lvBBS_footer
				.findViewById(R.id.listview_foot_progress);
		lvBBS = (PullToRefreshListView) findViewById(R.id.frame_listview_bbs);
		lvBBS.addFooterView(lvBBS_footer);// 添加底部视图 必须在setAdapter前
		lvBBS.setAdapter(lvBBSAdapter);
		lvBBS
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 点击头部、底部栏无效
						if (position == 0 || view == lvBBS_footer)
							return;

						Threads threads = null;
						// 判断是否是TextView
						if (view instanceof TextView) {
							threads = (Threads) view.getTag();
						} else {
							TextView tv = (TextView) view
									.findViewById(R.id.bbs_listitem_title);
							threads = (Threads) tv.getTag();
						}
						if (threads == null)
							return;

						// 跳转到帖子详情
						UIHelper.showBBSDetail(view.getContext(),
								threads);
					}
				});
		lvBBS.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvBBS.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvBBSData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvBBS_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvBBS.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvBBS.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvBBS_foot_more.setText(R.string.load_ing);
					lvBBS_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvBBSSumData / AppContext.PAGE_SIZE;
					loadLvBBSData(curBBSCatalog, pageIndex,lvBBSHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
							
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvBBS.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
						
			}
		});
		lvBBS
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						loadLvBBSData(curBBSCatalog, 0,lvBBSHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
								
					}
				});
	}

	/**
	 * 初始问答列表
	 */
	private void initQuestionListView() {
		lvQuestionAdapter = new ListViewQuestionAdapter(this, lvQuestionData,R.layout.question_listitem);
		lvQuestion_footer = getLayoutInflater().inflate(R.layout.listview_footer,null);
		lvQuestion_foot_more = (TextView) lvQuestion_footer.findViewById(R.id.listview_foot_more);
		lvQuestion_foot_progress = (ProgressBar) lvQuestion_footer.findViewById(R.id.listview_foot_progress);
		lvQuestion = (PullToRefreshListView) findViewById(R.id.frame_listview_question);
		lvQuestion.addFooterView(lvQuestion_footer);// 添加底部视图 必须在setAdapter前
		lvQuestion.setAdapter(lvQuestionAdapter);
		lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvQuestion_footer)
					return;

				Question question = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					question = (Question) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.question_listitem_title);
					question = (Question) tv.getTag();
				}
				if (question == null)
					return;   
				
				// 跳转到问答详细页面
				UIHelper.showQuestionDetail(view.getContext(), question);
			}
		});
		lvQuestion.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvQuestion.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvQuestionData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvQuestion_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvQuestion.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvQuestion.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvQuestion_foot_more.setText(R.string.load_ing);
					lvQuestion_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvQuestionSumData / AppContext.PAGE_SIZE;
					loadLvQuestionData(curQuestionCatalog, pageIndex, lvQuestionHandler,UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvQuestion.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		
		lvQuestion.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				loadLvQuestionData(curQuestionCatalog, 0, lvQuestionHandler,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}

	/**
	 * 初始化动态列表
	 */
	private void initShopPromotionListView() {
		lvShopPromotionAdapter = new ListViewPromotionAdapter(this, lvShopPromotionData,
				R.layout.shop_promotion_listitem);
		lvShopPromotion_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvShopPromotion_foot_more = (TextView) lvShopPromotion_footer
				.findViewById(R.id.listview_foot_more);
		lvShopPromotion_foot_progress = (ProgressBar) lvShopPromotion_footer
				.findViewById(R.id.listview_foot_progress);
		lvShop_Promot = (PullToRefreshListView) findViewById(R.id.frame_listview_shop_promotion);
		lvShop_Promot.addFooterView(lvShopPromotion_footer);// 添加底部视图 必须在setAdapter前
		lvShop_Promot.setAdapter(lvShopPromotionAdapter);
		lvShop_Promot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopPromotion_footer)
					return;

				Active active = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					active = (Active) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.active_listitem_username);
					active = (Active) tv.getTag();
				}
				if (active == null)
					return;

				// 跳转
				UIHelper.showActiveRedirect(view.getContext(), active);
			}
		});
		lvShop_Promot.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvShop_Promot.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvShopPromotionData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvShopPromotion_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvShop_Promot.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvShop_Promot.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvShopPromotion_foot_more.setText(R.string.load_ing);
					lvShopPromotion_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvShopPromotionSumData / AppContext.PAGE_SIZE;
					loadLvShopPromotionData(curShopCatalog, pageIndex,
							lvShopPromotionHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvShop_Promot.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvShop_Promot.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// 处理通知信息
				if (curShopCatalog == ActiveList.CATALOG_ATME
						&& bv_atme.isShown()) {
					isClearNotice = true;
					curClearNoticeType = Notice.TYPE_ATME;
				} else if (curShopCatalog == ActiveList.CATALOG_COMMENT
						&& bv_review.isShown()) {
					isClearNotice = true;
					curClearNoticeType = Notice.TYPE_COMMENT;
				}
				// 刷新数据
				loadLvShopPromotionData(curShopCatalog, 0, lvShopPromotionHandler,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}

	/**
	 * 初始化商店列表
	 */
	private void initShopShopListView() {
		lvShopShopAdapter = new ListViewShopAdapter(this, lvShopShopData,
				R.layout.shop_shop_listitem);
		lvShopShop_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvShopShop_foot_more = (TextView) lvShopShop_footer
				.findViewById(R.id.listview_foot_more);
		lvShopShop_foot_progress = (ProgressBar) lvShopShop_footer
				.findViewById(R.id.listview_foot_progress);
		lvShop_Shop = (PullToRefreshListView) findViewById(R.id.frame_listview_shop_shop);
		lvShop_Shop.addFooterView(lvShopShop_footer);// 添加底部视图 必须在setAdapter前
		lvShop_Shop.setAdapter(lvShopShopAdapter);
		lvShop_Shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopShop_footer)
					return;

				Shop shop = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					shop = (Shop) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.message_listitem_username);
					shop = (Shop) tv.getTag();
				}
				if (shop == null)
					return;

				// 跳转到留言详情
				UIHelper.showShopDetail(view.getContext(),
						0, shop.getId());
			}
		});
		lvShop_Shop.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvShop_Shop.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvShopShopData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvShopShop_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvShop_Shop.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvShop_Shop.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvShopShop_foot_more.setText(R.string.load_ing);
					lvShopShop_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvShopShopSumData / AppContext.PAGE_SIZE;
					loadLvShopShopData(0,pageIndex, lvShopShopHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvShop_Shop.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvShop_Shop.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopShop_footer)
					return false;

				Shop _shop = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					_shop = (Shop) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.message_listitem_username);
					_shop = (Shop) tv.getTag();
				}
				if (_shop == null)
					return false;

				final Shop message = _shop;

				
				//UIHelper.showMessageListOptionDialog(Main.this, message, thread);
				return true;
			}
		});
		lvShop_Shop.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// 清除通知信息
				
				// 刷新数据
				loadLvShopShopData(0,0, lvShopShopHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}
	
	/**
	 * 初始化商品列表
	 */
	private void initShopItemListView() {
		lvShopItemAdapter = new ListViewItemAdapter(this, lvShopItemData,
				R.layout.shop_item_listitem);
		lvShopItem_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvShopItem_foot_more = (TextView) lvShopItem_footer
				.findViewById(R.id.listview_foot_more);
		lvShopItem_foot_progress = (ProgressBar) lvShopItem_footer
				.findViewById(R.id.listview_foot_progress);
		lvShop_Item = (PullToRefreshListView) findViewById(R.id.frame_listview_shop_item);
		lvShop_Item.addFooterView(lvShopItem_footer);// 添加底部视图 必须在setAdapter前
		lvShop_Item.setAdapter(lvShopItemAdapter);
		lvShop_Item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopItem_footer)
					return;

				Item item = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					item = (Item) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.message_listitem_username);
					item = (Item) tv.getTag();
				}
				if (item == null)
					return;

				// 跳转到留言详情
				UIHelper.showShopDetail(view.getContext(),
						0, item.getId());
			}
		});
		lvShop_Item.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvShop_Item.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvShopItemData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvShopItem_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvShop_Item.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvShop_Item.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvShopItem_foot_more.setText(R.string.load_ing);
					lvShopItem_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvShopItemSumData / AppContext.PAGE_SIZE;
					loadLvShopShopData(0,pageIndex, lvShopItemHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvShop_Item.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvShop_Item.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopItem_footer)
					return false;

				Item _shop = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					_shop = (Item) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.message_listitem_username);
					_shop = (Item) tv.getTag();
				}
				if (_shop == null)
					return false;

				final Item message = _shop;

				
				//UIHelper.showMessageListOptionDialog(Main.this, message, thread);
				return true;
			}
		});
		lvShop_Item.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// 清除通知信息
				
				// 刷新数据
				loadLvShopItemData(0,0, lvShopItemHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}
	
	/**
	 * 初始化商品评价列表
	 */
	private void initShopItemCommentListView() {
		lvShopItemCommentAdapter = new ListViewItemCommentAdapter(this, lvShopItemCommentData,
				R.layout.shop_itemcomment_listitem);
		lvShopItemComment_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvShopItemComment_foot_more = (TextView) lvShopItemComment_footer
				.findViewById(R.id.listview_foot_more);
		lvShopItemComment_foot_progress = (ProgressBar) lvShopItemComment_footer
				.findViewById(R.id.listview_foot_progress);
		lvShop_ItemComment = (PullToRefreshListView) findViewById(R.id.frame_listview_shop_comment);
		lvShop_ItemComment.addFooterView(lvShopItemComment_footer);// 添加底部视图 必须在setAdapter前
		lvShop_ItemComment.setAdapter(lvShopItemCommentAdapter);
		lvShop_ItemComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopItem_footer)
					return;

				ItemComment item = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					item = (ItemComment) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.message_listitem_username);
					item = (ItemComment) tv.getTag();
				}
				if (item == null)
					return;

				// 跳转到留言详情
//				UIHelper.showShopDetail(view.getContext(),
//						0, item.getId());
			}
		});
		lvShop_ItemComment.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvShop_ItemComment.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvShopItemCommentData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvShopItemComment_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvShop_ItemComment.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvShop_ItemComment.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvShopItemComment_foot_more.setText(R.string.load_ing);
					lvShopItemComment_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = lvShopItemCommentSumData / AppContext.PAGE_SIZE;
					loadLvShopShopData(0,pageIndex, lvShopItemCommentHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvShop_ItemComment.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvShop_ItemComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvShopItemComment_footer)
					return false;

				ItemComment _shop = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					_shop = (ItemComment) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.message_listitem_username);
					_shop = (ItemComment) tv.getTag();
				}
				if (_shop == null)
					return false;

				final ItemComment message = _shop;

				
				//UIHelper.showMessageListOptionDialog(Main.this, message, thread);
				return true;
			}
		});
		lvShop_ItemComment.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				// 清除通知信息
				
				// 刷新数据
				loadLvShopItemCommentData(0,0, lvShopItemHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
	}
	
	/**
	 * 初始化头部视图
	 */
	private void initHeadView() {
		mHeadLogo = (ImageView) findViewById(R.id.main_head_logo);
		mHeadTitle = (TextView) findViewById(R.id.main_head_title);
		mHeadProgress = (ProgressBar) findViewById(R.id.main_head_progress);
		mHead_search = (ImageButton) findViewById(R.id.main_head_search);
		mHeadPub_bbs = (ImageButton) findViewById(R.id.main_head_pub_bbs);
		mHeadPub_question = (ImageButton) findViewById(R.id.main_head_pub_question);

		mHead_search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UIHelper.showSearch(v.getContext());
			}
		});
		mHeadPub_bbs.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UIHelper.showBBSPub(Main.this, curBBSCatalog);
			}
		});
		mHeadPub_question.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UIHelper.showTweetPub(Main.this);
			}
		});
	}

	/**
	 * 初始化底部栏
	 */
	private void initFootBar() {
		fbArticles = (RadioButton) findViewById(R.id.main_footbar_article);
		fbBBS = (RadioButton) findViewById(R.id.main_footbar_bbs);
		fbQuestion = (RadioButton) findViewById(R.id.main_footbar_question);
		fbShop = (RadioButton) findViewById(R.id.main_footbar_shop);

		fbHome = (ImageView) findViewById(R.id.main_footbar_home);
		fbHome.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				UIHelper.showUserHomeView(Main.this);
				
				// 展示快捷栏&判断是否登录&是否加载文章图片
//				UIHelper.showSettingLoginOrLogout(Main.this,
//						mGrid.getQuickAction(0));
//				mGrid.show(v);
			}
		});
	}

	/**
	 * 初始化通知信息标签控件
	 */
	private void initBadgeView() {
		bv_active = new BadgeView(this, fbShop);
		bv_active.setBackgroundResource(R.drawable.widget_count_bg);
		bv_active.setIncludeFontPadding(false);
		bv_active.setGravity(Gravity.CENTER);
		bv_active.setTextSize(8f);
		bv_active.setTextColor(Color.WHITE);

		bv_atme = new BadgeView(this, framebtn_Shop_shop);
		bv_atme.setBackgroundResource(R.drawable.widget_count_bg);
		bv_atme.setIncludeFontPadding(false);
		bv_atme.setGravity(Gravity.CENTER);
		bv_atme.setTextSize(8f);
		bv_atme.setTextColor(Color.WHITE);

		bv_review = new BadgeView(this, framebtn_Shop_item);
		bv_review.setBackgroundResource(R.drawable.widget_count_bg);
		bv_review.setIncludeFontPadding(false);
		bv_review.setGravity(Gravity.CENTER);
		bv_review.setTextSize(8f);
		bv_review.setTextColor(Color.WHITE);
	}

	/**
	 * 初始化水平滚动翻页
	 */
	private void initPageScroll() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_linearlayout_footer);
		//版块名
		mHeadTitles = getResources().getStringArray(R.array.head_titles);
		mViewCount = mScrollLayout.getChildCount();
		mButtons = new RadioButton[mViewCount];

		for (int i = 0; i < mViewCount; i++) {
			mButtons[i] = (RadioButton) linearLayout.getChildAt(i * 2);
			mButtons[i].setTag(i);
			mButtons[i].setChecked(false);
			mButtons[i].setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int pos = (Integer) (v.getTag());
					// 点击当前项刷新
					if (mCurSel == pos) {
						switch (pos) {
						case 0:// 资讯+博客
							lvArticles.clickRefresh();
							break;
						case 1:// 动弹
							lvQuestion.clickRefresh();
							break;
						case 2:// 问答
							lvBBS.clickRefresh();
							break;
						case 3:// 动态+留言
							if (lvShop_Promot.getVisibility() == View.VISIBLE)
								lvShop_Promot.clickRefresh();
							else
								lvShop_Shop.clickRefresh();
							break;
						}
					}
					mScrollLayout.snapToScreen(pos);
				}
			});
		}

		// 设置第一显示屏
		mCurSel = 0;
		mButtons[mCurSel].setChecked(true);

		mScrollLayout
				.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener() {
					public void OnViewChange(int viewIndex) {
						// 切换列表视图-如果列表数据为空：加载数据
						switch (viewIndex) {
						case 0:// 资讯
							if (lvArticlesData.isEmpty()) {
									loadLvArticlesData(curArticleCatalog, 0,
											lvArticlesHandler,
											UIHelper.LISTVIEW_ACTION_INIT);
							}
							
							break;
						case 1:// 社区
							if (lvBBSData.isEmpty()) {
								loadLvBBSData(curBBSCatalog, 0,
										lvBBSHandler,
										UIHelper.LISTVIEW_ACTION_INIT);
							}
							break;
						case 2:// 问答
							if (lvQuestionData.isEmpty()) {
								loadLvQuestionData(curQuestionCatalog, 0,
										lvQuestionHandler,
										UIHelper.LISTVIEW_ACTION_INIT);
							}
							break;
						case 3:// 商店
								// 判断登录
							if (!appContext.isLogin()) {
								if (lvShop_Promot.getVisibility() == View.VISIBLE
										&& lvShopPromotionData.isEmpty()) {
									lvShopPromotion_foot_more
											.setText(R.string.load_empty);
									lvShopPromotion_foot_progress
											.setVisibility(View.GONE);
								} else if (lvShop_Shop.getVisibility() == View.VISIBLE
										&& lvShopShopData.isEmpty()) {
									lvShopShop_foot_more
											.setText(R.string.load_empty);
									lvShopShop_foot_progress
											.setVisibility(View.GONE);
								}
								UIHelper.showLoginDialog(Main.this);
								break;
							}
							// 处理通知信息
							if (bv_atme.isShown())
								frameShopBtnOnClick(framebtn_Shop_shop,
										ActiveList.CATALOG_ATME,
										UIHelper.LISTVIEW_ACTION_REFRESH);
							else if (bv_review.isShown())
								frameShopBtnOnClick(framebtn_Shop_item,
										ActiveList.CATALOG_COMMENT,
										UIHelper.LISTVIEW_ACTION_REFRESH);
							else if (lvShop_Promot.getVisibility() == View.VISIBLE
									&& lvShopPromotionData.isEmpty())
								loadLvShopPromotionData(curShopCatalog, 0,
										lvShopPromotionHandler,
										UIHelper.LISTVIEW_ACTION_INIT);
							else if (lvShop_Shop.getVisibility() == View.VISIBLE
									&& lvShopShopData.isEmpty())
								loadLvShopShopData(0,0, lvShopShopHandler,
										UIHelper.LISTVIEW_ACTION_INIT);
							else if (lvShop_Item.getVisibility() == View.VISIBLE
									&& lvShopItemData.isEmpty())
								loadLvShopItemData(0,0, lvShopItemHandler,
										UIHelper.LISTVIEW_ACTION_INIT);
							else if (lvShop_ItemComment.getVisibility() == View.VISIBLE
									&& lvShopItemCommentData.isEmpty())
								loadLvShopItemCommentData(0,0, lvShopItemCommentHandler,
										UIHelper.LISTVIEW_ACTION_INIT);
							break;
						}
						setCurPoint(viewIndex);
					}
				});
	}

	/**
	 * 设置底部栏当前焦点
	 * 
	 * @param index
	 */
	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index)
			return;

		mButtons[mCurSel].setChecked(false);
		mButtons[index].setChecked(true);
		mHeadTitle.setText(mHeadTitles[index]);
		mCurSel = index;

		mHead_search.setVisibility(View.GONE);
		mHeadPub_bbs.setVisibility(View.GONE);
		mHeadPub_question.setVisibility(View.GONE);
		// 头部logo
		if (index == 0) {
			//资讯
			mHeadLogo.setImageResource(R.drawable.frame_logo_news);
			mHead_search.setVisibility(View.VISIBLE);
		} else if (index == 1) {
			//社区
			mHeadLogo.setImageResource(R.drawable.frame_logo_post);
			mHeadPub_bbs.setVisibility(View.VISIBLE);
		} else if (index == 2) {
			//问答
			mHeadLogo.setImageResource(R.drawable.frame_logo_tweet);
			mHeadPub_question.setVisibility(View.VISIBLE);
		} else if (index == 3) {
			//商店
			mHeadLogo.setImageResource(R.drawable.frame_logo_active);
			mHeadPub_question.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 初始化各个主页的按钮(资讯、问答、动弹、动态、留言)
	 */
	private void initFrameButton() {
		// 初始化按钮控件
		framebtn_Article_lastest = (Button) findViewById(R.id.frame_btn_news_lastest);
		framebtn_Article_blog = (Button) findViewById(R.id.frame_btn_news_blog);
		framebtn_Article_recommend = (Button) findViewById(R.id.frame_btn_news_recommend);
		
		framebtn_Question_ask = (Button) findViewById(R.id.frame_btn_question_ask);
		framebtn_Question_other = (Button) findViewById(R.id.frame_btn_question_other);
		
		framebtn_BBS_life = (Button) findViewById(R.id.frame_btn_bbs_life);
		framebtn_BBS_admin = (Button) findViewById(R.id.frame_btn_bbs_admin);
		
		framebtn_Shop_promot = (Button) findViewById(R.id.frame_btn_shop_promotion);
		framebtn_Shop_shop = (Button) findViewById(R.id.frame_btn_shop_shop);
		framebtn_Shop_item = (Button) findViewById(R.id.frame_btn_shop_item);
		framebtn_Shop_comment = (Button) findViewById(R.id.frame_btn_shop_comment);
		
		// 设置首选择项
		framebtn_Article_lastest.setEnabled(false);
		framebtn_Question_ask.setEnabled(false);
		framebtn_BBS_life.setEnabled(false);
		framebtn_Shop_promot.setEnabled(false);
		// 资讯
		framebtn_Article_lastest.setOnClickListener(frameArticlesBtnClick(
				framebtn_Article_lastest, NewsList.CATALOG_ALL));
		framebtn_Article_blog.setOnClickListener(frameArticlesBtnClick(
				framebtn_Article_blog, BlogList.CATALOG_LATEST));
		framebtn_Article_recommend.setOnClickListener(frameArticlesBtnClick(
				framebtn_Article_recommend, BlogList.CATALOG_RECOMMEND));
		// 问答
		framebtn_Question_ask.setOnClickListener(frameQuestionBtnClick(
				framebtn_Question_ask, PostList.CATALOG_ASK));
		framebtn_Question_other.setOnClickListener(frameQuestionBtnClick(
				framebtn_Question_other, PostList.CATALOG_OTHER));
		
		
		// 社区
		framebtn_BBS_life.setOnClickListener(frameBBSBtnClick(
				framebtn_BBS_life, ThreadsList.CATALOG_LIFE));
		framebtn_BBS_admin.setOnClickListener(frameBBSBtnClick(
				framebtn_BBS_admin, ThreadsList.CATALOG_ADMIN));
		
		// 商店
		framebtn_Shop_promot.setOnClickListener(frameShopBtnClick(
				framebtn_Shop_promot, ShopList.SHOPTYPE_PROMOT));
		framebtn_Shop_shop.setOnClickListener(frameShopBtnClick(
				framebtn_Shop_shop, ShopList.SHOPTYPE_SHOP));
		framebtn_Shop_item.setOnClickListener(frameShopBtnClick(
				framebtn_Shop_item, ShopList.SHOPTYPE_ITEM));
		framebtn_Shop_comment.setOnClickListener(frameShopBtnClick(
				framebtn_Shop_comment, ShopList.SHOPTYPE_COMMENT));
		
		
	}

	private View.OnClickListener frameArticlesBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		};
	}

	private View.OnClickListener frameQuestionBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				if (btn == framebtn_Question_ask)
					framebtn_Question_ask.setEnabled(false);
				else
					framebtn_Question_ask.setEnabled(true);
				if (btn == framebtn_Question_other)
					framebtn_Question_other.setEnabled(false);
				else
					framebtn_Question_other.setEnabled(true);
				

				curQuestionCatalog = catalog;
				loadLvQuestionData(curQuestionCatalog, 0, lvQuestionHandler,
						UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
			}
		};
	}

	private View.OnClickListener frameBBSBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				if (btn == framebtn_BBS_life)
					framebtn_BBS_life.setEnabled(false);
				else
					framebtn_BBS_life.setEnabled(true);
				if (btn == framebtn_BBS_admin)
					framebtn_BBS_admin.setEnabled(false);
				else
					framebtn_BBS_admin.setEnabled(true);

				curBBSCatalog = catalog;
				loadLvBBSData(curBBSCatalog, 0, lvBBSHandler,
						UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
				
				
			}
		};
	}

	private View.OnClickListener frameShopBtnClick(final Button btn,
			final int catalog) {
		return new View.OnClickListener() {
			public void onClick(View v) {

				curShopCatalog = catalog;

				frameShopBtnOnClick(btn, catalog,
						UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
			}
		};
	}

	private void frameShopBtnOnClick(Button btn, int catalog, int action) {
		if (btn == framebtn_Shop_promot)
			framebtn_Shop_promot.setEnabled(false);
		else
			framebtn_Shop_promot.setEnabled(true);
		if (btn == framebtn_Shop_shop)
			framebtn_Shop_shop.setEnabled(false);
		else
			framebtn_Shop_shop.setEnabled(true);
		if (btn == framebtn_Shop_item)
			framebtn_Shop_item.setEnabled(false);
		else
			framebtn_Shop_item.setEnabled(true);
		if (btn == framebtn_Shop_comment)
			framebtn_Shop_comment.setEnabled(false);
		else
			framebtn_Shop_comment.setEnabled(true);
		
		curShopCatalog = catalog;
		
		if (btn == framebtn_Shop_promot ) {
			action = UIHelper.LISTVIEW_ACTION_REFRESH;
			lvShop_Promot.setVisibility(View.VISIBLE);
			lvShop_Shop.setVisibility(View.GONE);
			lvShop_Item.setVisibility(View.GONE);
			lvShop_ItemComment.setVisibility(View.GONE);
			
			loadLvShopPromotionData(curShopCatalog, 0, lvShopPromotionHandler,
					UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);

		} else if (btn == framebtn_Shop_shop) {
			action = UIHelper.LISTVIEW_ACTION_REFRESH;
			lvShop_Shop.setVisibility(View.VISIBLE);
			lvShop_Item.setVisibility(View.GONE);
			lvShop_ItemComment.setVisibility(View.GONE);
			lvShop_Promot.setVisibility(View.GONE);
			
			loadLvShopShopData(curShopCatalog, 0, lvShopShopHandler,
					UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
		} else if (btn == framebtn_Shop_item) {
			action = UIHelper.LISTVIEW_ACTION_REFRESH;
			lvShop_Promot.setVisibility(View.GONE);
			lvShop_Shop.setVisibility(View.GONE);
			lvShop_Item.setVisibility(View.VISIBLE);
			lvShop_ItemComment.setVisibility(View.GONE);
			
			loadLvShopItemData(curShopCatalog, 0, lvShopItemHandler,
					UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
		} else if (btn == framebtn_Shop_comment) {
			action = UIHelper.LISTVIEW_ACTION_REFRESH;
			lvShop_Promot.setVisibility(View.GONE);
			lvShop_Shop.setVisibility(View.GONE);
			lvShop_Item.setVisibility(View.GONE);
			lvShop_ItemComment.setVisibility(View.VISIBLE);
			
			loadLvShopItemCommentData(curShopCatalog, 0, lvShopItemCommentHandler,
					UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
		} 

		
	}

	/**
	 * 获取listview的初始化Handler
	 * 
	 * @param lv
	 * @param adapter
	 * @return
	 */
	private Handler getLvHandler(final PullToRefreshListView lv,
			final BaseAdapter adapter, final TextView more,
			final ProgressBar progress, final int pageSize) {
		return new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what >= 0) {
					// listview数据处理
					Notice notice = handleLvData(msg.what, msg.obj, msg.arg2,
							msg.arg1);

					if (msg.what < pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					} else if (msg.what == pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);

						// 特殊处理-热门动弹不能翻页
						if (lv == lvQuestion) {
							TweetList tlist = (TweetList) msg.obj;
							if (lvQuestionData.size() == tlist.getTweetCount()) {
								lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
								more.setText(R.string.load_full);
							}
						}
					}
					// 发送通知广播
					if (notice != null) {
						UIHelper.sendBroadCast(lv.getContext(), notice);
					}
					// 是否清除通知信息
					if (isClearNotice) {
						ClearNotice(curClearNoticeType);
						isClearNotice = false;// 重置
						curClearNoticeType = 0;
					}
				} else if (msg.what == -1) {
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					((AppException) msg.obj).makeToast(Main.this);
				}
				if (adapter.getCount() == 0) {
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				mHeadProgress.setVisibility(ProgressBar.GONE);
				if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) {
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update)
							+ new Date().toLocaleString());
					lv.setSelection(0);
				} else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
	}

	/**
	 * listview数据处理
	 * 
	 * @param what
	 *            数量
	 * @param obj
	 *            数据
	 * @param objtype
	 *            数据类型
	 * @param actiontype
	 *            操作类型
	 * @return notice 通知信息
	 */
	private Notice handleLvData(int what, Object obj, int objtype,
			int actiontype) {
		Notice notice = null;
		switch (actiontype) {
		case UIHelper.LISTVIEW_ACTION_INIT:
		case UIHelper.LISTVIEW_ACTION_REFRESH:
		case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			int newdata = 0;// 新加载数据-只有刷新动作才会使用到
			switch (objtype) {
			case UIHelper.LISTVIEW_DATATYPE_Article:
				ArticleList nlist = (ArticleList) obj;
				notice = nlist.getNotice();
				lvArticlesSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvArticlesData.size() > 0) {
						for (Article news1 : nlist.getArticlelist()) {
							boolean b = false;
							for (Article news2 : lvArticlesData) {
								if (news1.getId() == news2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvArticlesData.clear();// 先清除原有数据
				lvArticlesData.addAll(nlist.getArticlelist());
				break;
			
			case UIHelper.LISTVIEW_DATATYPE_BBS:
				ThreadsList plist = (ThreadsList) obj;
				notice = plist.getNotice();
				lvBBSSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvBBSData.size() > 0) {
						for (Threads post1 : plist.getThreadList()) {
							boolean b = false;
							for (Threads post2 : lvBBSData) {
								if (post1.getId() == post2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvBBSData.clear();// 先清除原有数据
				lvBBSData.addAll(plist.getThreadList());
				break;
			case UIHelper.LISTVIEW_DATATYPE_QUESTION:
				QuestionList qlist = (QuestionList) obj;
				notice = qlist.getNotice();
				lvQuestionSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvQuestionData.size() > 0) {
						for (Question question : qlist.getQuestionList()) {
							boolean b = false;
							for (Question tweet2 : lvQuestionData) {
								if (question.getId() == tweet2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvQuestionData.clear();// 先清除原有数据
				lvQuestionData.addAll(qlist.getQuestionList());
				break;
			case UIHelper.LISTVIEW_DATATYPE_SHOP_Promotion:
				PromotionList promotionlist = (PromotionList) obj;
				notice = promotionlist.getNotice();
				lvShopPromotionSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvShopPromotionData.size() > 0) {
						for (Promotion active1 : promotionlist.getPromotionList()) {
							boolean b = false;
							for (Promotion active2 : lvShopPromotionData) {
								if (active1.getItem() == active2.getItem()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvShopPromotionData.clear();// 先清除原有数据
				lvShopPromotionData.addAll(promotionlist.getPromotionList());
				break;
			case UIHelper.LISTVIEW_DATATYPE_SHOP_Shop:
				ShopList mlist = (ShopList) obj;
				notice = mlist.getNotice();
				lvShopShopSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvShopShopData.size() > 0) {
						for (Shop msg1 : mlist.getShopList()) {
							boolean b = false;
							for (Shop msg2 : lvShopShopData) {
								if (msg1.getId() == msg2.getId()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvShopShopData.clear();// 先清除原有数据
				lvShopShopData.addAll(mlist.getShopList());
				break;
		case UIHelper.LISTVIEW_DATATYPE_SHOP_Item:
			ItemList ilist = (ItemList) obj;
			notice = ilist.getNotice();
			lvShopItemSumData = what;
			if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
				if (lvShopItemData.size() > 0) {
					for (Item msg1 : ilist.getItemList()) {
						boolean b = false;
						for (Item msg2 : lvShopItemData) {
							if (msg1.getId() == msg2.getId()) {
								b = true;
								break;
							}
						}
						if (!b)
							newdata++;
					}
				} else {
					newdata = what;
				}
			}
			lvShopItemData.clear();// 先清除原有数据
			lvShopItemData.addAll(ilist.getItemList());
			break;
			case UIHelper.LISTVIEW_DATATYPE_SHOP_Comment:
				ItemCommentList clist = (ItemCommentList) obj;
				notice = clist.getNotice();
				lvShopItemCommentSumData = what;
				if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
					if (lvShopItemData.size() > 0) {
						for (ItemComment msg1 : clist.getCommentList()) {
							boolean b = false;
							for (ItemComment msg2 : lvShopItemCommentData) {
								if (msg1.getNickname() == msg2.getNickname()) {
									b = true;
									break;
								}
							}
							if (!b)
								newdata++;
						}
					} else {
						newdata = what;
					}
				}
				lvShopItemCommentData.clear();// 先清除原有数据
				lvShopItemCommentData.addAll(clist.getCommentList());
				break;
			}
			if (actiontype == UIHelper.LISTVIEW_ACTION_REFRESH) {
				// 提示新加载数据
				if (newdata > 0) {
					NewDataToast
							.makeText(
									this,
									getString(R.string.new_data_toast_message,
											newdata), appContext.isAppSound())
							.show();
				} else {
					NewDataToast.makeText(this,
							getString(R.string.new_data_toast_none), false)
							.show();
				}
			}
			break;
		case UIHelper.LISTVIEW_ACTION_SCROLL:
			switch (objtype) {
			case UIHelper.LISTVIEW_DATATYPE_Article:
				ArticleList list = (ArticleList) obj;
				notice = list.getNotice();
				lvArticlesSumData += what;
				if (lvArticlesData.size() > 0) {
					for (Article news1 : list.getArticlelist()) {
						boolean b = false;
						for (Article news2 : lvArticlesData) {
							if (news1.getId() == news2.getId()) {
								b = true;
								break;
							}
						}
						if (!b)
							lvArticlesData.add(news1);
					}
				} else {
					lvArticlesData.addAll(list.getArticlelist());
				}
				break;
			
			case UIHelper.LISTVIEW_DATATYPE_BBS:
				ThreadsList plist = (ThreadsList) obj;
				notice = plist.getNotice();
				lvBBSSumData += what;
				if (lvBBSData.size() > 0) {
					for (Threads post1 : plist.getThreadList()) {
						boolean b = false;
						for (Threads post2 : lvBBSData) {
							if (post1.getId() == post2.getId()) {
								b = true;
								break;
							}
						}
						if (!b)
							lvBBSData.add(post1);
					}
				} else {
					lvBBSData.addAll(plist.getThreadList());
				}
				break;
			case UIHelper.LISTVIEW_DATATYPE_QUESTION:
				QuestionList qlist = (QuestionList) obj;
				notice = qlist.getNotice();
				lvQuestionSumData += what;
				if (lvQuestionData.size() > 0) {
					for (Question question : qlist.getQuestionList()) {
						boolean b = false;
						for (Question tweet2 : lvQuestionData) {
							if (question.getId() == tweet2.getId()) {
								b = true;
								break;
							}
						}
						if (!b)
							lvQuestionData.add(question);
					}
				} else {
					lvQuestionData.addAll(qlist.getQuestionList());
				}
				break;
			case UIHelper.LISTVIEW_DATATYPE_SHOP_Promotion:
				PromotionList alist = (PromotionList) obj;
				notice = alist.getNotice();
				lvShopPromotionSumData += what;
				if (lvShopPromotionData.size() > 0) {
					for (Promotion active1 : alist.getPromotionList()) {
						boolean b = false;
						for (Promotion active2 : lvShopPromotionData) {
							if (active1.getItem() == active2.getItem()) {
								b = true;
								break;
							}
						}
						if (!b)
							lvShopPromotionData.add(active1);
					}
				} else {
					lvShopPromotionData.addAll(alist.getPromotionList());
				}
				break;
			case UIHelper.LISTVIEW_DATATYPE_SHOP_Shop:
				ShopList mlist = (ShopList) obj;
				notice = mlist.getNotice();
				lvShopShopSumData += what;
				if (lvShopShopData.size() > 0) {
					for (Shop msg1 : mlist.getShopList()) {
						boolean b = false;
						for (Shop msg2 : lvShopShopData) {
							if (msg1.getId() == msg2.getId()) {
								b = true;
								break;
							}
						}
						if (!b)
							lvShopShopData.add(msg1);
					}
				} else {
					lvShopShopData.addAll(mlist.getShopList());
				}
				break;
			}
			break;
		}
		return notice;
	}

	/**
	 * 线程加载首页新闻数据
	 * 
	 * @param catalog
	 *            分类
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 *            处理器
	 * @param action
	 *            动作标识
	 */
	private void loadLvArticlesData(final int catalog, final int pageIndex,
			final Handler handler, final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					
					ArticleList list = appContext.getArticleList(catalog, 1,
							isRefresh);
					msg.what = list.getArticleCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_Article;
				if (curArticleCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}
	

	/**
	 * 线程加载帖子数据
	 * 
	 * @param catalog
	 *            分类
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 *            处理器
	 * @param action
	 *            动作标识
	 */
	private void loadLvBBSData(final int catalog, final int pageIndex,
			final Handler handler, final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					ThreadsList list = appContext.getThreadsList(catalog, pageIndex,
							isRefresh);
					msg.what = list.getThreadCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_BBS;
				if (curBBSCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 线程加载问答数据
	 * 
	 * @param catalog
	 *            
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 *            处理器
	 * @param action
	 *            动作标识
	 */
	private void loadLvQuestionData(final int catalog, final int pageIndex,
			final Handler handler, final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					QuestionList list = appContext.getQuestionList(catalog,
							pageIndex, isRefresh);
					msg.what = list.getQuestionCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_QUESTION;
				if (curQuestionCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 茶友之家，店铺促销信息
	 * 线程加载动态数据
	 * 
	 * @param catalog
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 * @param action
	 */
	private void loadLvShopPromotionData(final int catalog, final int pageIndex,
			final Handler handler, final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					PromotionList list = appContext.getPromotionList(catalog,pageIndex, isRefresh);
					msg.what = list.getPromotionCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_SHOP_Promotion;
				if (curShopCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 线程加载商店数据
	 * 
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 * @param action
	 */
	private void loadLvShopShopData(final int catalog, final int pageIndex, final Handler handler,
			final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					
					ShopList list = appContext.getShopList(catalog,pageIndex,
							isRefresh);
					msg.what = list.getShopCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_SHOP_Shop;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 线程加载商品数据
	 * 
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 * @param action
	 */
	private void loadLvShopItemData(final int catalog, final int pageIndex, final Handler handler,
			final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					
					ItemList list = appContext.getItemList(catalog,pageIndex,
							isRefresh);
					msg.what = list.getItemCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_SHOP_Item;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 线程加载商店数据
	 * 
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 * @param action
	 */
	private void loadLvShopItemCommentData(final int catalog, final int pageIndex, final Handler handler,
			final int action) {
		mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					
					ItemCommentList list = appContext.getItemCommentList(catalog,pageIndex,
							isRefresh);
					msg.what = list.getCommentCount();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_SHOP_Comment;
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 轮询通知信息
	 */
	private void foreachUserNotice() {
		final String username = appContext.getLoginUserName();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					UIHelper.sendBroadCast(Main.this, (Notice) msg.obj);
				}
				foreachUserNotice();// 回调
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					sleep(60 * 1000);
					if (username != null && !"".equals(username)) {
						Notice notice = appContext.getTHUserNotice(username);
						msg.what = 1;
						msg.obj = notice;
					} else {
						msg.what = 0;
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 通知信息处理
	 * 
	 * @param type
	 *            1:@我的信息 2:未读消息 3:评论个数 4:新粉丝个数
	 */
	private void ClearNotice(final int type) {
		final String username = appContext.getLoginUserName();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1 && msg.obj != null) {
					Result res = (Result) msg.obj;
					if (res.OK() && res.getNotice() != null) {
						UIHelper.sendBroadCast(Main.this, res.getNotice());
					}
				} else {
					((AppException) msg.obj).makeToast(Main.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					Result res = appContext.thNoticeClear(username, type);
					msg.what = 1;
					msg.obj = res;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 创建menu TODO 停用原生菜单
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	/**
	 * 菜单被显示之前的事件
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		UIHelper.showMenuLoginOrLogout(this, menu);
		return true;
	}

	/**
	 * 处理menu的事件
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		int item_id = item.getItemId();
		switch (item_id) {
		case R.id.main_menu_user:
			UIHelper.loginOrLogout(this);
			break;
		case R.id.main_menu_about:
			UIHelper.showAbout(this);
			break;
		case R.id.main_menu_setting:
			UIHelper.showSetting(this);
			break;
		case R.id.main_menu_exit:
			UIHelper.Exit(this);
			break;
		}
		return true;
	}

	/**
	 * 监听返回--是否退出程序
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 是否退出应用
			return mDoubleClickExitHelper.onKeyDown(keyCode, event);
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// 展示快捷栏&判断是否登录
			UIHelper.showSettingLoginOrLogout(Main.this,
					mGrid.getQuickAction(0));
			mGrid.show(fbHome, true);
		} else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			// 展示搜索页
			UIHelper.showSearch(Main.this);
		} else {
			flag = super.onKeyDown(keyCode, event);
		}
		return flag;
	}
}
