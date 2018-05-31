package com.zhouwenguang.hz.gitjavascript;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.zhouwenguang.hz.gitjavascript.application.MyApplication;
import com.zhouwenguang.hz.gitjavascript.dialog.LodingDialog;
import com.zhouwenguang.hz.gitjavascript.po.LinkMan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MyJavaScript {

    private static final String[] PHONES_PROJECTION = new String[] {
    Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };

	public static final int CAMERA  = 0x01;

    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    private static final int PHONES_NUMBER_INDEX = 1;


    private static final int PHONES_PHOTO_ID_INDEX = 2;


    private static final int PHONES_CONTACT_ID_INDEX = 3;


    private ArrayList<String> mContactsName = new ArrayList<String>();


    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();

    private ArrayList<LinkMan> linkMans = new ArrayList<LinkMan>();

	 private final int DIALOG          = 0;
	 private final int REQUEST_CONTACT = 2;
    private WebView webview;

    private Handler handler;
     private Context mContext;
    public MyJavaScript(Context context,Handler handler){
        this.handler = handler;
        webview = (WebView) ((Activity)context).findViewById(R.id.myweb);
         mContext  =  context;
    }
//    /*
//     * java������ʾ��ҳ���첽 
//     */  
//    public void show(){
//      handler.post(new Runnable() {
//        public void run() {
//       // ��Ҫ��url������,�������ݸ���ҳ
//        String url = "javascript:contactlist('" + generateData() + "')";
//        webview.loadUrl(url);
//        }
//       });
//    }
//
    @JavascriptInterface
    public void call(final String phone){
          Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phone));
          mContext.startActivity(intent);
    } 

    @JavascriptInterface
    public void sendMsg(String phone,String str_content){  
    	String content;
    	try {
    		content = new String(str_content.getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {

			 Toast.makeText(mContext, "����ʧ��!", Toast.LENGTH_LONG).show();//��ʾ�ɹ�   
			e.printStackTrace();
		} 
    	SmsManager manager_sms = SmsManager.getDefault();
    	ArrayList<String> texts = manager_sms.divideMessage(str_content);
    	for(String text : texts){
    		manager_sms.sendTextMessage(phone, null, text, null, null);//�ֱ���ÿһ������
    	}
    	    Toast.makeText(mContext, "���ͳɹ�!", Toast.LENGTH_LONG).show();//��ʾ�ɹ�   	   
    } 

    @JavascriptInterface
    public void systemSendMsg(String phone,String str_content){
    	String content;
    	try {
    		content = new String(str_content.getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			 Toast.makeText(mContext, "����ʧ��!", Toast.LENGTH_LONG).show();//��ʾ�ɹ�   
			e.printStackTrace();
		} 
    	Uri uri = Uri.parse("smsto:"+phone);          
    	Intent it = new Intent(Intent.ACTION_SENDTO, uri);          
    	it.putExtra("sms_body", str_content);          
    	mContext.startActivity(it);
    }

    @JavascriptInterface
    public void getAllLinkMan(){
    	 Intent intent = new Intent();
         intent.setAction(Intent.ACTION_PICK);
         intent.setData(ContactsContract.Contacts.CONTENT_URI);
         ((MainActivity) mContext).startActivityForResult(intent, REQUEST_CONTACT);
    }
   

    @JavascriptInterface
    public String getPhoneLinkMan(){
    	linkMans.clear();
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext())
        {
            int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=" + ContactId, null, null);
            while(phone.moveToNext())
            {
                String Number = phone.getString(phone.getColumnIndex(Phone.NUMBER));
               // string += (contact + ":" + Number + "");
                LinkMan man=new LinkMan(contact, Number);
                Log.d("TAG",contact + ":" + Number + "");
                linkMans.add(man);
            }
        }
        cursor.close();
        Log.d("TAG", "linkMans.size()"+linkMans.size());
//        handler.post(new Runnable() {           
//            public void run() {  
//           // ��Ҫ��url������,�������ݸ���ҳ  
//            String url = "javascript:contactlist('" + ObjectToJson() + "')";  
//            webview.loadUrl(url);  
//            }  
//           });  
        String json=ObjectToJson();
    	return json;
    }
   
    /**
     * ������ת����Ϊjson����
     */
    public String ObjectToJson(){
    	 try {    
	        //�������json����
	        JSONArray arr = new JSONArray(); 
	        for(int i =0; i<linkMans.size();i++){
	        	 //����һ��json����  
    	        JSONObject obj = new JSONObject();  
    	        obj.put("id", i);  
    	        obj.put("name", linkMans.get(i).getName());  
    	        obj.put("phone", linkMans.get(i).getPhone());
    	        arr.put(obj);  
	        }
	        return arr.toString();  
	        } catch (JSONException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return ""; 
    }
    @JavascriptInterface
    public void getNext(String url,String direction,int dialog){
    //	webview.loadUrl("file:///android_asset/index2.html");
    	//webview.loadUrl("http://www.baidu.com");
//    	Uri uri=Uri.parse("file:///android_asset/index2.html");
//    	Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//    	mContext.startActivity(intent);
    	if(dialog==1){
    		MyApplication.ld=new LodingDialog(((MainActivity)mContext));
    		MyApplication.ld.show();
    	}
    	Intent intent=new Intent(mContext,MainActivity2.class);
    	Log.d("Tag", "url"+url+"direction"+direction);
    	intent.putExtra("url", url);
    	((Activity)mContext).startActivity(intent);
    	((Activity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);  
		//ld.dismiss();
    }
    /**
     * �����ֻ����
     */
    @JavascriptInterface
    public void getCamera(){
    	 Intent intent = new Intent();
		 Intent intent_camera = mContext.getPackageManager()
				.getLaunchIntentForPackage("com.android.camera");
		if (intent_camera != null) {
			intent.setPackage("com.android.camera");
		}
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(""));// file�����ձ����ļ�
		((Activity) mContext).startActivityForResult(intent, CAMERA);// r
    }
   
}  
