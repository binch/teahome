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
 * 首页新闻大图列表
 */
public class BigPictureList extends THEntity {

	private int picCount;
	private List<BigPicture> bigPicList = new ArrayList<BigPicture>();
		
	public int getPicCount() {
		return picCount;
	}

	public void setPicCount(int picCount) {
		this.picCount = picCount;
	}

	public List<BigPicture> getBigPicList() {
		return bigPicList;
	}

	public void setBigPicList(List<BigPicture> bigPicList) {
		this.bigPicList = bigPicList;
	}

	public static BigPictureList parse(InputStream inputStream) throws IOException, AppException {
		BigPictureList picList = new BigPictureList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		BigPicture[] articles = new Gson().fromJson(json, BigPicture[].class);
		picList.getBigPicList().addAll(Arrays.asList(articles));
		return picList;
	}
}
