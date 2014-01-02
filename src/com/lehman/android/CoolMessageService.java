package com.lehman.android;

import com.lehman.android.utils.Either;
import com.lehman.android.utils.Failure;
import com.lehman.android.utils.Success;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class CoolMessageService extends Service implements CoolMessageCallback{
	private static final int GET_COOL_MESSAGE = 0;
	private static final int GET_COOL_MESSAGE_ACK = 1;
	
	private Either<CoolMessage> coolMessage = null;
	private Messenger clientMessenger = null;
	
	private final void sendCoolMessage(){
		if(coolMessage != null && clientMessenger != null){
			Message replyMsg = Message.obtain();
			replyMsg.what = GET_COOL_MESSAGE_ACK;
			Bundle data = new Bundle();
			if(coolMessage.isSuccess()){
				data.putString("id", coolMessage.getObject().getId());
				data.putString("message", coolMessage.getObject().getMessage());
			}else{
				data.putString("error", coolMessage.getError().getMessage());
			}
			replyMsg.setData(data);
			try {
				clientMessenger.send(replyMsg);
			} catch (RemoteException e) {
				Log.w("CoolMessageService.sendCoolMessage",e);
			}
		}else if(coolMessage != null && clientMessenger == null){
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.successkid)
			        .setContentTitle("Cool Message Received")
			        .setContentText("Your cool message is ready to be viewed")
			        .setAutoCancel(true)
			        .setContentIntent(pIntent);
			NotificationManager mNotificationManager =
			    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Log.i("CoolMessageService","broadcasting notification");
			mNotificationManager.notify(0, mBuilder.build());
		}
	}
	private final Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what){
				case GET_COOL_MESSAGE:
					clientMessenger = msg.replyTo;
					sendCoolMessage();
					return true;
				default:
					return false;
			}
		}
	}));
	@Override
	public void onCreate() {
		Log.i("CoolMessageService.onCreate","starting cool message retrieval");
		new GetCoolMessageTask(this).execute();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("CoolMessageService","service is starting");
		return Service.START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i("CoolMessageService","binding to client");
		return messenger.getBinder();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i("CoolMessageService", "client is unbinding");
		clientMessenger = null;
		return false;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("CoolMessageService","service is terminating");
	}

	@Override
	public void onCoolMessageReceived(Either<CoolMessage> coolMessage) {
		Log.i("CoolMessageService", "received cool message: "+coolMessage.toString());
		this.coolMessage = coolMessage;
		sendCoolMessage();
	}
}
