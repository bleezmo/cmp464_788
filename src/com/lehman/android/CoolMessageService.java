package com.lehman.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.lehman.android.utils.Either;
import com.lehman.android.utils.Failure;
import com.lehman.android.utils.Success;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class CoolMessageService extends Service implements CoolMessageCallback{

	private Either<String> coolMessage = null;

	@Override
	public void onCreate() {
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCoolMessageReceived(Either<String> coolMessage) {
		this.coolMessage  = coolMessage;
	}
}
