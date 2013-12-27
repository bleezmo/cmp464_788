package com.lehman.android.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lehman.android.R;
import com.lehman.android.R.layout;
import com.lehman.android.utils.CacheManager;
import com.lehman.android.utils.Downloader;
import com.lehman.android.utils.Either;

public class ImageToFileAdapterWithQueue extends BaseAdapter{
	private String[] files;
	private Activity ctx;
	private CacheManager cm;
	private ImageToFileQueue queue;
	public ImageToFileAdapterWithQueue(Activity ctx, CacheManager cm, int imageCount){
		this.ctx = ctx;
		this.cm = cm;
		files = new String[imageCount];
		queue = new ImageToFileQueue(imageCount);
		new Thread(queue).start();
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
			queue.addToQueue(new ImageToFileJob(cm, iv,files,position));
		}else{
			iv.setImageBitmap(BitmapFactory.decodeFile(files[position]));
		}
		return iv;
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		queue.close();
	}
	private static class ImageToFileQueue implements Runnable{
		private int maxConsume;
		private int consumed = 0;
		private ArrayBlockingQueue<ImageToFileJob> queue;
		private List<Integer> positionsAdded; //this is need because list adapter calls getView many times. not just once for each view. need to make sure we don't have duplicate jobs.
		private volatile boolean keepGoing = true;
		public ImageToFileQueue(int maxConsume){
			this.maxConsume = maxConsume;
			queue = new ArrayBlockingQueue<ImageToFileJob>(maxConsume);
			positionsAdded = Collections.synchronizedList(new ArrayList<Integer>(maxConsume));
		}
		private boolean hasDuplicateJob(Integer position){
			for(int i = 0; i < positionsAdded.size(); i++){
				if(positionsAdded.get(i) == position) return true;
			}
			return false;
		}
		private void removeJobListing(Integer position){
			for(int i = 0; i < positionsAdded.size(); i++){
				if(positionsAdded.get(i) == position) {
					positionsAdded.remove(position);
					break;
				}
			}
		}
		public void addToQueue(ImageToFileJob job){
			if(!hasDuplicateJob(job.position)){
				queue.add(job);
				positionsAdded.add(job.position);
			}
		}
		public void close(){
			keepGoing = false;
		}
		@Override
		public void run() {
			while(keepGoing && consumed < maxConsume){
				try {
					ImageToFileJob job = queue.poll(500, TimeUnit.MILLISECONDS);
					if(job != null) {
						job.run();
						consumed++;
						removeJobListing(job.position);
					}
				} catch (InterruptedException e) {
					Log.e("ImageToFileAdapter", "interrupted while waiting for image job", e);
				}
			}
		}
	}
	private static class ImageToFileJob implements Runnable{
		private String[] files;
		private int position;
		private ImageView iv;
		private CacheManager cm;
		public ImageToFileJob(CacheManager cm, ImageView iv, String[] files, int position){
			this.files = files;
			this.position = position;
			this.iv = iv;
			this.cm = cm;
		}
		@Override
		public void run() {
			Either<File> optfile = Downloader.downloadBytesToFile("http://theoutlawlife.files.wordpress.com/2013/01/oh-the-huge-manatee.jpg",cm);
			if(optfile.isSuccess()){
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
