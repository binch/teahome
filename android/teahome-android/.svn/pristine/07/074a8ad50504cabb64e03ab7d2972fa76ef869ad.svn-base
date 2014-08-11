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
 * 商店列表
 */
public class ShopList extends THEntity {
	public final static int SHOPTYPE_PROMOT = 0x01;//促销
	public final static int SHOPTYPE_SHOP = 0x02;//商店
	public final static int SHOPTYPE_ITEM = 0x03;//商品
	public final static int SHOPTYPE_COMMENT = 0x04;//商品评论
	
	private int catalog;
	private int pageSize;
	private int shopCount;
	private ArrayList<Shop> shopList = new ArrayList<Shop>();
	
	
	
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



	public int getShopCount() {
		return shopCount;
	}



	public void setShopCount(int shopCount) {
		this.shopCount = shopCount;
	}



	public List<Shop> getShopList() {
		return shopList;
	}



	public void setShopList(ArrayList<Shop> shopList) {
		this.shopList = shopList;
	}



	public static ShopList parse(InputStream inputStream) throws IOException, AppException {
		ShopList sList = new ShopList();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Shop[] sarr = new Gson().fromJson(json, Shop[].class);
		sList.getShopList().addAll(Arrays.asList(sarr));
		sList.shopCount = sList.getShopList().size();
		return sList;
	}
}
