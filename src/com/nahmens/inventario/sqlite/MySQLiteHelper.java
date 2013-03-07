package com.nahmens.inventario.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {


	private static final String DATABASE_NAME = "inventario.db";
	private static final int DATABASE_VERSION = 10;
	public static final String TABLE_INVENTARIO = "inventario";
	public static final String TABLE_DATA = "data";
	public static final String TABLE_USER = "user";
	public static final String TABLE_GENERAL_DATA = "data_general";
	public static final String TABLE_APP_DATA = "app_data";
	public static final String TABLE_MEDIA = "media";
	public static final String TABLE_CHECKIN = "checkin";

	public static final String CHECKIN_COLUMN_USER 	= "user";
	public static final String CHECKIN_COLUMN_DATE 	= "time";
	public static final String CHECKIN_COLUMN_LONGITUDE 	= "longitude";
	public static final String CHECKIN_COLUMN_LATITUDE 		= "latitude";

	public static final String INVENTARIO_COLUMN_USER 	= "user";
	public static final String INVENTARIO_COLUMN_ID 	= "id";
	public static final String DATA_COLUMN_INVENTARIO 	= "inventario";
	public static final String DATA_COLUMN_KEY 			= "key";
	public static final String DATA_COLUMN_VALUE 		= "value";
	public static final String USER_COLUMN_ID 	= "id";
	public static final String USER_COLUMN_PWD 	= "pwd";
	public static final String TABLE_APP_DATA_SERVER_KEY 	= "server";
	public static final String DATA_COLUMN_TYPE 	= "type";
	public static final String DATA_COLUMN_SYNC 	= "sync";



	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_INVENTARIO + "(" + INVENTARIO_COLUMN_ID
			+ " text primary key not null, " + INVENTARIO_COLUMN_USER
			+ " text not null);";

	private static final String DATABASE_CREATE_2 = "create table "
			+ TABLE_DATA + "(" + DATA_COLUMN_INVENTARIO
			+ " text not null, " + DATA_COLUMN_KEY
			+ " text not null, " + DATA_COLUMN_VALUE
			+ " text not null);";

	private static final String DATABASE_CREATE_3 = "create table "
			+ TABLE_USER + "(" + USER_COLUMN_ID
			+ " text primary key not null, " + USER_COLUMN_PWD
			+ " text not null);";

	private static final String DATABASE_CREATE_4 = "create table "
			+ TABLE_GENERAL_DATA + "(" + DATA_COLUMN_KEY
			+ " text not null, " + DATA_COLUMN_VALUE
			+ " text not null);";


	private static final String DATABASE_CREATE_5 = "create table "
			+ TABLE_APP_DATA + "(" + DATA_COLUMN_KEY
			+ " text not null, " + DATA_COLUMN_VALUE
			+ " text not null);";


	private static final String DATABASE_CREATE_6 = "create table "
			+ TABLE_MEDIA + "(" + DATA_COLUMN_INVENTARIO
			+ " text not null, " + DATA_COLUMN_KEY
			+ " text not null, " + DATA_COLUMN_TYPE
			+ " text not null, " + DATA_COLUMN_SYNC
			+ " text not null, " + DATA_COLUMN_VALUE
			+ " BLOB not null);";

	private static final String DATABASE_CREATE_7 = "create table "
			+ TABLE_CHECKIN + "(" + CHECKIN_COLUMN_USER
			+ " text not null, " + CHECKIN_COLUMN_DATE
			+ " text not null, " + CHECKIN_COLUMN_LONGITUDE
			+ " text not null, " + CHECKIN_COLUMN_LATITUDE
			+ " text not null);";


	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE_2);
		database.execSQL(DATABASE_CREATE_3);
		database.execSQL(DATABASE_CREATE_4);
		database.execSQL(DATABASE_CREATE_5);
		database.execSQL(DATABASE_CREATE_6);
		database.execSQL(DATABASE_CREATE_7);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTARIO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GENERAL_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);

		onCreate(db);
	}

}