package com.lehman.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class Activity2 extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_2);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("theme2activity2", false)){
			Button btn2 = (Button) findViewById(R.id.btn2);
			btn2.setTextAppearance(this, R.style.Theme2);
		}
	}
	
	public void toActivity3(View view){
		Intent intent = new Intent(this,Activity3.class);
		startActivity(intent);
	}
	
}
