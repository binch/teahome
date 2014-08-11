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
 * 促销商品实体类列表
 */
public class PromotionList extends THEntity {

	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	
	private int pageSize;
	private int promotionCount;
	private List<Promotion> promotionList = new ArrayList<Promotion>();
	
	
	
	public int getPageSize() {
		return pageSize;
	}



	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public int getPromotionCount() {
		return promotionCount;
	}



	public void setPromotionCount(int promotionCount) {
		this.promotionCount = promotionCount;
	}


	public List<Promotion> getPromotionList() {
		return promotionList;
	}



	public void setPromotionList(List<Promotion> promotionList) {
		this.promotionList = promotionList;
	}



	public static PromotionList parse(InputStream inputStream) throws IOException, AppException {
		PromotionList pList = new PromotionList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Promotion[] parr = new Gson().fromJson(json, Promotion[].class);
		pList.getPromotionList().addAll(Arrays.asList(parr));
		pList.promotionCount = pList.getPromotionList().size();
		return pList;
	}
}
