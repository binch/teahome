package com.haorenao.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import com.haorenao.app.api.ApiClient;
import com.haorenao.app.api.THApiClient;
import com.haorenao.app.bean.ActiveList;
import com.haorenao.app.bean.Barcode;
import com.haorenao.app.bean.Blog;
import com.haorenao.app.bean.BlogCommentList;
import com.haorenao.app.bean.BlogList;
import com.haorenao.app.bean.CommentList;
import com.haorenao.app.bean.FavoriteList;
import com.haorenao.app.bean.FriendList;
import com.haorenao.app.bean.MessageList;
import com.haorenao.app.bean.MyInformation;
import com.haorenao.app.bean.News;
import com.haorenao.app.bean.NewsList;
import com.haorenao.app.bean.Notice;
import com.haorenao.app.bean.Post;
import com.haorenao.app.bean.PostList;
import com.haorenao.app.bean.Report;
import com.haorenao.app.bean.Result;
import com.haorenao.app.bean.SearchList;
import com.haorenao.app.bean.Software;
import com.haorenao.app.bean.SoftwareCatalogList;
import com.haorenao.app.bean.SoftwareList;
import com.haorenao.app.bean.Tweet;
import com.haorenao.app.bean.TweetList;
import com.haorenao.app.bean.UserInformation;
import com.haorenao.app.bean.th.ArticleList;
import com.haorenao.app.bean.th.ItemCommentList;
import com.haorenao.app.bean.th.ItemList;
import com.haorenao.app.bean.th.PostResult;
import com.haorenao.app.bean.th.PromotionList;
import com.haorenao.app.bean.th.Question;
import com.haorenao.app.bean.th.QuestionList;
import com.haorenao.app.bean.th.ShopList;
import com.haorenao.app.bean.th.THUserInfo;
import com.haorenao.app.bean.th.Threads;
import com.haorenao.app.bean.th.ThreadsList;
import com.haorenao.app.common.CyptoUtils;
import com.haorenao.app.common.FileUtils;
import com.haorenao.app.common.ImageUtils;
import com.haorenao.app.common.MethodsCompat;
import com.haorenao.app.common.StringUtils;
import com.haorenao.app.common.UIHelper;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
//import android.webkit.CacheManager;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends Application {
	
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	
	public static final int PAGE_SIZE = 20;//默认分页大小
	private static final int CACHE_TIME = 60*60000;//缓存失效时间
	
	private boolean login = false;	//登录状态
	private String username = "";	//登录用户的id
	private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	
	private String saveImagePath;//保存图片路径
	
	private Handler unLoginHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				UIHelper.ToastMessage(AppContext.this, getString(R.string.msg_login_error));
				UIHelper.showLoginDialog(AppContext.this);
			}
		}		
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
        //注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        
        init();
	}

	/**
	 * 初始化
	 */
	private void init(){
		//设置保存图片的路径
		saveImagePath = getProperty(AppConfig.SAVE_IMAGE_PATH);
		if(StringUtils.isEmpty(saveImagePath)){
			setProperty(AppConfig.SAVE_IMAGE_PATH, AppConfig.DEFAULT_SAVE_IMAGE_PATH);
			saveImagePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
		}
	}
	
	/**
	 * 检测当前系统声音是否为正常模式
	 * @return
	 */
	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE); 
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}
	
	/**
	 * 应用程序是否发出提示音
	 * @return
	 */
	public boolean isAppSound() {
		return isAudioNormal() && isVoice();
	}
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
	/**
	 * 获取App唯一标识
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if(StringUtils.isEmpty(uniqueID)){
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}
	
	/**
	 * 用户是否登录
	 * @return
	 */
	public boolean isLogin() {
		return login;
	}
	
	/**
	 * 获取登录用户名
	 * @return
	 */
	public String getLoginUserName() {
		return this.username;
	}
	
	/**
	 * 用户注销
	 */
	public void Logout() {
		ApiClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.username = "";
	}
	
	/**
	 * 未登录或修改密码后的处理
	 */
	public Handler getUnLoginHandler() {
		return this.unLoginHandler;
	}
	
	/**
	 * 初始化用户登录信息
	 */
	public void initLoginInfo() {
		THUserInfo loginUser = getLoginInfo();
		if(loginUser!=null && !loginUser.getUsername().equals("")){
			this.username = loginUser.getUsername();
			this.login = true;
		}else{
			this.Logout();
		}
	}
	
	/**
	 * 用户登录验证
	 * @param account
	 * @param pwd
	 * @return
	 * @throws AppException
	 */
	public THUserInfo loginVerify(String account, String pwd, int deviceid) throws AppException {
		return THApiClient.login(this, account, pwd,deviceid);
	}
	
	/**
	 * 我的个人资料
	 * @param isRefresh 是否主动刷新
	 * @return
	 * @throws AppException
	 */
	public THUserInfo getMyInformation(boolean isRefresh) throws AppException {
		THUserInfo myinfo = null;
		String key = "userinfoinfo_"+username;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				myinfo = THApiClient.getUserInfo(this, username);
				if(myinfo != null && myinfo.getUsername().length() > 0){
					Notice notice = myinfo.getNotice();
					myinfo.setNotice(null);
					myinfo.setCacheKey(key);
					saveObject(myinfo, key);
					myinfo.setNotice(notice);
				}
			}catch(AppException e){
				myinfo = (THUserInfo)readObject(key);
				if(myinfo == null)
					throw e;
			}
		} else {
			myinfo = (THUserInfo)readObject(key);
			if(myinfo == null)
				myinfo = new THUserInfo();
		}
		return myinfo;
	}	
	
	/**
	 * 获取用户信息个人专页（包含该用户的动态信息以及个人信息）
	 * @param uid 自己的uid
	 * @param hisuid 被查看用户的uid
	 * @param hisname 被查看用户的用户名
	 * @param pageIndex 页面索引
	 * @return
	 * @throws AppException
	 */
	public UserInformation getInformation(int uid, int hisuid, String hisname, int pageIndex, boolean isRefresh) throws AppException {
		String _hisname = ""; 
		if(!StringUtils.isEmpty(hisname)){
			_hisname = hisname;
		}
		UserInformation userinfo = null;
		String key = "userinfo_"+uid+"_"+hisuid+"_"+(URLEncoder.encode(hisname))+"_"+pageIndex+"_"+PAGE_SIZE; 
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {			
			try{
				userinfo = ApiClient.information(this, uid, hisuid, _hisname, pageIndex, PAGE_SIZE);
				if(userinfo != null && pageIndex == 0){
					Notice notice = userinfo.getNotice();
					userinfo.setNotice(null);
					userinfo.setCacheKey(key);
					saveObject(userinfo, key);
					userinfo.setNotice(notice);
				}
			}catch(AppException e){
				userinfo = (UserInformation)readObject(key);
				if(userinfo == null)
					throw e;
			}
		} else {
			userinfo = (UserInformation)readObject(key);
			if(userinfo == null)
				userinfo = new UserInformation();
		}
		return userinfo;
	}
	
	/**
	 * 更新用户之间关系（加关注、取消关注）
	 * @param uid 自己的uid
	 * @param hisuid 对方用户的uid
	 * @param newrelation 0:取消对他的关注 1:关注他
	 * @return
	 * @throws AppException
	 */
	public Result updateRelation(int uid, int hisuid, int newrelation) throws AppException {
		return ApiClient.updateRelation(this, uid, hisuid, newrelation);
	}
	
	/**
	 * 更新用户头像
	 * @param portrait 新上传的头像
	 * @return
	 * @throws AppException
	 */
	public Result updatePortrait(File portrait) throws AppException {
		return ApiClient.updatePortrait(this, 0, portrait);
	}
	
	/**
	 * 清空通知消息
	 * @param uid
	 * @param type 1:@我的信息 2:未读消息 3:评论个数 4:新粉丝个数
	 * @return
	 * @throws AppException
	 */
	public Result noticeClear(int uid, int type) throws AppException {
		return ApiClient.noticeClear(this, uid, type);
	}
	
	/**
	 * 清空通知消息
	 * @param uid
	 * @param type 1:@我的信息 2:未读消息 3:评论个数 4:新粉丝个数
	 * @return
	 * @throws AppException
	 */
	public Result thNoticeClear(String username, int type) throws AppException {
		return ApiClient.noticeClear(this, 0, type);
	}
	
	/**
	 * 获取用户通知信息
	 * @param uid
	 * @return
	 * @throws AppException
	 */
	public Notice getUserNotice(int uid) throws AppException {
		return ApiClient.getUserNotice(this, uid);
	}
	
	/**
	 * 获取用户通知信息
	 * @param uid
	 * @return
	 * @throws AppException
	 */
	public Notice getTHUserNotice(String username) throws AppException {
		return ApiClient.getUserNotice(this, 0);
	}
	
	/**
	 * 用户收藏列表
	 * @param type 0:全部收藏 1:软件 2:话题 3:博客 4:新闻 5:代码
	 * @param pageIndex 页面索引 0表示第一页
	 * @return
	 * @throws AppException
	 */
	public FavoriteList getFavoriteList(int type, int pageIndex, boolean isRefresh) throws AppException {
		FavoriteList list = null;
		String key = "favoritelist_"+username+"_"+type+"_"+pageIndex+"_"+PAGE_SIZE; 
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getFavoriteList(this, 0, type, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (FavoriteList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (FavoriteList)readObject(key);
			if(list == null)
				list = new FavoriteList();
		}
		return list;
	}
	
	/**
	 * 用户粉丝、关注人列表
	 * @param relation 0:显示自己的粉丝 1:显示自己的关注者
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public FriendList getFriendList(int relation, int pageIndex, boolean isRefresh) throws AppException {
		FriendList list = null;
		String key = "friendlist_"+username+"_"+relation+"_"+pageIndex+"_"+PAGE_SIZE; 
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getFriendList(this, 0, relation, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (FriendList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (FriendList)readObject(key);
			if(list == null)
				list = new FriendList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 首页新闻列表
	 * @param catalog
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ApiException
	 */
	public ArticleList getArticleList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		ArticleList list = null;
		String key = "articleslist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = THApiClient.getArticalList(this,pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (ArticleList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (ArticleList)readObject(key);
			if(list == null)
				list = new ArticleList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 获取社区帖子列表
	 * @param catalog
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public ThreadsList getThreadsList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		ThreadsList list = null;
		if(pageIndex ==0){
			pageIndex=1;
		}
		String key = "bbsthreadlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				
				list = THApiClient.getThreadsList(this,catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (ThreadsList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (ThreadsList)readObject(key);
			if(list == null)
				list = new ThreadsList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 获取问答列表
	 * @param catalog
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public QuestionList getQuestionList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		QuestionList list = null;
		if(pageIndex ==0){
			pageIndex=1;
		}
		String key = "questionlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				
				list = THApiClient.getQuestionList(this,catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (QuestionList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (QuestionList)readObject(key);
			if(list == null)
				list = new QuestionList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 促销信息列表
	 * @param catalog
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public PromotionList getPromotionList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		PromotionList list = null;
		if(pageIndex ==0){
			pageIndex=1;
		}
		String key = "promotionlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				
				list = THApiClient.getPromotionList(this,catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (PromotionList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (PromotionList)readObject(key);
			if(list == null)
				list = new PromotionList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 获取商店列表
	 * @param catalog
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public ShopList getShopList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		ShopList list = null;
		if(pageIndex ==0){
			pageIndex=1;
		}
		String key = "shoplist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				
				list = THApiClient.getShopList(this,catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (ShopList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (ShopList)readObject(key);
			if(list == null)
				list = new ShopList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 获取商品列表
	 * @param catalog
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public ItemList getItemList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		ItemList list = null;
		if(pageIndex ==0){
			pageIndex=1;
		}
		String key = "itemlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				
				list = THApiClient.getItemList(this,catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (ItemList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (ItemList)readObject(key);
			if(list == null)
				list = new ItemList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 获取所有商品评价列表
	 * @param catalog
	 * @param pageIndex
	 * @param isRefresh
	 * @return
	 * @throws AppException
	 */
	public ItemCommentList getItemCommentList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		ItemCommentList list = null;
		if(pageIndex ==0){
			pageIndex=1;
		}
		String key = "itemcommentlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				
				list = THApiClient.getItemCommentList(this,catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					list.setCacheKey(key);
					saveObject(list, key);
				}
			}catch(AppException e){
				list = (ItemCommentList)readObject(key);
				if(list == null)
					throw e;
			}		
		} else {
			list = (ItemCommentList)readObject(key);
			if(list == null)
				list = new ItemCommentList();
		}
		return list;
	}
	
	/**
	 * 茶友之家
	 * 获取帖子详情详情
	 * @param thread_id
	 * @return
	 * @throws AppException
	 */
	public Threads getBBSDetail(int thread_id, boolean isRefresh) throws AppException {
		Threads bbs = null;
		String key = "thread_detail_"+thread_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				bbs = THApiClient.getThreadsDetail(this, thread_id);
				if(bbs != null){
					Notice notice = bbs.getNotice();
					bbs.setNotice(null);
					bbs.setCacheKey(key);
					saveObject(bbs, key);
					bbs.setNotice(notice);
				}
			}catch(AppException e){
				bbs = (Threads)readObject(key);
				if(bbs == null)
					throw e;
			}
		} else {
			bbs = (Threads)readObject(key);
			if(bbs == null)
				bbs = new Threads();
		}
		return bbs;
	}
	
	/**
	 * 茶友之家
	 * 获取问答详情详情
	 * @param question_id
	 * @return
	 * @throws AppException
	 */
	public Question getTHQuestionDetail(int question_id, boolean isRefresh) throws AppException {
		Question question = null;
		String key = "question_detail_"+question_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				question = THApiClient.getQuestionDetail(this, question_id);
				if(question != null){
					Notice notice = question.getNotice();
					question.setNotice(null);
					question.setCacheKey(key);
					saveObject(question, key);
					question.setNotice(notice);
				}
			}catch(AppException e){
				question = (Question)readObject(key);
				if(question == null)
					throw e;
			}
		} else {
			question = (Question)readObject(key);
			if(question == null)
				question = new Question();
		}
		return question;
	}
	
	/**
	 * 用户博客列表
	 * @param authoruid
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public BlogList getUserBlogList(int authoruid, String authorname, int pageIndex, boolean isRefresh) throws AppException {
		BlogList list = null;
		String key = "userbloglist_"+authoruid+"_"+(URLEncoder.encode(authorname))+"_"+username+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getUserBlogList(this, authoruid, authorname, 0, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (BlogList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (BlogList)readObject(key);
			if(list == null)
				list = new BlogList();
		}
		return list;
	}
	
	
	/**
	 * 软件列表
	 * @param searchTag 软件分类  推荐:recommend 最新:time 热门:view 国产:list_cn
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public SoftwareList getSoftwareList(String searchTag, int pageIndex, boolean isRefresh) throws AppException {
		SoftwareList list = null;
		String key = "softwarelist_"+searchTag+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getSoftwareList(this, searchTag, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (SoftwareList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (SoftwareList)readObject(key);
			if(list == null)
				list = new SoftwareList();
		}
		return list;
	}
	
	/**
	 * 软件分类的软件列表
	 * @param searchTag 从softwarecatalog_list获取的tag
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public SoftwareList getSoftwareTagList(int searchTag, int pageIndex, boolean isRefresh) throws AppException {
		SoftwareList list = null;
		String key = "softwaretaglist_"+searchTag+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (isCacheDataFailure(key) || isRefresh)) {
			try{
				list = ApiClient.getSoftwareTagList(this, searchTag, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (SoftwareList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (SoftwareList)readObject(key);
			if(list == null)
				list = new SoftwareList();
		}
		return list;
	}
	
	/**
	 * 软件分类列表
	 * @param tag 第一级:0  第二级:tag
	 * @return
	 * @throws AppException
	 */
	public SoftwareCatalogList getSoftwareCatalogList(int tag) throws AppException {
		SoftwareCatalogList list = null;
		String key = "softwarecataloglist_"+tag;
		if(isNetworkConnected() && isCacheDataFailure(key)) {
			try{
				list = ApiClient.getSoftwareCatalogList(this, tag);
				if(list != null){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (SoftwareCatalogList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (SoftwareCatalogList)readObject(key);
			if(list == null)
				list = new SoftwareCatalogList();
		}
		return list;
	}
	
	/**
	 * 软件详情
	 * @param soft_id
	 * @return
	 * @throws AppException
	 */
	public Software getSoftware(String ident, boolean isRefresh) throws AppException {
		Software soft = null;
		String key = "software_"+(URLEncoder.encode(ident));
		if(isNetworkConnected() && (isCacheDataFailure(key) || isRefresh)) {
			try{
				soft = ApiClient.getSoftwareDetail(this, ident);
				if(soft != null){
					Notice notice = soft.getNotice();
					soft.setNotice(null);
					soft.setCacheKey(key);
					saveObject(soft, key);
					soft.setNotice(notice);
				}
			}catch(AppException e){
				soft = (Software)readObject(key);
				if(soft == null)
					throw e;
			}
		} else {
			soft = (Software)readObject(key);
			if(soft == null)
				soft = new Software();
		}
		return soft;
	}
	
	/**
	 * 帖子列表
	 * @param catalog
	 * @param pageIndex
	 * @return
	 * @throws ApiException
	 */
	public PostList getPostList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		PostList list = null;
		String key = "postlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {		
			try{
				list = ApiClient.getPostList(this, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (PostList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (PostList)readObject(key);
			if(list == null)
				list = new PostList();
		}
		return list;
	}
	
	/**
	 * Tag相关帖子列表
	 * @param tag
	 * @param pageIndex
	 * @return
	 * @throws ApiException
	 */
	public PostList getPostListByTag(String tag, int pageIndex, boolean isRefresh) throws AppException {
		PostList list = null;
		String key = "postlist_"+(URLEncoder.encode(tag))+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {		
			try{
				list = ApiClient.getPostListByTag(this, tag, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (PostList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (PostList)readObject(key);
			if(list == null)
				list = new PostList();
		}
		return list;
	}
	
	/**
	 * 读取帖子详情
	 * @param post_id
	 * @return
	 * @throws ApiException
	 */
	public Post getPost(int post_id, boolean isRefresh) throws AppException {		
		Post post = null;
		String key = "post_"+post_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {	
			try{
				post = ApiClient.getPostDetail(this, post_id);
				if(post != null){
					Notice notice = post.getNotice();
					post.setNotice(null);
					post.setCacheKey(key);
					saveObject(post, key);
					post.setNotice(notice);
				}
			}catch(AppException e){
				post = (Post)readObject(key);
				if(post == null)
					throw e;
			}
		} else {
			post = (Post)readObject(key);
			if(post == null)
				post = new Post();
		}
		return post;		
	}
	
	/**
	 * 动弹列表
	 * @param catalog -1 热门，0 最新，大于0 某用户的动弹(uid)
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public TweetList getTweetList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		TweetList list = null;
		String key = "tweetlist_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;		
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getTweetList(this, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (TweetList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (TweetList)readObject(key);
			if(list == null)
				list = new TweetList();
		}
		return list;
	}
	
	/**
	 * 获取动弹详情
	 * @param tweet_id
	 * @return
	 * @throws AppException
	 */
	public Tweet getTweet(int tweet_id, boolean isRefresh) throws AppException {
		Tweet tweet = null;
		String key = "tweet_"+tweet_id;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				tweet = ApiClient.getTweetDetail(this, tweet_id);
				if(tweet != null){
					Notice notice = tweet.getNotice();
					tweet.setNotice(null);
					tweet.setCacheKey(key);
					saveObject(tweet, key);
					tweet.setNotice(notice);
				}
			}catch(AppException e){
				tweet = (Tweet)readObject(key);
				if(tweet == null)
					throw e;
			}
		} else {
			tweet = (Tweet)readObject(key);
			if(tweet == null)
				tweet = new Tweet();
		}
		return tweet;
	}
	
	/**
	 * 动态列表
	 * @param catalog 1最新动态 2@我 3评论 4我自己
	 * @param id
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public ActiveList getActiveList(int catalog, int pageIndex, boolean isRefresh) throws AppException {
		ActiveList list = null;
		String key = "activelist_"+username+"_"+catalog+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getActiveList(this, 0, catalog, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (ActiveList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (ActiveList)readObject(key);
			if(list == null)
				list = new ActiveList();
		}
		return list;
	}
	
	/**
	 * 留言列表
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public MessageList getMessageList(int pageIndex, boolean isRefresh) throws AppException {
		MessageList list = null;
		String key = "messagelist_"+username+"_"+pageIndex+"_"+PAGE_SIZE;
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getMessageList(this, 0, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (MessageList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (MessageList)readObject(key);
			if(list == null)
				list = new MessageList();
		}
		return list;
	}
	
	/**
	 * 博客评论列表
	 * @param id 博客Id
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public BlogCommentList getBlogCommentList(int id, int pageIndex, boolean isRefresh) throws AppException {
		BlogCommentList list = null;
		String key = "blogcommentlist_"+id+"_"+pageIndex+"_"+PAGE_SIZE;		
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getBlogCommentList(this, id, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (BlogCommentList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (BlogCommentList)readObject(key);
			if(list == null)
				list = new BlogCommentList();
		}
		return list;
	}
	
	/**
	 * 评论列表
	 * @param catalog 1新闻 2帖子 3动弹 4动态
	 * @param id 某条新闻，帖子，动弹的id 或者某条留言的friendid
	 * @param pageIndex
	 * @return
	 * @throws AppException
	 */
	public CommentList getCommentList(int catalog, int id, int pageIndex, boolean isRefresh) throws AppException {
		CommentList list = null;
		String key = "commentlist_"+catalog+"_"+id+"_"+pageIndex+"_"+PAGE_SIZE;		
		if(isNetworkConnected() && (!isReadDataCache(key) || isRefresh)) {
			try{
				list = ApiClient.getCommentList(this, catalog, id, pageIndex, PAGE_SIZE);
				if(list != null && pageIndex == 0){
					Notice notice = list.getNotice();
					list.setNotice(null);
					list.setCacheKey(key);
					saveObject(list, key);
					list.setNotice(notice);
				}
			}catch(AppException e){
				list = (CommentList)readObject(key);
				if(list == null)
					throw e;
			}
		} else {
			list = (CommentList)readObject(key);
			if(list == null)
				list = new CommentList();
		}
		return list;
	}
	
	/**
	 * 获取搜索列表
	 * @param catalog 全部:all 新闻:news  问答:post 软件:software 博客:blog 代码:code
	 * @param content 搜索的内容
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	public SearchList getSearchList(String catalog, String content, int pageIndex, int pageSize) throws AppException {
		return ApiClient.getSearchList(this, catalog, content, pageIndex, pageSize);
	}
	
	/**
	 * 发帖子
	 * @param post （uid、title、catalog、content、isNoticeMe）
	 * @return
	 * @throws AppException
	 */
	public Result pubPost(Post post) throws AppException {
		return ApiClient.pubPost(this, post);
	}
	
	/**
	 * 发动弹
	 * @param Tweet-uid & msg & image
	 * @return
	 * @throws AppException
	 */
	public Result pubTweet(Tweet tweet) throws AppException {
		return ApiClient.pubTweet(this, tweet);
	}
	
	/**
	 * 茶友之家
	 * 发帖子
	 * @param 
	 * @return
	 * @throws AppException
	 */
	public PostResult pubBBS(int catalog,Threads bbs) throws AppException {
		return THApiClient.pubBBS(this, catalog, bbs);
	}
	
	/**
	 * 删除动弹
	 * @param uid
	 * @param tweetid
	 * @return
	 * @throws AppException
	 */
	public Result delTweet(int uid, int tweetid) throws AppException {
		return ApiClient.delTweet(this, uid, tweetid);
	}
	
	/**
	 * 发送留言
	 * @param uid 登录用户uid
	 * @param receiver 接受者的用户id
	 * @param content 消息内容，注意不能超过250个字符
	 * @return
	 * @throws AppException
	 */
	public Result pubMessage(int uid, int receiver, String content) throws AppException {
		return ApiClient.pubMessage(this, uid, receiver, content);
	}
	
	/**
	 * 转发留言
	 * @param uid 登录用户uid
	 * @param receiver 接受者的用户名
	 * @param content 消息内容，注意不能超过250个字符
	 * @return
	 * @throws AppException
	 */
	public Result forwardMessage(int uid, String receiver, String content) throws AppException {
		return ApiClient.forwardMessage(this, uid, receiver, content);
	}
	
	/**
	 * 删除留言
	 * @param uid 登录用户uid
	 * @param friendid 留言者id
	 * @return
	 * @throws AppException
	 */
	public Result delMessage(int uid, int friendid) throws AppException {
		return ApiClient.delMessage(this, uid, friendid);
	}
	
	/**
	 * 发表评论
	 * @param catalog 1新闻  2帖子  3动弹  4动态
	 * @param id 某条新闻，帖子，动弹的id
	 * @param uid 用户uid
	 * @param content 发表评论的内容
	 * @param isPostToMyZone 是否转发到我的空间  0不转发  1转发
	 * @return
	 * @throws AppException
	 */
	public Result pubComment(int catalog, int id, int uid, String content, int isPostToMyZone) throws AppException {
		return ApiClient.pubComment(this, catalog, id, uid, content, isPostToMyZone);
	}
	
	/**
	 * 
	 * @param id 表示被评论的某条新闻，帖子，动弹的id 或者某条消息的 friendid 
	 * @param catalog 表示该评论所属什么类型：1新闻  2帖子  3动弹  4动态
	 * @param replyid 表示被回复的单个评论id
	 * @param authorid 表示该评论的原始作者id
	 * @param uid 用户uid 一般都是当前登录用户uid
	 * @param content 发表评论的内容
	 * @return
	 * @throws AppException
	 */
	public Result replyComment(int id, int catalog, int replyid, int authorid, int uid, String content) throws AppException {
		return ApiClient.replyComment(this, id, catalog, replyid, authorid, uid, content);
	}
	
	/**
	 * 删除评论
	 * @param id 表示被评论对应的某条新闻,帖子,动弹的id 或者某条消息的 friendid
	 * @param catalog 表示该评论所属什么类型：1新闻  2帖子  3动弹  4动态&留言
	 * @param replyid 表示被回复的单个评论id
	 * @param authorid 表示该评论的原始作者id
	 * @return
	 * @throws AppException
	 */
	public Result delComment(int id, int catalog, int replyid, int authorid) throws AppException {
		return ApiClient.delComment(this, id, catalog, replyid, authorid);
	}
	
	/**
	 * 发表博客评论
	 * @param blog 博客id
	 * @param uid 登陆用户的uid
	 * @param content 评论内容
	 * @return
	 * @throws AppException
	 */
	public Result pubBlogComment(int blog, int uid, String content) throws AppException {
		return ApiClient.pubBlogComment(this, blog, uid, content);
	}
	
	/**
	 * 发表博客评论
	 * @param blog 博客id
	 * @param uid 登陆用户的uid
	 * @param content 评论内容
	 * @param reply_id 评论id
	 * @param objuid 被评论的评论发表者的uid
	 * @return
	 * @throws AppException
	 */
	public Result replyBlogComment(int blog, int uid, String content, int reply_id, int objuid) throws AppException {
		return ApiClient.replyBlogComment(this, blog, uid, content, reply_id, objuid);
	}
	
	/**
	 * 删除博客评论
	 * @param uid 登录用户的uid
	 * @param blogid 博客id
	 * @param replyid 评论id
	 * @param authorid 评论发表者的uid
	 * @param owneruid 博客作者uid
	 * @return
	 * @throws AppException
	 */
	public Result delBlogComment(int uid, int blogid, int replyid, int authorid, int owneruid) throws AppException {
		return ApiClient.delBlogComment(this, uid, blogid, replyid, authorid, owneruid);
	}
	
	/**
	 * 删除博客
	 * @param uid 登录用户的uid
	 * @param authoruid 博客作者uid
	 * @param id 博客id
	 * @return
	 * @throws AppException
	 */
	public Result delBlog(int uid, int authoruid, int id) throws AppException { 	
		return ApiClient.delBlog(this, uid, authoruid, id);
	}
	
	/**
	 * 举报讨论区帖子
	 * @param report
	 * @return
	 * @throws AppException
	 */
	public String report(Report report) throws AppException {
		return ApiClient.report(this, report);
	}
	
	/**
	 * 扫描二维码签到
	 * @param barcode
	 * @return
	 * @throws AppException
	 */
	public String signIn(Barcode barcode) throws AppException{
		return ApiClient.signIn(this, barcode);
	}
	
	/**
	 * 用户添加收藏
	 * @param uid 用户UID
	 * @param objid 比如是新闻ID 或者问答ID 或者动弹ID
	 * @param type 1:软件 2:话题 3:博客 4:新闻 5:代码
	 * @return
	 * @throws AppException
	 */
	public Result addFavorite(int uid, int objid, int type) throws AppException {
		return ApiClient.addFavorite(this, uid, objid, type);
	}
	
	/**
	 * 用户删除收藏
	 * @param uid 用户UID
	 * @param objid 比如是新闻ID 或者问答ID 或者动弹ID
	 * @param type 1:软件 2:话题 3:博客 4:新闻 5:代码
	 * @return
	 * @throws AppException
	 */
	public Result delFavorite(int uid, int objid, int type) throws AppException { 	
		return ApiClient.delFavorite(this, uid, objid, type);
	}
	
	/**
	 * 保存登录信息
	 * @param username
	 * @param pwd
	 */
	public void saveLoginInfo(final THUserInfo user) {
		this.username = user.getUsername();
		this.login = true;
		setProperties(new Properties(){{
			setProperty("userinfo.username", user.getUsername());
			setProperty("userinfo.desc", user.getDesc());
			setProperty("userinfo.grade", user.getGrade());
			setProperty("userinfo.nickname", user.getNickname());
			setProperty("userinfo.passwd", user.getPasswd());
			setProperty("userinfo.point", user.getPoint());
			setProperty("userinfo.thumb", user.getThumb());
			
		}});		
	}
	
	/**
	 * 清除登录信息
	 */
	public void cleanLoginInfo() {
		this.username = "";
		this.login = false;
		removeProperty("userinfo.desc","userinfo.grade","userinfo.nickname",
				"userinfo.passwd","userinfo.username","userinfo.thumb","userinfo.point");
	}
	
	public int getLoginUid() {
		return 0;
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public THUserInfo getLoginInfo() {		
		THUserInfo u = new THUserInfo();
		u.setDesc(getProperty("userinfo.desc"));
		u.setGrade(getProperty("userinfo.grade"));
		u.setNickname(getProperty("userinfo.nickname"));
		u.setPasswd(getProperty("userinfo.passwd"));
		u.setUsername(getProperty("userinfo.username"));
		u.setPoint(getProperty("userinfo.point"));
		u.setPoint(getProperty("userinfo.thumb"));
		
		return u;
	}
	
	/**
	 * 保存用户头像
	 * @param fileName
	 * @param bitmap
	 */
	public void saveUserFace(String fileName,Bitmap bitmap) {
		try {
			ImageUtils.saveImage(this, fileName, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取用户头像
	 * @param key
	 * @return
	 * @throws AppException
	 */
	public Bitmap getUserFace(String key) throws AppException {
		FileInputStream fis = null;
		try{
			fis = openFileInput(key);
			return BitmapFactory.decodeStream(fis);
		}catch(Exception e){
			throw AppException.run(e);
		}finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 是否加载显示文章图片
	 * @return
	 */
	public boolean isLoadImage()
	{
		String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
		//默认是加载的
		if(StringUtils.isEmpty(perf_loadimage))
			return true;
		else
			return StringUtils.toBool(perf_loadimage);
	}
	
	/**
	 * 设置是否加载文章图片
	 * @param b
	 */
	public void setConfigLoadimage(boolean b)
	{
		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}
	
	/**
	 * 是否发出提示音
	 * @return
	 */
	public boolean isVoice()
	{
		String perf_voice = getProperty(AppConfig.CONF_VOICE);
		//默认是开启提示声音
		if(StringUtils.isEmpty(perf_voice))
			return true;
		else
			return StringUtils.toBool(perf_voice);
	}
	
	/**
	 * 设置是否发出提示音
	 * @param b
	 */
	public void setConfigVoice(boolean b)
	{
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
	}
	
	/**
	 * 是否启动检查更新
	 * @return
	 */
	public boolean isCheckUp()
	{
		String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);
		//默认是开启
		if(StringUtils.isEmpty(perf_checkup))
			return true;
		else
			return StringUtils.toBool(perf_checkup);
	}
	
	/**
	 * 设置启动检查更新
	 * @param b
	 */
	public void setConfigCheckUp(boolean b)
	{
		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
	}
	
	/**
	 * 是否左右滑动
	 * @return
	 */
	public boolean isScroll()
	{
		String perf_scroll = getProperty(AppConfig.CONF_SCROLL);
		//默认是关闭左右滑动
		if(StringUtils.isEmpty(perf_scroll))
			return false;
		else
			return StringUtils.toBool(perf_scroll);
	}
	
	/**
	 * 设置是否左右滑动
	 * @param b
	 */
	public void setConfigScroll(boolean b)
	{
		setProperty(AppConfig.CONF_SCROLL, String.valueOf(b));
	}
	
	/**
	 * 是否Https登录
	 * @return
	 */
	public boolean isHttpsLogin()
	{
		String perf_httpslogin = getProperty(AppConfig.CONF_HTTPS_LOGIN);
		//默认是http
		if(StringUtils.isEmpty(perf_httpslogin))
			return false;
		else
			return StringUtils.toBool(perf_httpslogin);
	}
	
	/**
	 * 设置是是否Https登录
	 * @param b
	 */
	public void setConfigHttpsLogin(boolean b)
	{
		setProperty(AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
	}
	
	/**
	 * 清除保存的缓存
	 */
	public void cleanCookie()
	{
		removeProperty(AppConfig.CONF_COOKIE);
	}
	
	/**
	 * 判断缓存数据是否可读
	 * @param cachefile
	 * @return
	 */
	private boolean isReadDataCache(String cachefile)
	{
		return readObject(cachefile) != null;
	}
	
	/**
	 * 判断缓存是否存在
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile)
	{
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if(data.exists())
			exist = true;
		return exist;
	}
	
	/**
	 * 判断缓存是否失效
	 * @param cachefile
	 * @return
	 */
	public boolean isCacheDataFailure(String cachefile)
	{
		boolean failure = false;
		File data = getFileStreamPath(cachefile);
		if(data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if(!data.exists())
			failure = true;
		return failure;
	}
	
	/**
	 * 清除app缓存
	 */
	public void clearAppCache()
	{
		/**
		//清除webview缓存
		File file = CacheManager.getCacheFileBaseDir();  
		if (file != null && file.exists() && file.isDirectory()) {  
		    for (File item : file.listFiles()) {  
		    	item.delete();  
		    }  
		    file.delete();  
		}  		  
		deleteDatabase("webview.db");  
		deleteDatabase("webview.db-shm");  
		deleteDatabase("webview.db-wal");  
		deleteDatabase("webviewCache.db");  
		deleteDatabase("webviewCache.db-shm");  
		deleteDatabase("webviewCache.db-wal");  
		//清除数据缓存
		clearCacheFolder(getFilesDir(),System.currentTimeMillis());
		clearCacheFolder(getCacheDir(),System.currentTimeMillis());
		//2.2版本才有将应用缓存转移到sd卡的功能
		if(isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)){
			clearCacheFolder(MethodsCompat.getExternalCacheDir(this),System.currentTimeMillis());
		}
		//清除编辑器保存的临时内容
		Properties props = getProperties();
		for(Object key : props.keySet()) {
			String _key = key.toString();
			if(_key.startsWith("temp"))
				removeProperty(_key);
		}
		**/
	}	
	
	/**
	 * 清除缓存目录
	 * @param dir 目录
	 * @param numDays 当前系统时间
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {          
	    int deletedFiles = 0;         
	    if (dir!= null && dir.isDirectory()) {             
	        try {                
	            for (File child:dir.listFiles()) {    
	                if (child.isDirectory()) {              
	                    deletedFiles += clearCacheFolder(child, curTime);          
	                }  
	                if (child.lastModified() < curTime) {     
	                    if (child.delete()) {                   
	                        deletedFiles++;           
	                    }    
	                }    
	            }             
	        } catch(Exception e) {       
	            e.printStackTrace();    
	        }     
	    }       
	    return deletedFiles;     
	}
	
	/**
	 * 将对象保存到内存缓存中
	 * @param key
	 * @param value
	 */
	public void setMemCache(String key, Object value) {
		memCacheRegion.put(key, value);
	}
	
	/**
	 * 从内存缓存中获取对象
	 * @param key
	 * @return
	 */
	public Object getMemCache(String key){
		return memCacheRegion.get(key);
	}
	
	/**
	 * 保存磁盘缓存
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try{
			fos = openFileOutput("cache_"+key+".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		}finally{
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 获取磁盘缓存数据
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getDiskCache(String key) throws IOException {
		FileInputStream fis = null;
		try{
			fis = openFileInput("cache_"+key+".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		}finally{
			try {
				fis.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 保存对象
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file){
		if(!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}catch(FileNotFoundException e){
		}catch(Exception e){
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if(e instanceof InvalidClassException){
				File data = getFileStreamPath(file);
				data.delete();
			}
		}finally{
			try {
				ois.close();
			} catch (Exception e) {}
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return null;
	}

	public boolean containsProperty(String key){
		Properties props = getProperties();
		 return props.containsKey(key);
	}
	
	public void setProperties(Properties ps){
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties(){
		return AppConfig.getAppConfig(this).get();
	}
	
	public void setProperty(String key,String value){
		AppConfig.getAppConfig(this).set(key, value);
	}
	
	public String getProperty(String key){
		return AppConfig.getAppConfig(this).get(key);
	}
	public void removeProperty(String...key){
		AppConfig.getAppConfig(this).remove(key);
	}

	/**
	 * 获取内存中保存图片的路径
	 * @return
	 */
	public String getSaveImagePath() {
		return saveImagePath;
	}
	/**
	 * 设置内存中保存图片的路径
	 * @return
	 */
	public void setSaveImagePath(String saveImagePath) {
		this.saveImagePath = saveImagePath;
	}	
	
}
