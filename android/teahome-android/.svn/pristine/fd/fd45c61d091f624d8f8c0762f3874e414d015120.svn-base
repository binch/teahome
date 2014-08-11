package com.haorenao.app.adapter;

import java.util.List;

import com.haorenao.app.R;
import com.haorenao.app.bean.Comment;
import com.haorenao.app.bean.Comment.Refer;
import com.haorenao.app.bean.Comment.Reply;
import com.haorenao.app.bean.th.THURLs;
import com.haorenao.app.bean.th.Threads;
import com.haorenao.app.common.BitmapManager;
import com.haorenao.app.common.StringUtils;
import com.haorenao.app.common.UIHelper;
import com.haorenao.app.widget.LinkView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 茶友之家
 * 社区评论详细页面Adapter类
 */
public class ListViewBBSCommentAdapter extends BaseAdapter {
	private Context 					context;//运行上下文
	private List<Threads.ThreadComment> 				listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源 
	private BitmapManager 				bmpManager;
	static class ListItemView{				//自定义控件集合
			public ImageView face;
	        public TextView name;  
		    public TextView date;  
		    public LinkView content;
		    public TextView client;
		    public LinearLayout relies;
		    public LinearLayout refers;
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewBBSCommentAdapter(Context context, List<Threads.ThreadComment> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
		this.bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_dface_loading));
	}
	
	public int getCount() {
		return listItems.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}
	
	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.d("method", "getView");
		
		//自定义视图
		ListItemView  listItemView = null;
		
		if (convertView == null) {
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(this.itemViewResource, null);
			
			listItemView = new ListItemView();
			//获取控件对象
			listItemView.face = (ImageView)convertView.findViewById(R.id.comment_listitem_userface);
			listItemView.name = (TextView)convertView.findViewById(R.id.comment_listitem_username);
			listItemView.date = (TextView)convertView.findViewById(R.id.comment_listitem_date);
			listItemView.content = (LinkView)convertView.findViewById(R.id.comment_listitem_content);
			listItemView.client= (TextView)convertView.findViewById(R.id.comment_listitem_client);
			listItemView.relies = (LinearLayout)convertView.findViewById(R.id.comment_listitem_relies);
			listItemView.refers = (LinearLayout)convertView.findViewById(R.id.comment_listitem_refers);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		//设置文字和图片
		Threads.ThreadComment comment = listItems.get(position);
		String faceURL = THURLs.THREAD_USER_ICON_IMG_URL + comment.getThumb();
		if(faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)){
			listItemView.face.setImageResource(R.drawable.widget_dface);
		}else{
			bmpManager.loadBitmap(faceURL, listItemView.face);
		}
		listItemView.face.setTag(comment);//设置隐藏参数(实体类)
		listItemView.face.setOnClickListener(faceClickListener);
		String username = comment.getNickname();
		if(username == null || username.equals("")){
			username = comment.getUsername();
		}
		listItemView.name.setText(username);
		listItemView.date.setText(StringUtils.friendly_time(comment.getCreate_time()));
		listItemView.content.setLinkText(comment.getContent());
		listItemView.content.setTag(comment);//设置隐藏参数(实体类)
		
		
		if(StringUtils.isEmpty(listItemView.client.getText().toString()))
			listItemView.client.setVisibility(View.GONE);
		else
			listItemView.client.setVisibility(View.VISIBLE);
		
		listItemView.relies.setVisibility(View.GONE);
		listItemView.relies.removeAllViews();//先清空
//		if(comment.getReplies().size() > 0){
//			//评论数目
//			View view = listContainer.inflate(R.layout.comment_reply, null);
//			TextView tv = (TextView)view.findViewById(R.id.comment_reply_content);
//			tv.setText(context.getString(R.string.comment_reply_title, comment.getReplies().size()));
//			listItemView.relies.addView(view);
//			//评论内容
//			for(Reply reply : comment.getReplies()){
//				View view2 = listContainer.inflate(R.layout.comment_reply, null);
//				TextView tv2 = (TextView)view2.findViewById(R.id.comment_reply_content);
//				tv2.setText(reply.rauthor+"("+StringUtils.friendly_time(reply.rpubDate)+")："+reply.rcontent);
//				listItemView.relies.addView(view2);
//			}
//			listItemView.relies.setVisibility(View.VISIBLE);
//		}
		
//		listItemView.refers.setVisibility(View.GONE);
//		listItemView.refers.removeAllViews();//先清空
//		if(comment.getRefers().size() > 0){
//			//引用内容
//			for(Refer refer : comment.getRefers()){
//				View view = listContainer.inflate(R.layout.comment_refer, null);
//				TextView title = (TextView)view.findViewById(R.id.comment_refer_title);
//				TextView body = (TextView)view.findViewById(R.id.comment_refer_body);
//				title.setText(refer.refertitle);
//				body.setText(refer.referbody);
//				listItemView.refers.addView(view);
//			}
//			listItemView.refers.setVisibility(View.VISIBLE);
//		}
		
		return convertView;
	}
	
	private View.OnClickListener faceClickListener = new View.OnClickListener(){
		public void onClick(View v) {
			Comment comment = (Comment)v.getTag();
			UIHelper.showUserCenter(v.getContext(), comment.getAuthorId(), comment.getAuthor());
		}
	};
}