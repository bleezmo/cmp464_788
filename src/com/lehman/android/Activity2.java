package com.lehman.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activity2 extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2);
	}
	
	public void toActivity3(View view){
		Intent intent = new Intent(this, Activity3.class);
		EditText editText = (EditText) findViewById(R.id.msg_tobesent);
		String message = editText.getText().toString();
		intent.putExtra("MSG_TOBESENT", message);
		startActivity(intent);
	}
}
