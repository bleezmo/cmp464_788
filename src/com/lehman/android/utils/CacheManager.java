package com.lehman.android.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import android.content.Context;
import android.util.Log;

public class CacheManager {
	private static class CacheFile{
		private String filepath;
		private Integer priority;
		private long size = 0;
		public CacheFile(String filepath, Integer priority){
			this.filepath = filepath;
			this.priority = priority;
		}
	}
	private static final long MAX_SIZE = 5242880L; //5MB
	
	private String cachepath;
	private ArrayList<CacheFile> files;
	private long totalWritten = 0;
	private int maxPriority = 5;
	public CacheManager(String cachepath, int maxPriority){
		this.cachepath = cachepath;
		files = new ArrayList<CacheFile>();
		this.maxPriority = maxPriority;
	}
	public CacheManager(String cachepath){
		this.cachepath = cachepath;
		files = new ArrayList<CacheFile>();
	}
	/**create a file with a default name
	 * @return
	 */
	public Either<File> newCachedFile(){
		return newCachedFile("temp"+System.currentTimeMillis(),0);
	}
	public Either<File> newCachedFile(String name, Integer priority){
		File cachedir = new File(cachepath);
		try {
			File file = File.createTempFile(name, null,cachedir);
			files.add(new CacheFile(file.getPath(),priority));
			return new Success<File>(file);
		} catch (IOException e) {
			return new Failure<File>(e);
		}
	}
	public Either<File> writeToFile(String filename, InputStream is){
		return writeToFile(new File(filename),is);
	}
 	public synchronized Either<File> writeToFile(File file, InputStream is){
		if(totalWritten > MAX_SIZE) {
			cleanUp();
		}
 		CacheFile cacheFile = null;
 		for(int i = 0; i < files.size(); i++){
 			if(files.get(i).filepath == file.getPath()) cacheFile = files.get(i);
 		}
 		if(cacheFile == null) return new Failure<File>(new RuntimeException("no file found in cache"));
		if(!file.exists() || !file.canWrite()) 
			return new Failure<File>(new RuntimeException("file does not exist or cannot write to file"));
		BufferedInputStream bis = new BufferedInputStream(is,5000);
		try {
			FileOutputStream fis = new FileOutputStream(file);
			int more = 0;
			while(more > -1){
				int available = bis.available();
				if(available == 0) available = 1;
				byte[] buf = new byte[available];
				more = bis.read(buf);
				fis.write(buf);
				totalWritten+=buf.length;
				cacheFile.size = cacheFile.size+buf.length;
			}
			bis.close();
			fis.flush();
			fis.close();
			return new Success<File>(file);
		} catch (FileNotFoundException e) {
			return new Failure<File>(e);
		} catch (IOException e) {
			return new Failure<File>(e);
		}
	}
	private void cleanUp(){
		int pi = 0; //priority index
		if(totalWritten < MAX_SIZE) return;
		Log.i("CacheManager","running clean up. totalWritten is "+totalWritten);
		while(totalWritten > (MAX_SIZE/2) && pi < maxPriority){
			ArrayList<CacheFile> priorityFiles = new ArrayList<CacheFile>();
			long cutSize = 0;
			for(int i = 0; i < files.size(); i++){
				CacheFile cacheFile = files.get(i);
				if((totalWritten-cutSize) < (MAX_SIZE/2)){
					break;
				}else if(cacheFile.priority <= pi){
					priorityFiles.add(cacheFile);
					cutSize += cacheFile.size;
				}
			}
			removeFiles(priorityFiles);
			Log.i("CacheManager","after clean run, totalWritten is now "+totalWritten);
			pi++;
		}
	}
	private void removeFiles(ArrayList<CacheFile> cutFiles){
		for(int i = 0; i < cutFiles.size(); i++){
			removeFile(cutFiles.get(i));
		}
	}
	private void removeFile(CacheFile cacheFile){
		new File(cacheFile.filepath).delete();
		for(int i = 0; i < files.size(); i++){
			if(files.get(i).filepath == cacheFile.filepath){
				files.remove(i);
				break;
			}
		}
		totalWritten -= cacheFile.size;
	}
	/**
	 * removes all priority 0 files
	 */
	public synchronized void purge(){
		ArrayList<CacheFile> cutFiles = new ArrayList<CacheFile>();
		for(int i = 0; i < files.size(); i++){
			CacheFile file = files.get(i);
			if(file.priority == 0) {
				cutFiles.add(files.get(i));
			}
		}
		removeFiles(cutFiles);
	}
}
