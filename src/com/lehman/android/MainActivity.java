package com.lehman.android;

import com.lehman.android.utils.Downloader;
import com.lehman.android.utils.Either;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity{
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncDownloader.run(this);
    }
}
