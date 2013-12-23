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

public class CacheManager {
	private static class CacheFile implements Comparable<CacheFile>{
		private String filepath;
		private Integer priority;
		private Long timestamp;
		public CacheFile(String filepath, Integer priority){
			this.filepath = filepath;
			this.priority = priority;
			this.timestamp = System.currentTimeMillis();
		}
		public Long getSize(){
			return new File(filepath).length();
		}
		@Override
		public int compareTo(CacheFile file) {
			return (int) (this.timestamp - file.timestamp);
		}
	}
	private static final long MAX_SIZE = 5242880L; //5MB
	
	private String cachepath;
	private ArrayList<CacheFile> files;
	private volatile Object writeLock = new Object();
	public CacheManager(String cachepath){
		this.cachepath = cachepath;
		files = new ArrayList<CacheFile>();
	}
	/**create a file with a default name
	 * @return
	 */
	public File newCachedFile(){
		return newCachedFile("temp"+System.currentTimeMillis(),0);
	}
	public File newCachedFile(String name, Integer priority){
		synchronized(writeLock){
			File cachedir = new File(cachepath);
			File file = new File(cachedir.getPath(),name);
			files.add(new CacheFile(file.getPath(),priority));
			return file;
		}
	}
	public Either<File> writeToFile(String filename, InputStream is){
		return writeToFile(new File(filename),is);
	}
 	public Either<File> writeToFile(File file, InputStream is){
 		synchronized(writeLock){
 			if(!file.exists() || !file.canWrite()) 
 				return new Failure<File>(new RuntimeException("file does not exist or cannot write to file"));
 			BufferedInputStream bis = new BufferedInputStream(is);
 			try {
 				FileOutputStream fis = new FileOutputStream(file);
 				int available = bis.available();
 				int written = 0;
 				while(available > 0){
 					byte[] buf = new byte[available];
 					bis.read(buf);
 					fis.write(buf, written, available);
 					written += available;
 					available = bis.available();
 				}
 				bis.close();
 				fis.flush();
 				fis.close();
 				cleanUp();
 				return new Success<File>(file);
 			} catch (FileNotFoundException e) {
 				return new Failure<File>(e);
 			} catch (IOException e) {
 				return new Failure<File>(e);
 			}
 		}
	}
	private static final long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
	public void cleanUp(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				int pi = 0; //priority index
				long totalSize = folderSize(new File(cachepath));
				if(totalSize < MAX_SIZE) return;
				while(totalSize > (MAX_SIZE/2) && pi < 11){
					synchronized(writeLock){
						ArrayList<CacheFile> priorityFiles = new ArrayList<CacheFile>();
						for(int i = 0; i < files.size(); i++){
							if(files.get(i).priority <= pi) {
								priorityFiles.add(files.get(i));
							}
						}
						Collections.sort(priorityFiles);
						long cutSize = 0;
						ArrayList<CacheFile> cutFiles = new ArrayList<CacheFile>();
						for(int i = 0; i < priorityFiles.size(); i++){
							if((totalSize-cutSize) < (MAX_SIZE/2)){
								break;
							}else{
								CacheFile priorityFile = priorityFiles.get(i);
								cutFiles.add(priorityFile);
								cutSize += priorityFile.getSize();
							}
						}
						for(int i = 0; i < cutFiles.size(); i++){
							CacheFile cutFile = cutFiles.get(i);
							new File(cutFile.filepath).delete();
							for(int j = 0; j < files.size(); i++){
								if(files.get(j).filepath == cutFile.filepath){
									files.remove(j);
								}
							}
						}
						totalSize = folderSize(new File(cachepath));
					}
					pi++;
				}
			}
		}).start();
	}
}
