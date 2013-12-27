package com.lehman.android;

import java.net.URL;

import com.lehman.android.utils.Downloader;
import com.lehman.android.utils.Either;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class AsyncDownloader {
	public static final void run(final Activity ctx){
		new MyAsyncTask(ctx).execute("http://theoutlawlife.files.wordpress.com/2013/01/oh-the-huge-manatee.jpg");
	}
	private static final class MyAsyncTask extends AsyncTask<String,Either<byte[]>,Long>{

		private Activity ctx;
		public MyAsyncTask(Activity ctx){
			this.ctx = ctx;
		}
		@Override
		protected Long doInBackground(String... urls) {
			long bytesReceived = 0;
			for(int i = 0; i < urls.length; i++){
				Either<byte[]> optba = Downloader.downloadBytes(urls[i]);
				publishProgress(optba);
				if(optba.isSuccess()) bytesReceived += optba.getObject().length;
			}
			return bytesReceived;
		}

		@Override
		protected void onProgressUpdate(Either<byte[]>... values) {
			if(values[0].isSuccess()){
				final byte[] byteArray = values[0].getObject();
				ImageView iv = (ImageView) ctx.findViewById(R.id.myimage);
				Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,new BitmapFactory.Options());
				iv.setImageBitmap(bm);
				iv.invalidate();
			}
		}
		
		@Override
		protected void onPostExecute(Long result) {
			Log.i("AsyncDownloader","downloaded "+result+" bytes");
		}
	}
}
