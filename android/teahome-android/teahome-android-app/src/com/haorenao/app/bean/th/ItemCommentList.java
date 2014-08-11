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
 * 商品评价列表
 */
public class ItemCommentList extends THEntity {
	
	private int catalog;
	private int pageSize;
	private int commentCount;
	private List<ItemComment> commentList = new ArrayList<ItemComment>();
	
	
	public int getCatalog() {
		return catalog;
	}


	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getCommentCount() {
		return commentCount;
	}


	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}


	public List<ItemComment> getCommentList() {
		return commentList;
	}


	public void setCommentList(List<ItemComment> commentList) {
		this.commentList = commentList;
	}


	public static ItemCommentList parse(InputStream inputStream) throws IOException, AppException {
		ItemCommentList cList = new ItemCommentList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		ItemComment[] sarr = new Gson().fromJson(json, ItemComment[].class);
		cList.getCommentList().addAll(Arrays.asList(sarr));
		cList.commentCount = cList.getCommentList().size();
		return cList;
	}
}
