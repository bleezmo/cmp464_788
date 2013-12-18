package com.lehman.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Activity3 extends Activity{
	public static final String MSG_TOBESENT = "MSG_TOBESENT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_3);
		String msgReceived = this.getIntent().getStringExtra(MSG_TOBESENT);
		setMessageReceived(msgReceived);
	}
	
	private void setMessageReceived(String msgReceived){
		TextView tv = (TextView) findViewById(R.id.message_received);
		tv.setText("received message from Activity 3: "+msgReceived);
	}
	
	public void toActivity4(View view){
		Intent intent = new Intent(this,Activity4.class);
		startActivityForResult(intent, Global.ACTIVITY4_RESULT);
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Global.ACTIVITY4_RESULT) {
        	TextView tv = (TextView) findViewById(R.id.on_msg_result);
            if (resultCode == RESULT_OK) {
            	String msgResult = data.getStringExtra(Activity4.MSG_RESULT);
            	tv.setText("received message from Activity 4: "+msgResult);
            }else if(resultCode == RESULT_CANCELED){
            	tv.setText("User did not click Done. No message received");
            }
        }
    }

}
