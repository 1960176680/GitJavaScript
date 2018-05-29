package com.zhouwenguang.hz.gitjavascript.dialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.zhouwenguang.hz.gitjavascript.R;

public class LodingDialog extends Dialog {
	

	public LodingDialog(Context ctx){
		super(ctx);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog��̨���������
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.loading);
	}
	

}
