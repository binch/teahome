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
 * 问答实体类
 */
public class Question extends THEntity {

	private String id;
	private String username;
	private String nickname;
	private String create_time;
	private String thumb;
	private String title;
	private String content;
	private String ended;
	
	
	private ArrayList<Answer> answers = new ArrayList<Answer>();
	
	public class Answer {
		private String id;
		private String username;
		private String nickname;
		private String thumb;
		private String content;
		private String create_time;
		private String accepted;
		
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
		public String getAccepted() {
			return accepted;
		}
		public void setAccepted(String accepted) {
			this.accepted = accepted;
		}
		@Override
		public String toString() {
			return "Answer [id=" + id + ", username=" + username
					+ ", nickname=" + nickname + ", thumb=" + thumb
					+ ", content=" + content + ", create_time=" + create_time
					+ ", accepted=" + accepted + "]";
		}
		
	
	}
	
	public Question(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public String getEnded() {
		return ended;
	}

	public void setEnded(String ended) {
		this.ended = ended;
	}

	public ArrayList<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public String toString() {
		return "Question [id=" + id + ", username=" + username + ", nickname="
				+ nickname + ", create_time=" + create_time + ", thumb="
				+ thumb + ", title=" + title + ", content=" + content
				+ ", ended=" + ended + ", answers=" + answers.toString() + "]";
	}

	public static Question parse(InputStream inputStream) throws IOException, AppException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8*1024);
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		String json = builder.toString();
		
		Question question = new Gson().fromJson(json, Question.class);
		
		return question;
	}
}
