package com.lehman.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.lehman.android.utils.Downloader;
import com.lehman.android.utils.Either;

public class ThreadDownloader {
	public static final void run(final Activity ctx){
		new Thread(new Runnable(){
			@Override
			public void run() {
				Either<byte[]> optba = Downloader.downloadBytes("http://theoutlawlife.files.wordpress.com/2013/01/oh-the-huge-manatee.jpg");
				if(optba.isSuccess()){
					final byte[] byteArray = optba.getObject();
					ctx.runOnUiThread(new Runnable(){
						@Override
						public void run() {
							ImageView iv = (ImageView) ctx.findViewById(R.id.myimage);
							Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,new BitmapFactory.Options());
							iv.setImageBitmap(bm);
							iv.invalidate();
						}
					});
				}else{
					Log.e("image error",optba.getError().getMessage());
				}
			}
        }).start();
	}
}
