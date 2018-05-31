package com.zhouwenguang.hz.gitjavascript;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.zhouwenguang.hz.gitjavascript.application.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity2 extends Activity {
	public static final int CAMERA  = 0x01;
	  
	private String contactId, contactName;
	private WebView webView;
	private Handler handler = new Handler();
	SelectPicPopupWindow2 menuWindow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_activity2);
		init();
	}

	
	
	private void init() {
		webView = (WebView) findViewById(R.id.myweb2);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new MyJavaScript(this, handler),
				"myjavascript");
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String url = bundle.getString("url");
		Log.d("TAGGG", "url...."+url);
		if (null != MyApplication.ld) {
			MyApplication.ld.dismiss();
		}
		WebSettings websetting = webView.getSettings();
		webView.loadUrl(url);
	}
	public void btnClick(View v) {
		if(v.getId()==R.id.regGoBackLogin){
			MainActivity2.this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
			
		}else if(v.getId()==R.id.share){
			menuWindow = new SelectPicPopupWindow2(MainActivity2.this);
			menuWindow.showAtLocation(MainActivity2.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // ����layout��PopupWindow����ʾ��λ��
		}else if(v.getId()==R.id.getByCamera){
			Intent intent = new Intent();
      		 Intent intent_camera = getPackageManager()
      				.getLaunchIntentForPackage("com.android.camera");
      		if (intent_camera != null) {
      			intent.setPackage("com.android.camera");
      		}
      		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
      		startActivityForResult(intent, CAMERA);// r
      		menuWindow.dismiss();
		}else if(v.getId()==R.id.getByAlbum){
			menuWindow.dismiss();
		}else if(v.getId()==R.id.btn_cancel){
			menuWindow.dismiss();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CAMERA && resultCode == Activity.RESULT_OK && null != data){
			   String sdState=Environment.getExternalStorageState();
			   if(!sdState.equals(Environment.MEDIA_MOUNTED)){
			    Log.d("Tag", "sd card unmount");
			    return;
			   }
			   new DateFormat();
			   String name= DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA))+".jpg";
			   Bundle bundle = data.getExtras();
			   //��ȡ������ص����ݣ���ת��ΪͼƬ��ʽ
			   Bitmap bitmap = (Bitmap)bundle.get("data");
			   FileOutputStream fout = null;
			   //�����ļ��洢·��
			   File file = new File("/sdcard/pintu/");
			   if(!file.exists()){
				   file.mkdirs();
			   }
			   String filename=file.getPath()+"/"+name;
			   try {
			    fout = new FileOutputStream(filename);
			    //��ͼƬ����ѹ��
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			   } catch (FileNotFoundException e) {
			    e.printStackTrace();
			   }finally{
			    try {
			     fout.flush();
			     fout.close();
			    } catch (IOException e) {
			     e.printStackTrace();
			    }
			   }
			   //Ӧ�ý�ͼƬ���͵���������Ȼ����¼�����ҳ
//			   BitmapDrawable bd=new BitmapDrawable(bitmap);
//			   image.setBackground(bd);
//			   image.setImageBitmap(bitmap);;
			   
		}
	}

}
