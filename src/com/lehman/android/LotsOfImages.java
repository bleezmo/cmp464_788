package com.lehman.android;

import com.lehman.android.adapters.ByteImageAdapter;
import com.lehman.android.adapters.ImageToFileAdapter;
import com.lehman.android.adapters.ImageToFileAdapterWithQueue;
import com.lehman.android.utils.CacheManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class LotsOfImages extends Activity{
	private CacheManager cm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_images);
        cm = new CacheManager(getCacheDir().toString());
		ListView lv = (ListView) findViewById(R.id.imagelist);
		int imageCount = 50;
		//lv.setAdapter(new ByteImageAdapter(this,imageCount));
		//lv.setAdapter(new ImageToFileAdapter(this,cm,imageCount));
		//lv.setAdapter(new ImageToFileAdapterWithQueue(this,cm,imageCount));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cm.cleanAll(0);
		cm = null;
	}

}
