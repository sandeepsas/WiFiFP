/*
 * Developed by Sandeep Sasidharan
 * 10 June 2014
 * The app scans and lists the wifi access points
 * Option available to store the data in a data base
 * */
package com.example.wififp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DatabaseHandler {
	public static final String UID = "uid";
	public static final String BSSID = "bssid";
	public static final String SSID = "ssid";
	public static final String SIGNAL_LEVEL = "signal_level";
	
	public static final String TABLE_NAME = "finger_prints";
	public static final String DB_PATH = Environment.getExternalStorageDirectory().getPath();
	public static final String DATABASE_NAME = "finger_prints_db";
	public static final int DATABASE_VERSION = 1;
	
	public static final String TABLE_CREATE = "create table finger_prints (uid text,bssid text not null,ssid text,signal_level text not null);";
	
	/*Create a DB helper class*/
	DatabaseHelper dbHelper; /*Create a object DB helper class*/
	Context ctx;
	SQLiteDatabase db;
	
	public DatabaseHandler (Context ctx) /*Constructor of  DB Handler class*/
	{
		this.ctx = ctx;
		dbHelper = new DatabaseHelper(ctx); 
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/*Constructor of DatabaseHelper class*/
		public DatabaseHelper (Context ctx)
		{
			super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			/*Create the table*/
			try{
			db.execSQL(TABLE_CREATE);
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			/*Drop table if already present*/
			db.execSQL("DROP TABLE IF EXISTS finger_prints");
			onCreate(db);
		}
		
	}
	
	/*Method to open the database returns class type*/
	public DatabaseHandler open()
	{
		db = dbHelper.getWritableDatabase();
		return (this);
	}
	public void close()
	{
		dbHelper.close();
	}
	public long insertData(String uid,String bssid,String ssid,int signal_level)
	{
		ContentValues content = new ContentValues();
		content.put(UID, uid);
		content.put(BSSID, bssid);
		content.put(SSID, ssid);
		content.put(SIGNAL_LEVEL, signal_level);
		return db.insert(TABLE_NAME, null, content);
	}
	public Cursor returnData()
	{
		return db.query(TABLE_NAME, new String[]{UID,BSSID,SSID,SIGNAL_LEVEL}, null, null, null, null, null);
	}
	public void clearData()
	{
		db.delete(TABLE_NAME, null, null);
	}
}
