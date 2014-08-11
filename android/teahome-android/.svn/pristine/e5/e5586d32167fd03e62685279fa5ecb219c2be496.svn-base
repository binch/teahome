package com.haorenao.app.bean.th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.haorenao.app.AppException;
import com.haorenao.app.bean.Base;

/**
 * POST数据返回的状态
 * @author alex
 *
 */
public class PostResult extends Base{
	
	private String ret;

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	@Override
	public String toString() {
		return "PostResult [ret=" + ret + "]";
	}
	
	public static PostResult parse(InputStream inputStream) throws IOException, AppException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		PostResult ret = new Gson().fromJson(json, PostResult.class);
		
		return ret;
	}
}
