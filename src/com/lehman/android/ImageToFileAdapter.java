package com.lehman.android;

import java.io.File;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lehman.android.utils.CacheManager;
import com.lehman.android.utils.Either;

public class ImageToFileAdapter extends BaseAdapter{
	private String[] files;
	private Activity ctx;
	private CacheManager cm;
	public ImageToFileAdapter(Activity ctx, CacheManager cm, int imageCount){
		this.ctx = ctx;
		this.cm = cm;
		files = new String[imageCount];
	}
	@Override
	public int getCount() {
		return files.length;
	}

	@Override
	public Object getItem(int position) {
		return files[position];
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
		if(files[position] == null){
			new Thread(new ImageToFileDownloader(cm, iv,files,position)).start();
		}else{
			iv.setImageBitmap(BitmapFactory.decodeFile(files[position]));
		}
		return iv;
	}
	private static class ImageToFileDownloader implements Runnable{
		private String[] files;
		private int position;
		private ImageView iv;
		private CacheManager cm;
		public ImageToFileDownloader(CacheManager cm, ImageView iv, String[] files, int position){
			this.files = files;
			this.position = position;
			this.iv = iv;
			this.cm = cm;
		}
		@Override
		public void run() {
			Either<File> optfile = Downloader.downloadBytesToFile("http://theoutlawlife.files.wordpress.com/2013/01/oh-the-huge-manatee.jpg",cm);
			if(optfile.isSuccess()){
				File file = optfile.getObject();
				files[position] = optfile.getObject().getPath();
				iv.post(new Runnable(){
					@Override
					public void run() {
						iv.setImageBitmap(BitmapFactory.decodeFile(files[position]));
						iv.invalidate();
					}
				});
			}else{
				Log.e("image error",optfile.getError().getMessage());
			}
		}
	}
}
