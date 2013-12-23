package com.lehman.android.rss;

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

import com.lehman.android.utils.*;

public class Downloader {
	public static final Either<StringBuffer> downloadText(String strUrl){
		try {
			URL url = new URL(strUrl);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(url.openStream()));
			StringBuffer sb = new StringBuffer();
			String line = in.readLine();
			while(line != null){
				sb.append(sb);
				line = in.readLine();
			}
			return new Success<StringBuffer>(sb);
		} catch (MalformedURLException e) {
			return new Failure<StringBuffer>(e);
		} catch (IOException e) {
			return new Failure<StringBuffer>(e);
		}
	}
	public static final Either<ByteBuffer> downloadBytes(String strUrl){
		try {
			URL url = new URL(strUrl);
			ByteBuffer bb = ByteBuffer.allocate(256);
			BufferedInputStream in = new BufferedInputStream(url.openStream());
			int available = in.available();
			while(available > 0){
				if(bb.remaining() < available) bb = resizeByteBuffer(bb,available * 2);
				byte[] buf = new byte[available];
				in.read(buf);
				bb.put(buf);
				available = in.available();
			}
			return new Success<ByteBuffer>(bb);
		} catch (MalformedURLException e) {
			return new Failure<ByteBuffer>(e);
		} catch (IOException e) {
			return new Failure<ByteBuffer>(e);
		}
	}
	private static final ByteBuffer resizeByteBuffer(ByteBuffer bb, int extra){
		ByteBuffer newbb = ByteBuffer.allocate(bb.capacity()+extra);
		return newbb.put(bb);
	}
	public static final Either<File> downloadBytesToFile(String strUrl, CacheManager cm){
		try {
			return cm.writeToFile(cm.newCachedFile(), new URL(strUrl).openStream());
		} catch (MalformedURLException e) {
			return new Failure<File>(e);
		} catch (IOException e) {
			return new Failure<File>(e);
		}
	}
}
