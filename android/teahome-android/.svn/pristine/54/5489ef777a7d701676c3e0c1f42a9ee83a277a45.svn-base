package com.haorenao.app.ui.th;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haorenao.app.AppConfig;
import com.haorenao.app.AppContext;
import com.haorenao.app.AppException;
import com.haorenao.app.R;
import com.haorenao.app.adapter.GridViewFaceAdapter;
import com.haorenao.app.bean.Result;
import com.haorenao.app.bean.Tweet;
import com.haorenao.app.bean.th.PostResult;
import com.haorenao.app.bean.th.Threads;
import com.haorenao.app.common.FileUtils;
import com.haorenao.app.common.ImageUtils;
import com.haorenao.app.common.MediaUtils;
import com.haorenao.app.common.StringUtils;
import com.haorenao.app.common.UIHelper;
import com.haorenao.app.ui.BaseActivity;

/**
 * 茶友之家
 * 发表帖子
 */
public class BBSPub extends BaseActivity{
	
	private final static String TAG = "BBSPub";
	
	// 普通帖子
	private final static byte BBS_TYPE_CONTENT = 0X04;
	
	//当前板块
	private int curBBSCatalog = 1;
	
	private AppContext ac;
	private DisplayMetrics dm;
	private FrameLayout mForm;
	private ImageView mBack;
	private EditText mContent;
	private Button mPublish;
	private ImageView mFace;
	
	private ImageView mPick;
	private ImageView mAtme;
	private ImageView mSoftware;
	private ImageView mImage;
	private LinearLayout mClearwords;
	private TextView mNumberwords;
	
	private Handler mHandler;
	
	
	private GridView mGridView;
	private GridViewFaceAdapter mGVFaceAdapter;
	
	private Threads bbsPost;
	private File imgFile;
	private String theLarge;
	private String theThumbnail;
	private InputMethodManager imm;
	
	private String tempBBSKey = AppConfig.TEMP_TWEET;
	private String tempBBSImageKey = AppConfig.TEMP_TWEET_IMAGE;
	
	public static LinearLayout mMessage;
	public static Context mContext;
	
	private static final int MAX_TEXT_LENGTH = 160;//最大输入字数
	private static final String TEXT_ATME = "@请输入用户名 ";
	private static final String TEXT_SOFTWARE = "#请输入话题名#";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_pub);
		
		curBBSCatalog= getIntent().getIntExtra("curBBSCatalog", 1);
		
		mContext = this;
		//软键盘管理类
		imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		//初始化基本视图
		this.initView();
		//初始化表情视图
		this.initGridView();
	}
    @Override
	protected void onDestroy() {
    	mContext = null;
    	super.onDestroy();
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if(mGridView.getVisibility() == View.VISIBLE){
    		//隐藏表情
    		hideFace();
    	}
    }
    
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		if(mGridView.getVisibility() == View.VISIBLE) {
    			//隐藏表情
    			hideFace();
    		}else{
    			return super.onKeyDown(keyCode, event);
    		}
    	}
    	return true;
    }
    
	//初始化视图控件
    private void initView()
    {   
    	// 获得手机屏幕的像素大小
    	ac = (AppContext)getApplication();
    	dm = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);
    	mForm = (FrameLayout)findViewById(R.id.bbs_pub_form);
    	mBack = (ImageView)findViewById(R.id.bbs_pub_back);
    	mMessage = (LinearLayout)findViewById(R.id.bbs_pub_message);
    	mImage = (ImageView)findViewById(R.id.bbs_pub_image);
    	mPublish = (Button)findViewById(R.id.bbs_pub_publish);
    	mContent = (EditText)findViewById(R.id.bbs_pub_content);
    	mFace = (ImageView)findViewById(R.id.bbs_pub_footbar_face);
    	
    	mPick = (ImageView)findViewById(R.id.bbs_pub_footbar_photo);
    	mAtme = (ImageView)findViewById(R.id.bbs_pub_footbar_atme);
    	mSoftware = (ImageView)findViewById(R.id.bbs_pub_footbar_software);
    	mHandler = new Handler();
    	
    	mClearwords = (LinearLayout)findViewById(R.id.bbs_pub_clearwords);
    	mNumberwords = (TextView)findViewById(R.id.bbs_pub_numberwords);
    	
    	
    	mBack.setOnClickListener(UIHelper.finish(this));
    	mPublish.setOnClickListener(publishClickListener);
    	mImage.setOnLongClickListener(imageLongClickListener);
    	mFace.setOnClickListener(faceClickListener);
    	
    	mPick.setOnClickListener(pickClickListener);
    	mAtme.setOnClickListener(atmeClickListener);
    	mSoftware.setOnClickListener(softwareClickListener);
    	mClearwords.setOnClickListener(clearwordsClickListener);   	
    	//@某人
    	String atme = getIntent().getStringExtra("at_me");
    	int atuid = getIntent().getIntExtra("at_uid",0);
    	if(atuid > 0){
    		tempBBSKey = AppConfig.TEMP_TWEET + "_" + atuid;
    		tempBBSImageKey = AppConfig.TEMP_TWEET_IMAGE + "_" + atuid;
    	}
    	
    	//编辑器添加文本监听
    	mContent.addTextChangedListener(new TextWatcher() {		
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//保存当前EditText正在编辑的内容
				((AppContext)getApplication()).setProperty(tempBBSKey, s.toString());
				//显示剩余可输入的字数
				mNumberwords.setText((MAX_TEXT_LENGTH - s.length()) + "");
			}		
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}		
			public void afterTextChanged(Editable s) {}
		});
    	//编辑器点击事件
    	mContent.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//显示软键盘
				showIMM();
			}
		});
    	//设置最大输入字数
    	InputFilter[] filters = new InputFilter[1];  
    	filters[0] = new InputFilter.LengthFilter(MAX_TEXT_LENGTH);
    	mContent.setFilters(filters);
    	
    	//显示临时编辑内容
		UIHelper.showTempEditContent(this, mContent, tempBBSKey);
		//显示临时保存图片
		String tempImage = ((AppContext)getApplication()).getProperty(tempBBSImageKey);
		if(!StringUtils.isEmpty(tempImage)) {
    		Bitmap bitmap = ImageUtils.loadImgThumbnail(tempImage, 100, 100);
    		if(bitmap != null) {
    			imgFile = new File(tempImage);
				mImage.setImageBitmap(bitmap);
				mImage.setVisibility(View.VISIBLE);
			}
		}
		
		if(atuid > 0 && mContent.getText().length() == 0){
			mContent.setText(atme);
    		mContent.setSelection(atme.length());//设置光标位置
		}
    }
    
    //初始化表情控件
    private void initGridView() {
    	mGVFaceAdapter = new GridViewFaceAdapter(this);
    	mGridView = (GridView)findViewById(R.id.bbs_pub_faces);
    	mGridView.setAdapter(mGVFaceAdapter);
    	mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//插入的表情
				SpannableString ss = new SpannableString(view.getTag().toString());
				Drawable d = getResources().getDrawable((int)mGVFaceAdapter.getItemId(position));
				d.setBounds(0, 0, 35, 35);//设置表情图片的显示大小
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, view.getTag().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);				 
				//在光标所在处插入表情
				mContent.getText().insert(mContent.getSelectionStart(), ss);				
			}    		
    	});
    }
    
    private void showIMM() {
    	mFace.setTag(1);
    	showOrHideIMM();
    }
    private void showFace() {
		mFace.setImageResource(R.drawable.widget_bar_keyboard);
		mFace.setTag(1);
		mGridView.setVisibility(View.VISIBLE);
    }
    private void hideFace() {
    	mFace.setImageResource(R.drawable.widget_bar_face);
		mFace.setTag(null);
		mGridView.setVisibility(View.GONE);
    }
    private void showOrHideIMM() {
    	if(mFace.getTag() == null){
			//隐藏软键盘
			imm.hideSoftInputFromWindow(mContent.getWindowToken(), 0);
			//显示表情
			showFace();				
		}else{
			//显示软键盘
			imm.showSoftInput(mContent, 0);
			//隐藏表情
			hideFace();
		}
    }
    
    private View.OnClickListener faceClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			showOrHideIMM();
		}
	};
	
    
	private View.OnClickListener pickClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			//隐藏表情
			hideFace();		
			CharSequence[] items = {
					BBSPub.this.getString(R.string.img_from_album),
					BBSPub.this.getString(R.string.img_from_camera)
			};
			imageChooseItem(items);
		}
	};
	
	private View.OnClickListener atmeClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			//显示软键盘
			showIMM();			
    		//在光标所在处插入“@用户名”
			int curTextLength = mContent.getText().length();
			if(curTextLength < MAX_TEXT_LENGTH) {
				String atme = TEXT_ATME;
				int start,end;
				if((MAX_TEXT_LENGTH - curTextLength) >= atme.length()) {
					start = mContent.getSelectionStart() + 1;
					end = start + atme.length() - 2;
				} else {
					int num = MAX_TEXT_LENGTH - curTextLength;
					if(num < atme.length()) {
						atme = atme.substring(0, num);
					}
					start = mContent.getSelectionStart() + 1;
					end = start + atme.length() - 1;
				}
				if(start > MAX_TEXT_LENGTH || end > MAX_TEXT_LENGTH) {
					start = MAX_TEXT_LENGTH;
					end = MAX_TEXT_LENGTH;
				}
				mContent.getText().insert(mContent.getSelectionStart(), atme);
				mContent.setSelection(start, end);//设置选中文字
			}
		}
	};
	
	private View.OnClickListener softwareClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			//显示软键盘
			showIMM();
			
			//在光标所在处插入“#话题#”
			int curTextLength = mContent.getText().length();
			if(curTextLength < MAX_TEXT_LENGTH) {
				String software = TEXT_SOFTWARE;
				int start,end;
				if((MAX_TEXT_LENGTH - curTextLength) >= software.length()) {
					start = mContent.getSelectionStart() + 1;
					end = start + software.length() - 2;
				} else {
					int num = MAX_TEXT_LENGTH - curTextLength;
					if(num < software.length()) {
						software = software.substring(0, num);
					}					
					start = mContent.getSelectionStart() + 1;
					end = start + software.length() - 1;
				}
				if(start > MAX_TEXT_LENGTH || end > MAX_TEXT_LENGTH) {
					start = MAX_TEXT_LENGTH;
					end = MAX_TEXT_LENGTH;
				}
				mContent.getText().insert(mContent.getSelectionStart(), software);
	    		mContent.setSelection(start, end);//设置选中文字
			}
		}
	};
	
	private View.OnClickListener clearwordsClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			String content = mContent.getText().toString();
			if(!StringUtils.isEmpty(content)){
				UIHelper.showClearWordsDialog(v.getContext(), mContent, mNumberwords);
			}
		}
	};
	
	private View.OnLongClickListener imageLongClickListener = new View.OnLongClickListener() {
		public boolean onLongClick(View v) {
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			
			new AlertDialog.Builder(v.getContext())
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(getString(R.string.delete_image))
			.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//清除之前保存的编辑图片
					((AppContext)getApplication()).removeProperty(tempBBSImageKey);
					imgFile = null;
					mImage.setVisibility(View.GONE);
					dialog.dismiss();
				}
			})
			.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create().show();
			return true;
		}
		
	};
	
	
	/**
	 * 操作选择
	 * @param items
	 */
	public void imageChooseItem(CharSequence[] items )
	{
		AlertDialog imageDialog = new AlertDialog.Builder(this).setTitle(R.string.ui_insert_image).setIcon(android.R.drawable.btn_star).setItems(items,
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int item)
				{
					//手机选图
					if( item == 0 )
					{
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
						intent.addCategory(Intent.CATEGORY_OPENABLE); 
						intent.setType("image/*"); 
						startActivityForResult(Intent.createChooser(intent, "选择图片"),ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD); 
					}
					//拍照
					else if( item == 1 )
					{	
						String savePath = "";
						//判断是否挂载了SD卡
						String storageState = Environment.getExternalStorageState();		
						if(storageState.equals(Environment.MEDIA_MOUNTED)){
							savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TeaHome/Camera/";//存放照片的文件夹
							File savedir = new File(savePath);
							if (!savedir.exists()) {
								savedir.mkdirs();
							}
						}
						
						//没有挂载SD卡，无法保存文件
						if(StringUtils.isEmpty(savePath)){
							UIHelper.ToastMessage(BBSPub.this, "无法保存照片，请检查SD卡是否挂载");
							return;
						}

						String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
						String fileName = "osc_" + timeStamp + ".jpg";//照片命名
						File out = new File(savePath, fileName);
						Uri uri = Uri.fromFile(out);
						
						theLarge = savePath + fileName;//该照片的绝对路径
						
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
						startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
					}   
				}}).create();
		
		 imageDialog.show();
	}
	
	@Override 
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
	{ 
    	if(resultCode != RESULT_OK) return;
		
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what == 1 && msg.obj != null){
					//显示图片
					mImage.setImageBitmap((Bitmap)msg.obj);
					mImage.setVisibility(View.VISIBLE);
				} else if (msg.what == 2) {
					//选择图片出现问题
					Toast.makeText(BBSPub.this, getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		new Thread(){
			public void run() 
			{
				Bitmap bitmap = null;
				
				//从SDK卡选择图片
		        if(requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) 
		        {         	
		        	if(data == null)  return;
		        	Uri thisUri = data.getData();
		        	
		        	String thePath ="";
		        	final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
		        	if (isKitKat) {
		        		thePath = getPath(BBSPub.this, thisUri);
		        	} else {
		        		
			        	thePath = ImageUtils.getAbsolutePathFromNoStandardUri(thisUri);
		        	}
		        	
		        	//如果是标准Uri
		        	if(StringUtils.isEmpty(thePath))
		        	{
		        		theLarge = ImageUtils.getAbsoluteImagePath(BBSPub.this,thisUri);
		        	}
		        	else
		        	{
		        		theLarge = thePath;
		        	}
		        	
		        	String attFormat = FileUtils.getFileFormat(theLarge);
		        	if(!"photo".equals(MediaUtils.getContentType(attFormat)))
		        	{
		        		Message msg = new Message();
		        		msg.what = 2;
		        		handler.sendMessage(msg);
		        		
		        		return;
		        	}
		        	
		        	//获取图片缩略图 只有Android2.1以上版本支持
		    		if(AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)){
		    			String imgName = FileUtils.getFileName(theLarge);
		    			bitmap = ImageUtils.loadImgThumbnail(BBSPub.this, imgName, MediaStore.Images.Thumbnails.MICRO_KIND);
		    		}
		        	
		        	if(bitmap == null && !StringUtils.isEmpty(theLarge))
		        	{
		        		bitmap = ImageUtils.loadImgThumbnail(theLarge, 100, 100);
		        	}
		        }
		        //拍摄图片
		        else if(requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA)
		        {	
		        	if(bitmap == null && !StringUtils.isEmpty(theLarge))
		        	{
		        		bitmap = ImageUtils.loadImgThumbnail(theLarge, 100, 100);
		        	}
		        }
		        
				if(bitmap!=null)
				{
					//存放照片的文件夹
					String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TeaHome/Camera/";
					File savedir = new File(savePath);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}
					
					String largeFileName = FileUtils.getFileName(theLarge);
					String largeFilePath = savePath + largeFileName;
					//判断是否已存在缩略图
					if(largeFileName.startsWith("thumb_") && new File(largeFilePath).exists()) 
					{
						theThumbnail = largeFilePath;
						imgFile = new File(theThumbnail);
					} 
					else 
					{
						//生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if(new File(theThumbnail).exists())
						{
							imgFile = new File(theThumbnail);
						}
						else
						{
							try {
								//压缩上传的图片
								ImageUtils.createImageThumbnail(BBSPub.this, theLarge, theThumbnail, 800, 80);
								imgFile = new File(theThumbnail);
							} catch (IOException e) {
								e.printStackTrace();
							}	
						}						
					}					
					//保存动弹临时图片
					((AppContext)getApplication()).setProperty(tempBBSImageKey, theThumbnail);
					
					Message msg = new Message();
					msg.what = 1;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}				
			};
		}.start();
    }
	
	/**
	 * 发布帖子
	 * @param tweetType 动弹类型
	 * @param content 文本动弹的内容
	 */
	private void pubBBS(final byte tweetType, String content) {
		
		if(!ac.isLogin()){
			UIHelper.showLoginDialog(BBSPub.this);
			return;
		}
		
		mMessage.setVisibility(View.VISIBLE);
		mForm.setVisibility(View.GONE);
		
		bbsPost = new Threads();
		bbsPost.setTitle("");
		bbsPost.setContent(content);
		if (tweetType == BBS_TYPE_CONTENT) {
			if(imgFile != null) {
				bbsPost.setImages(imgFile.getName());
			}
			
		}
		
		
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					//清除之前保存的编辑内容
					ac.removeProperty(tempBBSKey,tempBBSImageKey);
					finish();
				}else{
					mMessage.setVisibility(View.GONE);
					if (tweetType == BBS_TYPE_CONTENT) 
						mForm.setVisibility(View.VISIBLE);
					
				}
			}				
		};
		
		new Thread(){
			public void run() {
				Message msg =new Message();
				PostResult res = null;
				int what = 0;
				try {
					res = ac.pubBBS(curBBSCatalog,bbsPost);
					what = 1;
					msg.what = 1;
					msg.obj = res;
	            } catch (AppException e) {
	            	e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
	            }
				handler.sendMessage(msg);
				UIHelper.sendBroadCastBBSPost(BBSPub.this, what, res, bbsPost);
			}
		}.start();
	}
	
	private View.OnClickListener publishClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			
			String content = mContent.getText().toString();
			if(StringUtils.isEmpty(content)){
				UIHelper.ToastMessage(v.getContext(), "请输入动弹内容");
				return;
			}
			// 调用动弹发布方法
			pubBBS(BBS_TYPE_CONTENT, content);
		}
	};
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {

	        // Return the remote address
	        if (isGooglePhotosUri(uri))
	            return uri.getLastPathSegment();

	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
	
	public String selectImage(Context context,Intent data){
		Uri selectedImage = data.getData();
//		Log.e(TAG, selectedImage.toString());
		if(selectedImage!=null){			
			String uriStr=selectedImage.toString();
			String path=uriStr.substring(10,uriStr.length());
			if(path.startsWith("com.sec.android.gallery3d")){
				Log.e(TAG, "It's auto backup pic path:"+selectedImage.toString());
				return null;
			}
		}		
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return picturePath;		
	}
	
}
