package com.haorenao.app.bean.th;

/**
 * 茶友之家实体类
 */
public abstract class THEntity extends THBase {

	protected String cacheKey;

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
