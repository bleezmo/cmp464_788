package com.lehman.android;

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
		int imageCount = 500;
		//lv.setAdapter(new ByteImageAdapter(ctx,imageCount));
		//lv.setAdapter(new ImageToFileAdapter(ctx,cm,imageCount));
		//lv.setAdapter(new ImageToFileAdapterWithQueue(ctx,cm,imageCount));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cm.cleanAll(0);
		cm = null;
	}

}
