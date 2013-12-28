package com.lehman.android.adapters;

import com.lehman.android.R;
import com.lehman.android.utils.Downloader;
import com.lehman.android.utils.SQLiteManager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

public class ImageToDatabaseAdapter extends CursorAdapter{
	private final int COLUMN_IMAGES_INDEX;
	private Activity ctx;
	private String[] urls;
	public ImageToDatabaseAdapter(Activity ctx, Cursor c, String[] urls) {
		super(ctx, c, false);
		COLUMN_IMAGES_INDEX = c.getColumnIndex(SQLiteManager.COLUMN_IMAGE);
		this.ctx = ctx;
		this.urls = urls;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		byte[] data = cursor.getBlob(COLUMN_IMAGES_INDEX);
		ImageView iv = (ImageView) view;
		if(data != null) iv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		byte[] data = cursor.getBlob(COLUMN_IMAGES_INDEX);
		LayoutInflater inflater = ctx.getLayoutInflater();
		ImageView iv = (ImageView) inflater.inflate(R.layout.myimage, null);
		iv.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
		return iv;
	}
	
	public static class AsyncDownloader extends AsyncTask<String,Void,Void>{
		private SQLiteDatabase db;
		private String reQuery;
		private CursorAdapter adapter;
		
		public AsyncDownloader(SQLiteDatabase db, String reQuery, CursorAdapter adapter){
			this.db = db;
			this.reQuery = reQuery;
			this.adapter = adapter;
		}
		
		@Override
		protected Void doInBackground(String... urls) {
			for(int i = 0; i < urls.length; i++){
				Downloader.downloadBytesToDatabase(urls[i], db);
				if(i % 5 == 0) publishProgress();
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			adapter.changeCursor(db.rawQuery(reQuery, null));
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.i("ImageToDatabaseAdapter.AsyncDownloader","finished downloading");
		}
		
	}

}
