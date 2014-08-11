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
 * 问答列表实体类
 */
public class QuestionList extends THEntity {

	/**
	 * 板块分类1 
	 */
	public static int CATALOG_ALL =1;
	public static int CATALOG_ADMIN =2;
	
	private int pageIndex;
	private int pageSize;
	private int questionCount;
	private int category;
	private List<Question> questionList = new ArrayList<Question>();
	

	
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



	public int getQuestionCount() {
		return questionCount;
	}



	public void setQuestionCount(int newsCount) {
		this.questionCount = newsCount;
	}



	public int getBoard() {
		return category;
	}



	public void setBoard(int board) {
		this.category = board;
	}



	public List<Question> getQuestionList() {
		return questionList;
	}



	public void setQuestionList(List<Question> questionList) {
		this.questionList = questionList;
	}



	public static QuestionList parse(InputStream inputStream) throws IOException, AppException {
		QuestionList questionList = new QuestionList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Question[] articles = new Gson().fromJson(json, Question[].class);
		questionList.getQuestionList().addAll(Arrays.asList(articles));
		questionList.questionCount = questionList.getQuestionList().size();
		return questionList;
	}
}
