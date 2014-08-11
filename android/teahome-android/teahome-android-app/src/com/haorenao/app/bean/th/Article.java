package com.haorenao.app.bean.th;


/**
 * 茶友之家
 * 首页新闻新闻实体类
 */
public class Article extends THEntity {
	
	public final static String NODE_ID = "id";
	public final static String NODE_TITLE = "title";
	public final static String NODE_URL = "url";
	public final static String NODE_BODY = "body";
	public final static String NODE_AUTHORID = "authorid";
	public final static String NODE_AUTHOR = "author";
	public final static String NODE_PUBDATE = "pubDate";
	public final static String NODE_COMMENTCOUNT = "commentCount";
	public final static String NODE_FAVORITE = "favorite";
	public final static String NODE_START = "news";
	
	public final static String NODE_SOFTWARELINK = "softwarelink";
	public final static String NODE_SOFTWARENAME = "softwarename";
	
	public final static String NODE_NEWSTYPE = "newstype";
	public final static String NODE_TYPE = "type";
	public final static String NODE_ATTACHMENT = "attachment";
	public final static String NODE_AUTHORUID2 = "authoruid2";
	
	public final static int NEWSTYPE_NEWS = 0x00;//0 新闻
	public final static int NEWSTYPE_SOFTWARE = 0x01;//1 软件
	public final static int NEWSTYPE_POST = 0x02;//2 帖子
	public final static int NEWSTYPE_BLOG = 0x03;//3 博客

	private String id;
	private String pic_url;
	private String name;
	private String summary;
	private String text;
	

	public Article(){
		
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPic_url() {
		return pic_url;
	}


	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", pic_url=" + pic_url + ", name=" + name
				+ ", summary=" + summary + ", text=" + text + "]";
	}	
	
	
	
}
