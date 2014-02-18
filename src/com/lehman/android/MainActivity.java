package com.lehman.android;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("theme2activity1", false)){
			Button btn1 = (Button) findViewById(R.id.btn1);
			btn1.setTextAppearance(this, R.style.Theme2);
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_settings){
			Intent intent = new Intent(this,SettingsActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
    
    public void toActivity2(View view){
    	Intent intent = new Intent(this,Activity2.class);
    	startActivity(intent);
    }
}
