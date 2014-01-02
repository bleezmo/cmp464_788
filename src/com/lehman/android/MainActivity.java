package com.lehman.android;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity{
	private static final int GET_COOL_MESSAGE = 0;
	private static final int GET_COOL_MESSAGE_ACK = 1;
	
	private Messenger messenger;
	private final ServiceConnection serviceConnection = new ServiceConnection(){
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("MainActivity","connected to service");
			Messenger serviceMessenger = new Messenger(service);
			Message message = Message.obtain();
			message.replyTo = messenger;
			try {
				serviceMessenger.send(message);
			} catch (RemoteException e) {
				Log.e("MainActivity", "could not send message to service", e);
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i("MainActivity","disconnected from service");
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TextView tv = (TextView) findViewById(R.id.cool_message);
		final Intent i = new Intent(this,CoolMessageService.class);
		messenger = new Messenger(new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch(msg.what){
				case GET_COOL_MESSAGE_ACK:
					Bundle data = msg.getData();
					String error = data.getString("error");
					if(error == null){
						String coolMessage = data.getString("message");
						tv.setText("The cool message received is: "+coolMessage);
						
					}else{
						tv.setText("An error occurred: "+error);
					}
					stopService(i);
					return true;
				default:
					return false;
				}
			}
		}));
		startService(i);
		bindService(i,serviceConnection,0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}

}
