package com.haorenao.app.bean.th;

import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.haorenao.app.common.StringUtils;

/**
 * 茶友之家
 * 接口URL实体类
 */
public class THURLs implements Serializable {
	public final static String HOSTIP = "223.4.180.21:8080";
	public final static String HOST = "www.haorenao.com:8080";
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	
	/**
	 * 论坛用户头像地址
	 */
	public final static String THREAD_USER_ICON_IMG_URL = "http://www.haorenao.cn/static/tea/threadimages/";
	/**
	 * 论坛帖子图片地址
	 */
	public final static String THREAD_INFO_IMG_URL = "http://www.haorenao.cn/static/tea/threadimages/";
	
	/**
	 * 商品促销图片地址
	 */
	public final static String SHOP_PROMOTION_IMG_URL = "http://www.haorenao.cn/static/shopimages/promotion/";
	
	private final static String URL_SPLITTER = "/";
	private final static String URL_TEAHOME_CMD = "cmd/?cmd=";
	
	private final static String URL_API_HOST = HTTP + HOSTIP + URL_SPLITTER + URL_TEAHOME_CMD;
	
	//首页文章
	public final static String ARTICLES_LIST = URL_API_HOST+"get_articles";
	public final static String ARTICLES_BIG_PIC = URL_API_HOST+"get_bigpictures";
	
	//论坛
	public final static String THREAD_LIST = URL_API_HOST+"get_threads";
	public final static String THREAD_DETAIL = URL_API_HOST+"get_thread";
	public final static String THREAD_POST = URL_API_HOST+"post_thread";
	public final static String THREAD_REPLY = URL_API_HOST+"post_reply";
	public final static String THREAD_BOARD = URL_API_HOST+"get_boards";
	
	//问答
	public final static String QUESTION_LIST = URL_API_HOST+"get_questions";
	public final static String QUESTION_DETAIL = URL_API_HOST+"get_question";
	public final static String QUESTION_POST = URL_API_HOST+"post_question";
	public final static String QUESTION_POST_ANSWER = URL_API_HOST+"post_answer";
	public final static String QUESTION_ACCEPT_ANSWER = URL_API_HOST+"accept_answer";
	public final static String QUESTION_ANSWER_CATEGORY = URL_API_HOST+"get_qa_cats";
	
	//用户登录注册
	public final static String USER_REG = URL_API_HOST+"reg_user";
	public final static String USER_LOGIN = URL_API_HOST+"login_user";
	public final static String USER_GETINFO = URL_API_HOST+"get_userinfo";
	public final static String USER_UPDATEINFO = URL_API_HOST+"update_userinfo";
	
	//商店
	public final static String SHOP_LIST_PROMOTIONS= URL_API_HOST+"get_promotions";
	public final static String SHOP_LIST_ALL_SHOP = URL_API_HOST+"get_shops";
	public final static String SHOP_LIST_ALL_ITEM = URL_API_HOST+"get_all_items";
	public final static String SHOP_LIST_ONE_ITEM = URL_API_HOST+"get_item";
	
	//商品评价
	public final static String SHOP_ALL_ITEM_COMMENTS = URL_API_HOST+"get_all_item_comments";
	public final static String SHOP_ONE_ITEM_COMMENTS = URL_API_HOST+"get_item_comments";
	public final static String SHOP_POST_ITEM_COMMENTS = URL_API_HOST+"post_item_comment";
	
	//购物车 && 订单
	public final static String CAT_LIST = URL_API_HOST+"get_shop_cats";
	
	public final static String ORDERS_LIST = URL_API_HOST+"get_user_orders";
	public final static String ORDERS_ITEM = URL_API_HOST+"get_order";
	public final static String ORDERS_NEW = URL_API_HOST+"new_order";
	
	//通知信息
	public final static String MESSAGE_LIST = URL_API_HOST+"get_user_atmessages";
	public final static String MESSAGE_MARK_READ = URL_API_HOST+"mark_read_atmessage";
	
	
	public final static int URL_OBJ_TYPE_OTHER = 0x000;
	public final static int URL_OBJ_TYPE_NEWS = 0x001;
	public final static int URL_OBJ_TYPE_SOFTWARE = 0x002;
	public final static int URL_OBJ_TYPE_QUESTION = 0x003;
	public final static int URL_OBJ_TYPE_ZONE = 0x004;
	public final static int URL_OBJ_TYPE_BLOG = 0x005;
	public final static int URL_OBJ_TYPE_TWEET = 0x006;
	public final static int URL_OBJ_TYPE_QUESTION_TAG = 0x007;
	
	private int objId;
	private String objKey = "";
	private int objType;
	
	public int getObjId() {
		return objId;
	}
	public void setObjId(int objId) {
		this.objId = objId;
	}
	public String getObjKey() {
		return objKey;
	}
	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}
	public int getObjType() {
		return objType;
	}
	public void setObjType(int objType) {
		this.objType = objType;
	}
}
