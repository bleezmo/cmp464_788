package com.lehman.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainActivity extends Activity{
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Lesson1.run(this);
    }
    
    public void toImageList(View v){
    	Intent intent = new Intent(this,LotsOfImages.class);
    	startActivity(intent);
    }
}
