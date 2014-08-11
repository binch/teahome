package com.haorenao.app.widget;

import com.haorenao.app.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;

/**
 * 用户信息对话框控件
 */
public class UserInfoDialog extends Dialog {
	
	private LayoutParams lp;

	public UserInfoDialog(Context context) {
		super(context, R.style.Dialog);		
		setContentView(R.layout.user_center_content);
		
		// 设置点击对话框之外能消失
		setCanceledOnTouchOutside(true);
		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.TOP;
		lp.dimAmount = 0; // 去背景遮盖
		lp.alpha = 1.0f;
		lp.y = 55;
		getWindow().setAttributes(lp);

	}
}
