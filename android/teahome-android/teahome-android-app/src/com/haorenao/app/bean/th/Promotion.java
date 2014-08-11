package com.haorenao.app.bean.th;


/**
 * 茶友之家
 * 促销商品实体类
 */
public class Promotion extends THEntity {
	
	/**
	 * 商品图片地址
	 */
	private String image_name;
	/**
	 * 商店id
	 */
	private String shop;
	/**
	 * 商品id
	 */
	private String item;
	
	public Promotion(){
		
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "Promotion [image_name=" + image_name + ", shop=" + shop
				+ ", item=" + item + "]";
	}
	
	
}
