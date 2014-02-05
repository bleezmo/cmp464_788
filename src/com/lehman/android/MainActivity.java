package com.lehman.android;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText et;
	private TextView tv;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.input_text);
        tv = (TextView) findViewById(R.id.output_text);
        et.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tv.setText(s);
			}
        	
        });
    }
    
    public void showToast(View view){
    	Toast.makeText(view.getContext(), "You clicked the button!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	int itemId = item.getItemId();
		if(itemId == R.id.reset_edittext){
			tv.setText("");
			et.setText("");
		}else if(itemId == R.id.action_settings){
			Intent intent = new Intent(this,SettingsActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
    public void showPlanet(View view){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	String planet = prefs.getString("favorite_planets", "None");
    	Toast.makeText(this, planet, Toast.LENGTH_SHORT).show();
    }
}
