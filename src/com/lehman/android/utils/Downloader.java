package com.lehman.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import android.content.Context;
import android.util.Log;

import com.lehman.android.utils.*;

public class Downloader {
	public static final Either<byte[]> downloadBytes(String strUrl){
		try {
			URL url = new URL(strUrl);
			ByteBuffer bb = ByteBuffer.allocate(1000);
			BufferedInputStream in = new BufferedInputStream(url.openStream(),5000);
			int more = 0;
			while(more > -1){
				int available = in.available();
				if(bb.remaining() < available){
					bb = resizeByteBuffer(bb,available * 2);
				}
				byte[] buf;
				if(available > 0){
					buf = new byte[available];
				}else{
					buf = new byte[1];
				}
				more = in.read(buf);
				bb.put(buf);
			}
			bb.flip();
			byte[] array = new byte[bb.limit()];
			bb.get(array);
			return new Success<byte[]>(array);
		} catch (MalformedURLException e) {
			return new Failure<byte[]>(e);
		} catch (IOException e) {
			return new Failure<byte[]>(e);
		}
	}
	private static final ByteBuffer resizeByteBuffer(ByteBuffer bb, int extra){
		ByteBuffer newbb = ByteBuffer.allocate(bb.capacity()+extra);
		newbb.put(bb.array(), 0, bb.position());
		return newbb;
	}
	public static final Either<File> downloadBytesToFile(String strUrl, CacheManager cm){
		try {
			Either<File> cachedFile = cm.newCachedFile();
			if(cachedFile.isSuccess()){
				return cm.writeToFile(cachedFile.getObject(), new URL(strUrl).openStream());
			}else{
				return cachedFile;
			}
		} catch (MalformedURLException e) {
			return new Failure<File>(e);
		} catch (IOException e) {
			return new Failure<File>(e);
		}
	}
}
