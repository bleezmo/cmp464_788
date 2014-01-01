package com.lehman.android;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

import com.lehman.android.utils.Either;
import com.lehman.android.utils.Failure;
import com.lehman.android.utils.Success;

public class GetCoolMessageTask extends AsyncTask<Void,Either<String>,Either<String>>{
	private CoolMessageCallback coolMessageCallback;
	public GetCoolMessageTask(CoolMessageCallback coolMessageCallback){
		this.coolMessageCallback = coolMessageCallback;
	}
	@Override
	protected Either<String> doInBackground(Void... params) {
		HttpURLConnection urlConnection = null;
		Either<String> optid;
		try {
			URL url = new URL("http://10.0.2.2:9000/coolmessage/process");
			urlConnection = (HttpURLConnection) url.openConnection();
			optid = getResponseContents(urlConnection);
			urlConnection = null;
		}catch(MalformedURLException e){
			return new Failure<String>(e);
		}catch (IOException e) {
			return new Failure<String>(e);
		}finally{
			if(urlConnection != null) urlConnection.disconnect();
		}
		publishProgress(optid);
		if(optid.isSuccess()){
			String id = optid.getObject();
			try {
				URL url = new URL("http://10.0.2.2:9000/coolmessage/get/"+id);
				urlConnection = (HttpURLConnection) url.openConnection();
				while(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
					urlConnection.disconnect();
					urlConnection = null;
					Thread.sleep(10000);
					urlConnection = (HttpURLConnection) url.openConnection();
				}
				Either<String> coolMessage = getResponseContents(urlConnection);
				urlConnection = null;
				return coolMessage;
			}catch(MalformedURLException e){
				return new Failure<String>(e);
			}catch (IOException e) {
				return new Failure<String>(e);
			} catch (InterruptedException e) {
				return new Failure<String>(e);
			}finally{
				if(urlConnection != null) urlConnection.disconnect();
			}
		}else{
			return optid;
		}
	}
	private static final Either<String> getResponseContents(HttpURLConnection urlConnection){
		try {
			BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
			BufferedReader in = new BufferedReader(
		                  new InputStreamReader(bis));
			StringBuffer sb = new StringBuffer();
			String line = in.readLine();
			while(line != null){
				sb.append(sb);
				line = in.readLine();
			}
			return new Success<String>(sb.toString());
		} catch (IOException e) {
			return new Failure<String>(e);
		}finally {
			urlConnection.disconnect();
		}
	}
	@Override
	protected void onProgressUpdate(Either<String>... values) {
		Either<String> optid = values[0];
		coolMessageCallback.onIdReceived(optid);
	}
	@Override
	protected void onPostExecute(Either<String> result) {
		coolMessageCallback.onCoolMessageReceived(result);
	}
	
}
