package com.lehman.android.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteManager extends SQLiteOpenHelper{

	  public static final String TABLE_IMAGES = "images";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_PRIORITY = "priority";
	  public static final String COLUMN_SIZE = "size";
	  public static final String COLUMN_IMAGE = "image_blob";

	  public static final String DATABASE_NAME = "images.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "+ TABLE_IMAGES + "(" + 
			  COLUMN_ID + " integer primary key autoincrement,"+ 
			  COLUMN_PRIORITY + " integer NOT NULL DEFAULT 0," + 
			  COLUMN_SIZE + " integer NOT NULL DEFAULT 0," + 
			  COLUMN_IMAGE + " none" +
			  ");";

	  public SQLiteManager(Context context) {
	    super(context, context.getCacheDir()+"/"+DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(SQLiteManager.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
	    onCreate(db);
	  }
	  
	  
}
