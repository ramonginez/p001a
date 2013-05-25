package com.nahmens.inventario.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nahmens.inventario.Inventario;
import com.nahmens.inventario.User;
import com.nahmens.inventario.utils.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InventarioDataSource {

	private SQLiteDatabase database;

	private MySQLiteHelper dbHelper;

	private static InventarioDataSource INSTANCE = null;


	private InventarioDataSource(Context context) {

		dbHelper = new MySQLiteHelper(context);

	}

	public static InventarioDataSource getInstance(Context context){

		if(INSTANCE==null){

			INSTANCE = new InventarioDataSource(context);
		}

		return INSTANCE;
	}

	private void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	private void close() {
		dbHelper.close();
	}

	@SuppressWarnings("unused")
	private User insertUser(String user, String pwd){

		try{

			open();

			//String id = Utils.generateId();

			ContentValues values = new ContentValues();

			values.put(MySQLiteHelper.USER_COLUMN_ID,user);

			values.put(MySQLiteHelper.USER_COLUMN_PWD,pwd);

			database.insert(MySQLiteHelper.TABLE_USER, null,values);

			return new User(user);

		}finally{
			close();
		}
	}

	public void checkin(String user, String time, String latitude, String longitude) {

		try{

			open();

			ContentValues values = new ContentValues();

			values.put(MySQLiteHelper.CHECKIN_COLUMN_USER,user);
			values.put(MySQLiteHelper.CHECKIN_COLUMN_DATE,time);
			values.put(MySQLiteHelper.CHECKIN_COLUMN_LATITUDE,latitude);
			values.put(MySQLiteHelper.CHECKIN_COLUMN_LONGITUDE,longitude);

			database.insert(MySQLiteHelper.TABLE_CHECKIN, null,values);


		}finally{
			close();
		}



	}

	public Inventario createInventario(String user) {

		try{

			open();

			String id = Utils.generateId();

			ContentValues values = new ContentValues();

			values.put(MySQLiteHelper.INVENTARIO_COLUMN_ID,id);
			values.put(MySQLiteHelper.INVENTARIO_COLUMN_USER,user);


			database.insert(MySQLiteHelper.TABLE_INVENTARIO, null,values);

			Inventario newInventario = new Inventario(id,user);

			return newInventario;

		}finally{
			close();
		}



	}

	public void deleteInventario(String id) {

		try{


			open();

			Log.i( "deleteInventario", "table "+MySQLiteHelper.TABLE_INVENTARIO +" key->"+MySQLiteHelper.INVENTARIO_COLUMN_ID+", value->"+id);

			database.delete(MySQLiteHelper.TABLE_INVENTARIO, MySQLiteHelper.INVENTARIO_COLUMN_ID
					+" ='" + id +"'", null);

			database.delete(MySQLiteHelper.TABLE_DATA, MySQLiteHelper.DATA_COLUMN_INVENTARIO
					+ " ='" + id +"'", null);

			Log.i( "deleteInventario", "table "+MySQLiteHelper.TABLE_DATA +" key->"+MySQLiteHelper.INVENTARIO_COLUMN_ID+", value->"+id);

			System.out.println("Inventario deleted with id: " + id);


		}finally{
			close();
		}



	}

	public List<String> getAllInventarios() {

		try{
			open();

			List<String> inventarios = new ArrayList<String>();

			String[] allColumns = { MySQLiteHelper.INVENTARIO_COLUMN_ID };

			Cursor cursor = database.query(MySQLiteHelper.TABLE_INVENTARIO,
					allColumns, null, null, null, null, null);

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {

				inventarios.add(cursor.getString(0));

				cursor.moveToNext();
			}

			// Make sure to close the cursor
			cursor.close();

			return inventarios;


		}finally{
			close();
		}



	}

	public void setInventario(Inventario inventario) {

		try{

			open();

			/*InsertHelper ih = new InsertHelper(database, MySQLiteHelper.TABLE_DATA);

			// Get the numeric indexes for each of the columns that we're updating
			final int col1 = ih.getColumnIndex(MySQLiteHelper.DATA_COLUMN_KEY);
			final int col2 = ih.getColumnIndex(MySQLiteHelper.DATA_COLUMN_VALUE);
			final int col3 = ih.getColumnIndex(MySQLiteHelper.DATA_COLUMN_INVENTARIO);*/


			database.delete(MySQLiteHelper.TABLE_DATA, MySQLiteHelper.DATA_COLUMN_INVENTARIO
					+ " ='" + inventario.getId()+"'", null);

			HashMap<String,String>invData = inventario.getData();

			Log.i( "setInventario", "invData:"+invData.size());

			Iterator<Map.Entry<String,String>> it = invData.entrySet().iterator();

			while (it.hasNext()) {


				Map.Entry<String,String> pairs = (Entry<String, String>)it.next();
				String key = pairs.getKey();
				String value = pairs.getValue();

				Log.i( "setInventario", "key->"+key+", value->"+value+", id->"+inventario.getId());

				/*ih.prepareForInsert();

				ih.bind(col1, key);
				ih.bind(col2, value);
				ih.bind(col3, inventario.getId());

				// Insert the row into the database.
				ih.execute();*/

				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.DATA_COLUMN_KEY, key);
				values.put(MySQLiteHelper.DATA_COLUMN_VALUE, value);
				values.put(MySQLiteHelper.DATA_COLUMN_INVENTARIO, inventario.getId());

				database.insert(MySQLiteHelper.TABLE_DATA, null,values);



				it.remove(); // avoids a ConcurrentModificationException
			}

			setInventarioMedia(inventario.getId(),inventario.getAudioMedia(), "audio");

			setInventarioMedia(inventario.getId(),inventario.getImageMedia(), "img");

			Log.i( "setInventario", "set ends");


		}finally{
			close();
		}





	}

	public List<String> getSavedAttributes(String key) {

		try{

			if(key == null){

				return null;
			}

			open();

			String[] allColumns = {  MySQLiteHelper.DATA_COLUMN_VALUE };

			Cursor cursor = database.query(MySQLiteHelper.TABLE_DATA,
					allColumns, MySQLiteHelper.DATA_COLUMN_KEY + " ='" + key+"'", null, null, null, null);

			cursor.moveToFirst();

			List<String> list = new ArrayList<String>();

			while (!cursor.isAfterLast()) {

				list.add(cursor.getString(0));

				cursor.moveToNext();
			}

			cursor.close();

			return list;


		}finally{
			close();
		}







	}

	public Inventario getInventario(String id) {


		try{

			open();

			String[] allColumns = { MySQLiteHelper.INVENTARIO_COLUMN_ID,MySQLiteHelper.INVENTARIO_COLUMN_USER };

			Cursor cursor2 = database.query(MySQLiteHelper.TABLE_INVENTARIO,
					allColumns, MySQLiteHelper.INVENTARIO_COLUMN_ID + " ='" + id+"'", null, null, null, null);

			cursor2.moveToFirst();

			Inventario inv = null;

			if (!cursor2.isAfterLast()) {

				inv = new Inventario(cursor2.getString(0), cursor2.getString(1));

			}else{

				return null;
			}

			cursor2.close();

			String[] allColumns2 = { MySQLiteHelper.DATA_COLUMN_KEY, MySQLiteHelper.DATA_COLUMN_VALUE };

			HashMap<String,String> mp = new HashMap<String,String>();

			Cursor cursor = database.query(MySQLiteHelper.TABLE_DATA,
					allColumns2, MySQLiteHelper.DATA_COLUMN_INVENTARIO + " = '" + id+"'", null, null, null, null);

			cursor.moveToFirst();

			Log.i( "getInventario", "search");

			while (!cursor.isAfterLast()) {

				Log.i( "getInventario", "cursor.getString(0)->"+cursor.getString(0) 
						+", cursor.getString(1)->" +cursor.getString(1));

				mp.put(cursor.getString(0), cursor.getString(1));

				cursor.moveToNext();
			}
			Log.i( "getInventario", "end search");


			cursor.close();

			inv.setData(mp);

			getInventarioMedia(inv);

			return inv;


		}finally{

			close();
		}

	}

	public User getUser(String id, String password) {

		try{

			open();

			String[] allColumns = { MySQLiteHelper.USER_COLUMN_ID, MySQLiteHelper.USER_COLUMN_PWD };

			Cursor cursor = database.query(MySQLiteHelper.TABLE_USER,
					allColumns, MySQLiteHelper.USER_COLUMN_ID + " ='" + id+"'", null, null, null, null);

			if(cursor==null){

				return null;
			}

			cursor.moveToFirst();

			Log.i( "getUser", "search");

			User user = null;

			while (!cursor.isAfterLast()) {

				String dbUid = cursor.getString(0);

				String dbPwd = cursor.getString(1);

				if(dbUid.equals(id)&&dbPwd.equals(password)){

					user = new User(dbUid);

					user.setPassword(password);
				}

				cursor.moveToNext();
			}

			cursor.close();

			return user;


		}finally{

			close();
		}
	}




	public void setSYNCServer(String server) {

		try{

			open();


			database.delete(MySQLiteHelper.TABLE_CHECKIN, MySQLiteHelper.DATA_COLUMN_KEY
					+ " ='" + MySQLiteHelper.TABLE_APP_DATA_SERVER_KEY+"'", null);


			Log.i( "setSYNCServer", "setSYNCServer");

			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.DATA_COLUMN_KEY, MySQLiteHelper.TABLE_APP_DATA_SERVER_KEY);
			values.put(MySQLiteHelper.DATA_COLUMN_VALUE, server);

			database.insert(MySQLiteHelper.TABLE_APP_DATA, null,values);

			Log.i( "setSYNCServer", "set ends");


		}finally{
			close();
		}

	}

	public String getSyncServer() {

		try{

			open();

			String[] allColumns = { MySQLiteHelper.DATA_COLUMN_VALUE };

			Cursor cursor = database.query(MySQLiteHelper.TABLE_APP_DATA,
					allColumns, MySQLiteHelper.DATA_COLUMN_KEY + " ='" + MySQLiteHelper.TABLE_APP_DATA_SERVER_KEY+"'", null, null, null, null);

			if(cursor==null){

				return null;
			}

			String server = "";

			Log.i( "getSyncServer", "search");

			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {

				server = cursor.getString(0);

				cursor.moveToNext();
			}

			cursor.close();

			return server;

		}finally{

			close();
		}
	}

	public void setUserTable(JSONArray userArray) throws JSONException {

		if(userArray==null){

			return;
		}

		try{

			open();

			//database.beginTransaction();

			database.delete(MySQLiteHelper.TABLE_USER, null, null);

			for (int i = 0; i < userArray.length(); ++i) {


				JSONObject userObject = userArray.getJSONObject(i);

				String uid = userObject.getString(InventarioControllerImpl.SERVER_RESPONSE_USER_ID_KEY);

				String pwd = userObject.getString(InventarioControllerImpl.SERVER_RESPONSE_USER_PWD_KEY);

				Log.i( "setUserTable", "id:"+uid +", pwd:"+pwd);

				ContentValues values = new ContentValues();

				values.put(MySQLiteHelper.USER_COLUMN_ID,uid);

				values.put(MySQLiteHelper.USER_COLUMN_PWD,pwd);

				database.insert(MySQLiteHelper.TABLE_USER, null,values);

				Log.i( "setUserTable", "inserted!");


			}

			//database.endTransaction();

			//database.setTransactionSuccessful();

		}finally{

			close();
		}
	}

	public void setCampoTable(JSONArray campoArray) throws JSONException {

		if(campoArray==null){

			return;
		}

		try{

			open();

			//database.beginTransaction();

			database.delete(MySQLiteHelper.TABLE_CAMPO, null, null);

			for (int i = 0; i < campoArray.length(); ++i) {


				JSONObject campoObject = campoArray.getJSONObject(i);

				String id = campoObject.getString(InventarioControllerImpl.SERVER_RESPONSE_CAMPO_ID_KEY);
						
				String value = campoObject.getString(InventarioControllerImpl.SERVER_RESPONSE_CAMPO_VALUE_KEY);
			
				Log.i( "setCampoTable", "id:"+id +", pwd:"+value);

				ContentValues values = new ContentValues();

				values.put(MySQLiteHelper.CAMPO_COLUMN_ID,id);

				values.put(MySQLiteHelper.CAMPO_COLUMN_VALOR,value);

				database.insert(MySQLiteHelper.TABLE_CAMPO, null,values);

				Log.i( "setCampoTable", "inserted!");


			}

			//database.endTransaction();

			//database.setTransactionSuccessful();

		}finally{

			close();
		}
	}

	private void setInventarioMedia(String id, List<byte[]> media, String type) {

		try{

			int currentSavedMedia = getCurrentMedia(id,type);

			open();


			/*database.delete(MySQLiteHelper.TABLE_MEDIA, MySQLiteHelper.DATA_COLUMN_INVENTARIO
					+ " ='"  + id+"'" 
					+ " AND " + MySQLiteHelper.DATA_COLUMN_TYPE+" ='"+type+"'", null);*/

			int count = 0;

			for(byte[] value : media){

				if(count>=currentSavedMedia){

					String key = String.valueOf(count);

					ContentValues values = new ContentValues();
					values.put(MySQLiteHelper.DATA_COLUMN_KEY, key);
					values.put(MySQLiteHelper.DATA_COLUMN_VALUE, value);
					values.put(MySQLiteHelper.DATA_COLUMN_INVENTARIO, id);
					values.put(MySQLiteHelper.DATA_COLUMN_TYPE, type);
					values.put(MySQLiteHelper.DATA_COLUMN_SYNC, "0");
					database.insert(MySQLiteHelper.TABLE_MEDIA, null,values);

				}

				count++;

			}


		}finally{
			close();
		}

	}


	private void getInventarioMedia(Inventario inventario) {


		try{

			open();

			String[] allColumns2 = { MySQLiteHelper.DATA_COLUMN_TYPE, MySQLiteHelper.DATA_COLUMN_VALUE ,MySQLiteHelper.DATA_COLUMN_SYNC};

			List<byte[]> imgMedia = new ArrayList<byte[]>();

			List<byte[]> audioMedia = new ArrayList<byte[]>();

			Cursor cursor = database.query(MySQLiteHelper.TABLE_MEDIA,
					allColumns2, MySQLiteHelper.DATA_COLUMN_INVENTARIO + " = '" + inventario.getId()+"'", null, null, null, null);

			cursor.moveToFirst();

			Log.i( "getInventarioMedia", "search");

			while (!cursor.isAfterLast()) {

				Log.i( "getInventarioMedia", "sync:"+cursor.getInt(2));

				String type = cursor.getString(0);

				byte[] data = cursor.getBlob(1);

				if(type.equals("img")){

					imgMedia.add(data);
				}
				else if(type.equals("audio")){

					audioMedia.add(data);
				}

				cursor.moveToNext();
			}


			cursor.close();

			inventario.setAudioMedia(audioMedia);

			inventario.setImageMedia(imgMedia);


		}finally{

			close();
		}

	}

	public int getCurrentMedia(String id , String type) {

		try{

			open();

			String[] allColumns2 = { MySQLiteHelper.DATA_COLUMN_INVENTARIO};

			Cursor cursor = database.query( MySQLiteHelper.TABLE_MEDIA,
					allColumns2, MySQLiteHelper.DATA_COLUMN_INVENTARIO + " = '" + id+"' AND "
							+ MySQLiteHelper.DATA_COLUMN_TYPE+ " = '"+type+"'", null, null, null, null);

			cursor.moveToFirst();

			Log.i( "getCurrentMedia", "search");

			int count = 0;

			while (!cursor.isAfterLast()) {
				cursor.moveToNext();
				count++;
			}

			cursor.close();

			return  count;

		}finally{

			close();
		}	

	}

	public List<byte[]> getNoSyncMedia(String id , String type) {

		try{

			open();

			String[] allColumns2 = { MySQLiteHelper.DATA_COLUMN_TYPE, MySQLiteHelper.DATA_COLUMN_VALUE, MySQLiteHelper.DATA_COLUMN_SYNC };

			List<byte[]> media = new ArrayList<byte[]>();

			Cursor cursor = database.query( MySQLiteHelper.TABLE_MEDIA,
					allColumns2, MySQLiteHelper.DATA_COLUMN_INVENTARIO + " = '" + id+"' AND "
							+ MySQLiteHelper.DATA_COLUMN_SYNC + "='0' AND "
							+ MySQLiteHelper.DATA_COLUMN_TYPE+ " = '"+type+"'", null, null, null, null);

			cursor.moveToFirst();

			Log.i( "getInventarioMedia", "search");

			int count = 0;

			while (!cursor.isAfterLast()) {

				Log.i( "getNoSyncMedia", type+" count"+count+"sync:"+cursor.getInt(2));

				byte[] data = cursor.getBlob(1);

				media.add(data);

				cursor.moveToNext();
				count++;
			}

			cursor.close();

			return  media;

		}finally{

			close();
		}

	}
	/*
	public void setSyncMedia(Inventario inventario) {

		try{

			open();


			database.delete(MySQLiteHelper.TABLE_MEDIA, MySQLiteHelper.DATA_COLUMN_INVENTARIO
					+ " ='"  + inventario.getId()+"'", null);

			int count = 0;

			for(byte[] value : inventario.getAudioMedia()){

				String key = String.valueOf(count);

				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.DATA_COLUMN_KEY, key);
				values.put(MySQLiteHelper.DATA_COLUMN_VALUE, value);
				values.put(MySQLiteHelper.DATA_COLUMN_INVENTARIO, inventario.getId());
				values.put(MySQLiteHelper.DATA_COLUMN_TYPE, "audio");
				values.put(MySQLiteHelper.DATA_COLUMN_SYNC, "1");
				database.insert(MySQLiteHelper.TABLE_MEDIA, null,values);

				count++;

			}

			count = 0;

			for(byte[] value : inventario.getImageMedia()){

				String key = String.valueOf(count);

				ContentValues values = new ContentValues();
				values.put(MySQLiteHelper.DATA_COLUMN_KEY, key);
				values.put(MySQLiteHelper.DATA_COLUMN_VALUE, value);
				values.put(MySQLiteHelper.DATA_COLUMN_INVENTARIO, inventario.getId());
				values.put(MySQLiteHelper.DATA_COLUMN_TYPE, "img");
				values.put(MySQLiteHelper.DATA_COLUMN_SYNC, "1");
				database.insert(MySQLiteHelper.TABLE_MEDIA, null,values);

				count++;

			}



		}finally{
			close();
		}

	}*/



	public  void setSyncMedia(Inventario inventario) {

		try{

			Log.i( "setSyncMedia", "setSyncMedia:"+inventario.getId());

			open();

			ContentValues cv=new ContentValues();

			cv.put(MySQLiteHelper.DATA_COLUMN_SYNC, "1");

			String whereClause = MySQLiteHelper.DATA_COLUMN_INVENTARIO + " =?";
			String[] whereArgs = new String[]{ String.valueOf(inventario.getId()) };

			database.update(MySQLiteHelper.TABLE_MEDIA,cv, whereClause, whereArgs);

			getInventarioMedia(inventario);


		}finally{

			close();
		}

	}

	public JSONArray getCheckin() throws JSONException {


		try{

			open();

			String[] allColumns2 = { MySQLiteHelper.CHECKIN_COLUMN_USER, MySQLiteHelper.CHECKIN_COLUMN_DATE, 
					MySQLiteHelper.CHECKIN_COLUMN_LATITUDE,MySQLiteHelper.CHECKIN_COLUMN_LONGITUDE };

			JSONArray jArray= new JSONArray();

			Cursor cursor = database.query( MySQLiteHelper.TABLE_CHECKIN,
					allColumns2, null, null, null, null, null);

			cursor.moveToFirst();

			Log.i( "getCheckin", "search");

			while (!cursor.isAfterLast()) {

				JSONObject row = new JSONObject();

				row.put(MySQLiteHelper.CHECKIN_COLUMN_USER, cursor.getString(0));
				row.put(MySQLiteHelper.CHECKIN_COLUMN_DATE, cursor.getString(1));
				row.put(MySQLiteHelper.CHECKIN_COLUMN_LATITUDE, cursor.getString(2));
				row.put(MySQLiteHelper.CHECKIN_COLUMN_LONGITUDE, cursor.getString(3));

				jArray.put(row);

				cursor.moveToNext();

			}

			cursor.close();

			return  jArray;

		}finally{

			close();
		}

	}
	
	public void emptyCheckin() {
		
		try{

			open();


			database.delete(MySQLiteHelper.TABLE_CHECKIN, null, null);


			Log.i( "emptyCheckin", "set ends");


		}finally{
			close();
		}
	}

	public HashMap<String, List<String>> getAutoComplete() {
		try{

			open();

			String[] allColumns2 = { MySQLiteHelper.CAMPO_COLUMN_ID, MySQLiteHelper.CAMPO_COLUMN_VALOR};

			HashMap<String, List<String>> map = new HashMap<String,List<String>>();

			Cursor cursor = database.query( MySQLiteHelper.TABLE_CAMPO,
					allColumns2, null, null, null, null, null);

			cursor.moveToFirst();

			Log.i( "getCamposAutoComplete", "search");

			while (!cursor.isAfterLast()) {

				String id = cursor.getString(0);
				
				String value = cursor.getString(1);
				
				List<String> values = map.get(id);
				
				if(values==null){
					
					values = new ArrayList<String>();
				}
				
				values.add(value);
				
				map.put(id, values);
				
				cursor.moveToNext();

			}

			cursor.close();

			return  map;

		}finally{

			close();
		}
	}

}


