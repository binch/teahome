package com.haorenao.app.adapter;

import java.util.List;

import com.haorenao.app.R;
import com.haorenao.app.bean.News;
import com.haorenao.app.bean.th.Article;
import com.haorenao.app.bean.th.Shop;
import com.haorenao.app.common.BitmapManager;
import com.haorenao.app.common.StringUtils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 茶友之家
 * 商店Adapter类
 */
public class ListViewShopAdapter extends BaseAdapter {
	private Context 					context;//运行上下文
	private List<Shop> 					listItems;//数据集合
	private LayoutInflater 				listContainer;//视图容器
	private int 						itemViewResource;//自定义项视图源 
	static class ListItemView{				//自定义控件集合  
	        public TextView title;  
		    public TextView summary;
		    public ImageView pic;
	 }  

	/**
	 * 实例化Adapter
	 * @param context
	 * @param data
	 * @param resource
	 */
	public ListViewShopAdapter(Context context, List<Shop> data,int resource) {
		this.context = context;			
		this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.itemViewResource = resource;
		this.listItems = data;
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
			listItemView.title = (TextView)convertView.findViewById(R.id.shop_shop_listitem_title);
			listItemView.summary = (TextView)convertView.findViewById(R.id.shop_shop_listitem_summary);
			listItemView.pic= (ImageView)convertView.findViewById(R.id.shop_shop_listitem_pic);
			
			//设置控件集到convertView
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}	
		
		//设置文字和图片
		Shop shop = listItems.get(position);
		
		listItemView.title.setText(shop.getTitle());
		listItemView.title.setTag(shop);//设置隐藏参数(实体类)
		listItemView.summary.setText(shop.getDesc());
		
		//异步加载图片
//		BitmapManager bmpManager;
//		bmpManager = new BitmapManager(BitmapFactory.decodeResource(context.getResources(), R.drawable.image_loading));
//		bmpManager.loadBitmap(shop.getDesc(), listItemView.pic);

		
		return convertView;
	}
}