package com.lehman.android;


import android.app.Activity;
import android.widget.ListView;

import com.lehman.android.utils.CacheManager;

public class Lesson2 {
	public static final void run(Activity ctx, CacheManager cm, final int imageCount){
		ListView lv = (ListView) ctx.findViewById(R.id.imagelist);
		//lv.setAdapter(new ByteImageAdapter(ctx,imageCount));
		//lv.setAdapter(new ImageToFileAdapter(ctx,cm,imageCount));
		lv.setAdapter(new ImageToFileAdapterWithQueue(ctx,cm,imageCount));
	}
	
}
