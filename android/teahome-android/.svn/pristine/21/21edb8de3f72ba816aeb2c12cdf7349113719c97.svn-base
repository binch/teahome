package com.haorenao.app.bean.th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.Gson;
import com.haorenao.app.AppException;
import com.haorenao.app.bean.Base;
import com.haorenao.app.bean.Notice;
import com.haorenao.app.bean.Result;
import com.haorenao.app.common.StringUtils;

import android.util.Xml;

/**
 * 茶友之家
 * 登录用户实体类
 */
public class THUserInfo extends THEntity {
	
	//用户名，密码，昵称
	private String username;
	private String passwd;
	private String nickname;
	//个人简介，积分，等级
	private String desc;
	private String point;
	private String grade;
	private String thumb;
	private String ret;
	
	
	public String getThumb() {
		return thumb;
	}


	public void setThumb(String thumb) {
		this.thumb = thumb;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getPoint() {
		return point;
	}


	public void setPoint(String point) {
		this.point = point;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String garde) {
		this.grade = garde;
	}


	public String getRet() {
		return ret;
	}


	public void setRet(String ret) {
		this.ret = ret;
	}

	

	@Override
	public String toString() {
		return "THUserInfo [username=" + username + ", passwd=" + passwd
				+ ", nickname=" + nickname + ", desc=" + desc + ", point="
				+ point + ", garde=" + grade + ", thumb=" + thumb + ", ret="
				+ ret + "]";
	}


	public static THUserInfo parse(InputStream stream) throws IOException, AppException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		THUserInfo user = new Gson().fromJson(json, THUserInfo.class);
		
		return user;
	}
}
