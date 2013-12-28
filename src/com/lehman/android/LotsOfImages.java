package com.lehman.android;

import java.io.File;

import com.lehman.android.adapters.ByteImageAdapter;
import com.lehman.android.adapters.ImageToDatabaseAdapter;
import com.lehman.android.adapters.ImageToFileAdapter;
import com.lehman.android.adapters.ImageToFileAdapterWithQueue;
import com.lehman.android.utils.CacheManager;
import com.lehman.android.utils.Downloader;
import com.lehman.android.utils.SQLiteManager;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class LotsOfImages extends Activity{
	private CacheManager cm;
	private SQLiteManager dbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple_images);
        //cm = new CacheManager(getCacheDir().toString());
		final int imageCount = 20;
		//lv.setAdapter(new ByteImageAdapter(this,imageCount));
		//lv.setAdapter(new ImageToFileAdapter(this,cm,imageCount));
		//lv.setAdapter(new ImageToFileAdapterWithQueue(this,cm,imageCount));

		dbHelper = new SQLiteManager(this);
		new Thread(new Runnable(){
			@Override
			public void run() {
				final SQLiteDatabase db = dbHelper.getWritableDatabase();
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						String rawQuery = 
								"SELECT "+SQLiteManager.COLUMN_ID+","+SQLiteManager.COLUMN_IMAGE + 
								" FROM "+SQLiteManager.TABLE_IMAGES;
						ListView lv = (ListView) findViewById(R.id.imagelist);
						String[] urls = new String[imageCount];
						for(int i = 0; i < urls.length; i++){
							urls[i] = "http://theoutlawlife.files.wordpress.com/2013/01/oh-the-huge-manatee.jpg";
						}
						ImageToDatabaseAdapter adapter = new ImageToDatabaseAdapter(LotsOfImages.this,
								db.rawQuery(rawQuery, null),
								urls
								);
						new ImageToDatabaseAdapter.AsyncDownloader(db, rawQuery, adapter).execute(urls);
						lv.setAdapter(adapter);
					}
				});
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(cm != null){
			cm.cleanAll(0);
			cm = null;
		}
		if(dbHelper != null){
		    new File(getCacheDir(),SQLiteManager.DATABASE_NAME).delete();
			dbHelper.close();
		}
	}

}
