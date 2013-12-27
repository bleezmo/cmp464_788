package com.lehman.android;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lehman.android.utils.CacheManager;
import com.lehman.android.utils.Either;

public class ByteImageAdapter extends BaseAdapter{
	private static class ByteImage{
		public byte[] bytes;
		public ByteImage(byte[] bytes){
			this.bytes = bytes;
		}
	}
	private ByteImage[] images;
	private Activity ctx;
	public ByteImageAdapter(Activity ctx, int imageCount){
		this.ctx = ctx;
		images = new ByteImage[imageCount];
	}
	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		return images[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv;
		if(convertView == null){
			LayoutInflater inflater = ctx.getLayoutInflater();
			iv = (ImageView) inflater.inflate(R.layout.myimage, null);
		}else{
			iv = (ImageView) convertView;
		}
		if(images[position] == null){
			new Thread(new ByteImageDownloader(iv,images,position)).start();
		}else{
			iv.setImageBitmap(BitmapFactory.decodeByteArray(images[position].bytes, 0, images[position].bytes.length, new BitmapFactory.Options()));
		}
		return iv;
	}
	private static class ByteImageDownloader implements Runnable{
		private ByteImage[] images;
		private int position;
		private ImageView iv;
		public ByteImageDownloader(ImageView iv, ByteImage[] images, int position){
			this.images = images;
			this.position = position;
			this.iv = iv;
		}
		@Override
		public void run() {
			Either<byte[]> optbytes = Downloader.downloadBytes("http://theoutlawlife.files.wordpress.com/2013/01/oh-the-huge-manatee.jpg");
			if(optbytes.isSuccess()){
				images[position] = new ByteImage(optbytes.getObject());
				iv.post(new Runnable(){
					@Override
					public void run() {
						iv.setImageBitmap(BitmapFactory.decodeByteArray(images[position].bytes, 0, images[position].bytes.length, new BitmapFactory.Options()));
						iv.invalidate();
					}
				});
			}else{
				Log.e("image error",optbytes.getError().getMessage());
			}
		}
	}
}
