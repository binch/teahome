package com.haorenao.app.bean.th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.haorenao.app.AppException;
import com.haorenao.app.bean.Base;

/**
 * 茶友之家
 * 登录用户实体类
 */
public class THUser extends Base {
	
	public class Ret {
		private String ret;

		public String getRet() {
			return ret;
		}

		public void setRet(String ret) {
			this.ret = ret;
		}

		@Override
		public String toString() {
			return "Ret [ret=" + ret + "]";
		}
		
	}


	public static boolean parse(InputStream stream) throws IOException, AppException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Ret ret = new Gson().fromJson(json, Ret.class);
		if(ret.getRet() != null && ret.getRet().equals("ok")){
			return true;
		}
		
		return false;
	}
	
	
}
