package com.haorenao.app.bean.th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.haorenao.app.AppException;

/**
 * 茶友之家
 * 首页新闻列表实体类
 */
public class ArticleList extends THEntity {

	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	
	private int catalog;
	private int pageSize;
	private int newsCount;
	private List<Article> articleList = new ArrayList<Article>();
	
	public int getCatalog() {
		return catalog;
	}
	public int getPageSize() {
		return pageSize;
	}
	public int getArticleCount() {
		return newsCount;
	}
	public List<Article> getArticlelist() {
		return articleList;
	}
	
	public static ArticleList parse(InputStream inputStream) throws IOException, AppException {
		ArticleList articleList = new ArticleList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Article[] articles = new Gson().fromJson(json, Article[].class);
		articleList.getArticlelist().addAll(Arrays.asList(articles));
		articleList.newsCount = articleList.getArticlelist().size();
		return articleList;
	}
}
