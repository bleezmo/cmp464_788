package com.lehman.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activity4 extends Activity{
	public static final String MSG_RESULT = "msg_result";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_4);
	}
	
	public void done(View view){
		EditText et = (EditText) findViewById(R.id.msg_result);
		String msgResult = et.getText().toString();
		Intent intent = new Intent();
		intent.putExtra(MSG_RESULT, msgResult);
		setResult(RESULT_OK,intent);
		finish();
	}

}
