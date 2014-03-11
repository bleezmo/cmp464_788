package com.lehman.android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int CAPTURE_IMAGE_REQUEST_CODE = 1;
	private String mediaPath;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CAPTURE_IMAGE_REQUEST_CODE){
			if(resultCode == RESULT_OK){
				ImageView iv = (ImageView) findViewById(R.id.camera_img);
				iv.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
			}else if(resultCode == RESULT_CANCELED){
				//user canceled the image capture
			}else{
				//something resulted in an error
			}
		}
	}

	public void startCamera(View view){
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	Uri mediaUri = putExtraOutput(intent);
    	if(mediaUri == null) return;
    	this.mediaPath = mediaUri.getPath();
    	startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }
    
    private Uri putExtraOutput(Intent intent){
    	File mediaStorageDir = new File(
    			getExternalStoragePublicDirectory(), //getExternalFilesDir(),
    			"MyCameraApp");
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                Toast.makeText(this, "Could not start camera", Toast.LENGTH_SHORT);
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
        Uri mediaUri = Uri.fromFile(mediaFile);
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
    	return mediaUri;
    }

	// To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.
    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.
    private File getExternalStoragePublicDirectory(){
        return Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
    }
    
    private File getExternalFilesDir(){
    	return getExternalCacheDir();
    }
    
}
