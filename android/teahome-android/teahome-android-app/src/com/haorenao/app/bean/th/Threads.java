package com.haorenao.app.bean.th;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.haorenao.app.AppException;

/**
 * 茶友之家
 * 论坛帖子实体类
 */
public class Threads extends THEntity {

	private String id;
	private String grade;
	private String last_reply;
	private String create_time;
	private String images;
	private String username;
	private String nickname;
	private String thumb;
	private String title;
	private String content;
	private ArrayList<ThreadComment> comments = new ArrayList<ThreadComment>();
	
	public class ThreadComment {
		private String id;
		private String create_time;
		private String images;
		private String username;
		private String nickname;
		private String thumb;
		private String content;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCreate_time() {
			return create_time;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
		public String getImages() {
			return images;
		}
		public void setImages(String images) {
			this.images = images;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getThumb() {
			return thumb;
		}
		public void setThumb(String thumb) {
			this.thumb = thumb;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		@Override
		public String toString() {
			return "ThreadComment [id=" + id + ", create_time=" + create_time
					+ ", images=" + images + ", username=" + username
					+ ", nickname=" + nickname + ", thumb=" + thumb
					+ ", content=" + content + "]";
		}
	
	}
	
	public Threads(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLast_reply() {
		return last_reply;
	}

	public void setLast_reply(String last_reply) {
		this.last_reply = last_reply;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<ThreadComment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<ThreadComment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Threads [id=" + id + ", grade=" + grade + ", last_reply="
				+ last_reply + ", create_time=" + create_time + ", images="
				+ images + ", username=" + username + ", nickname=" + nickname
				+ ", thumb=" + thumb + ", title=" + title + ", content="
				+ content + ", comments=" + comments + "]";
	}

	public static Threads parse(InputStream inputStream) throws IOException, AppException {
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Threads thread = new Gson().fromJson(json, Threads.class);
		
		return thread;
	}
	
}
