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
 * 首页论坛帖子列表实体类
 */
public class ThreadsList extends THEntity {

	/**
	 * 板块分类1 
	 */
	public static int CATALOG_LIFE =1;
	public static int CATALOG_ADMIN =2;
	
	private int pageIndex;
	private int pageSize;
	private int threadCount;
	private int board;
	private List<Threads> threadList = new ArrayList<Threads>();
	

	
	public int getPageIndex() {
		return pageIndex;
	}



	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}



	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public int getThreadCount() {
		return threadCount;
	}



	public void setThreadCount(int newsCount) {
		this.threadCount = newsCount;
	}



	public int getBoard() {
		return board;
	}



	public void setBoard(int board) {
		this.board = board;
	}



	public List<Threads> getThreadList() {
		return threadList;
	}



	public void setThreadList(List<Threads> threadList) {
		this.threadList = threadList;
	}



	public static ThreadsList parse(InputStream inputStream) throws IOException, AppException {
		ThreadsList threadList = new ThreadsList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Threads[] articles = new Gson().fromJson(json, Threads[].class);
		threadList.getThreadList().addAll(Arrays.asList(articles));
		threadList.threadCount = threadList.getThreadList().size();
		return threadList;
	}
}
