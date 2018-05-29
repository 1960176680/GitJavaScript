package com.zhouwenguang.hz.gitjavascript;


import android.app.Activity;  
import android.content.Context;  
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;  
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.view.LayoutInflater;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.View.OnTouchListener;  
import android.view.ViewGroup.LayoutParams;  
import android.widget.Button;  
import android.widget.PopupWindow;  
import android.widget.Toast;
  
public class SelectPicPopupWindow2 extends PopupWindow {  
	public static final int CAMERA  = 0x01;
  
    private Button  btn_cancel;  
    private Button  getByCamera;  
    private Button  getByAlbum;  
    private View mMenuView;  
    Context ctx;
    public SelectPicPopupWindow2(Activity context) {  
        super(context);  
        this.ctx=context;
        LayoutInflater inflater = (LayoutInflater) ctx  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
       mMenuView = inflater.inflate(R.layout.mult2, null);  
       //��ʼ������
       init();
    }

	private void init() {
//		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);         
//		getByAlbum = (Button) mMenuView.findViewById(R.id.getByAlbum);         
//		getByCamera = (Button) mMenuView.findViewById(R.id.getByCamera);         
//		//ȡ����ť  
//        btn_cancel.setOnClickListener(new OnClickListener() {  
//  
//            public void onClick(View v) {  
//                //���ٵ�����  
//                dismiss();  
//            }  
//        });   
//        //�����ֻ����  
//        getByAlbum.setOnClickListener(new OnClickListener() {  
//        	
//        	public void onClick(View v) {  
//        		//���ٵ�����  
//        		dismiss();  
//        	}  
//        });   
        //�����ֻ����
//        getByCamera.setOnClickListener(new OnClickListener() {  
//        	
//        	public void onClick(View v) {  
//        		Intent intent = new Intent();
//       		 Intent intent_camera = ctx.getPackageManager()
//       				.getLaunchIntentForPackage("com.android.camera");
//       		if (intent_camera != null) {
//       			intent.setPackage("com.android.camera");
//       		}
//       		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//       		//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(""));// file�����ձ����ļ�
//       		((Activity) ctx).startActivityForResult(intent, CAMERA);// r
//       		//���ٵ�����  
//            dismiss(); 
//        	}  
//        });   
		//����SelectPicPopupWindow��View  
        this.setContentView(mMenuView);  
        //����SelectPicPopupWindow��������Ŀ�  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true);  
        //����SelectPicPopupWindow�������嶯��Ч��  
        this.setAnimationStyle(R.style.AnimationPreview);  
        //ʵ����һ��ColorDrawable��ɫΪ��͸��  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //����SelectPicPopupWindow��������ı���  
        this.setBackgroundDrawable(dw);  
        //mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
		
	}  
  
}  
