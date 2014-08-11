package com.haorenao.app.ui;

import com.haorenao.app.R;
import com.haorenao.app.common.StringUtils;
import com.haorenao.app.common.UIHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 用户反馈
 */
public class FeedBack extends BaseActivity{
	
	private ImageButton mClose;
	private EditText mEditer;
	private Button mPublish;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		this.initView();
	}
	
	//初始化视图控件
    private void initView()
    {
    	mClose = (ImageButton)findViewById(R.id.feedback_close_button);
    	mEditer = (EditText)findViewById(R.id.feedback_content);
    	mPublish = (Button)findViewById(R.id.feedback_publish);
    	
    	mClose.setOnClickListener(UIHelper.finish(this));
    	mPublish.setOnClickListener(publishClickListener);
    }
    
    private View.OnClickListener publishClickListener = new View.OnClickListener() {		
		public void onClick(View v) {
			String content = mEditer.getText().toString();
			
			if(StringUtils.isEmpty(content)) {
				UIHelper.ToastMessage(v.getContext(), "反馈信息不能为空");
				return;
			}
			
			Intent i = new Intent(Intent.ACTION_SEND);  
			//i.setType("text/plain"); //模拟器
			i.setType("message/rfc822") ; //真机
			i.putExtra(Intent.EXTRA_EMAIL, new String[]{"635512001@qq.com"});  
			i.putExtra(Intent.EXTRA_SUBJECT,"用户反馈-Android客户端");  
			i.putExtra(Intent.EXTRA_TEXT,content);  
			startActivity(Intent.createChooser(i, "Sending mail..."));
			finish();
		}
	};
}
