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
 * 商品列表
 */
public class ItemList extends THEntity {
	
	private int catalog;
	private int pageSize;
	private int itemCount;
	private List<Item> itemList = new ArrayList<Item>();
	
	


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




	public int getItemCount() {
		return itemCount;
	}




	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}




	public List<Item> getItemList() {
		return itemList;
	}




	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}




	public static ItemList parse(InputStream inputStream) throws IOException, AppException {
		ItemList iList = new ItemList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Item[] sarr = new Gson().fromJson(json, Item[].class);
		iList.getItemList().addAll(Arrays.asList(sarr));
		iList.itemCount = iList.getItemList().size();
		return iList;
	}
}
