package com.lehman.android;

import com.lehman.android.utils.CacheManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LotsOfImages extends Activity{
	private CacheManager cm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_images);
        cm = new CacheManager(getCacheDir().toString());
        Lesson2.run(this,cm,500);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cm.purge();
		cm = null;
	}

}
