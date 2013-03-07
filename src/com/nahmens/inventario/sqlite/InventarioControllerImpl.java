package com.nahmens.inventario.sqlite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.nahmens.inventario.Inventario;
import com.nahmens.inventario.InventarioController;
import com.nahmens.inventario.User;

public class  InventarioControllerImpl implements InventarioController {


	private InventarioDataSource dataSource;

	public static final String SERVER_RESPONSE_USER_LIST_KEY = "users";
	public static final String SERVER_RESPONSE_USER_ID_KEY = "id";
	public static final String SERVER_RESPONSE_USER_PWD_KEY = "pwd";
	private static final String DEFAULT_SERVER = "http://vasa.zentyal.me/webserver/";
	//private static final String DEFAULT_SERVER = "http://200.8.215.209/webserver/";

	public InventarioControllerImpl(Context context){

		dataSource = InventarioDataSource.getInstance(context);
	}

	@Override
	public Inventario createNewInventario(String user) {

		return dataSource.createInventario(user);

	}	

	@Override
	public void saveInventario(Inventario inventario) {

		HashMap<String,String>invData = inventario.getData();

		Date now = new Date();

		invData.put(Inventario.LAST_SAVED, String.valueOf(now.getTime()));

		dataSource.setInventario(inventario);		

	}

	@Override
	public List<String> getSavedAttributes(String key) {

		return dataSource.getSavedAttributes(key);

	}


	@Override
	public List<String> getInventarioIds() {

		return dataSource.getAllInventarios();


	}


	@Override
	public Inventario getInventario(String id) {

		return dataSource.getInventario(id);


	}


	@Override
	public void deleteInventario(String id) {

		dataSource.deleteInventario(id);		


	}

	@Override
	public User getUserById(String id, String password) {

		return dataSource.getUser( id, password);
	}

	@Override
	public String getSyncServer() {

		return dataSource.getSyncServer();
	}

	@Override
	public void setSyncServer(String server) {

		dataSource.setSYNCServer(server);
	}


	@Override
	public void syncServer() throws Exception {

		JSONObject object = getValuesFromServer();

		JSONArray userArray = object.getJSONArray(SERVER_RESPONSE_USER_LIST_KEY);

		dataSource.setUserTable(userArray);

	}

	private JSONObject getValuesFromServer() throws Exception {

		return getValuesFromServer(null,false);
	}

	private JSONObject getValuesFromServer(JSONObject object, boolean isInventrio) throws Exception {

		JSONObject jObject;
		String server = DEFAULT_SERVER;
		try{

			Log.e("log_tag ", "getValuesFromServer: start");

			server = dataSource.getSyncServer();

			if(server==null||server.equals("")){

				server = DEFAULT_SERVER;
			}

			Log.e("log_tag ", "getValuesFromServer: using server:"+server);

			if(isInventrio){

				server = server + "syncInventario.php";

			}else{

				server = server + "mobilesync.php";

			}

			Log.e("log_tag ", "getValuesFromServer: using server path:"+server);

			Log.e("log_tag ", "Server:"+ server);

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(server);//http://192.168.1.100:8888/webserver/users.php"

			if(object!=null){

				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("inventario", object.toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				Log.e("log_tag ", "parameters:"+ nameValuePairs.get(0));

			}

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			Log.e("log_tag ", "response:"+ sb.toString());

			is.close();

			jObject = new JSONObject(sb.toString());


		}catch(Exception e){

			Log.e("log_tag ", "Error in http connection. "+server+ "--"+ e.toString());

			throw e;
		}

		return jObject;

	}

	private JSONObject getValuesFromServerMP(JSONArray checkin, JSONObject inventario, List<byte[]>audio,List<byte[]>img) throws Exception {

		JSONObject jObject;
		String server = DEFAULT_SERVER;
		try{

			Log.e("log_tag ", "getValuesFromServer: start");

			server = dataSource.getSyncServer();

			if(server==null||server.equals("")){

				server = DEFAULT_SERVER;
			}

			Log.e("log_tag ", "getValuesFromServer: using server:"+server);

			server = server + "uploadInventario.php";

			Log.e("log_tag ", "getValuesFromServer: using server path:"+server);

			Log.e("log_tag ", "Server:"+ server);


			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(server);
			MultipartEntity mp = new MultipartEntity();

			String id = inventario.getString("id")+"_";

			Log.e("log_tag ", "Inventario id antes de enviar:"+ id);

			if(audio!=null){


				for(byte[] data : audio){
					
					UUID uid = UUID.randomUUID();
					
					String fId = "audio-"+ uid;

					File exporFile = File.createTempFile(id, ".mp3");
					exporFile.deleteOnExit();
					FileOutputStream fos = new FileOutputStream(exporFile);
					fos.write(data);
					fos.close();

					mp.addPart(fId, new FileBody(exporFile));

					

				}

			}		


			if(img!=null){


				for(byte[] data : img){

					UUID uid = UUID.randomUUID();
					
					String fId = "img-"+ uid ;

					File exporFile = File.createTempFile(id, ".png");
					exporFile.deleteOnExit();
					FileOutputStream fos = new FileOutputStream(exporFile);
					fos.write(data);
					fos.close();

					mp.addPart(fId, new FileBody(exporFile));

				}

			}	

			if(inventario!=null){

				File exporFile = File.createTempFile(id, ".json");
				exporFile.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(exporFile);
				fos.write(inventario.toString().getBytes());
				fos.close();

				mp.addPart("inventario", new FileBody(exporFile));

			}
			
			if(checkin!=null){

				File exporFile = File.createTempFile("checkin", ".json");
				exporFile.deleteOnExit();
				FileOutputStream fos = new FileOutputStream(exporFile);
				fos.write(checkin.toString().getBytes());
				fos.close();

				mp.addPart("checkin", new FileBody(exporFile));

			}


			httppost.setEntity(mp);

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			InputStream is = entity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			Log.e("log_tag ", "response:"+ sb.toString());

			is.close();

			jObject = new JSONObject(sb.toString());


		}catch(Exception e){

			Log.e("log_tag ", "Error in http connection. "+server+ "--"+ e.toString());

			throw e;
		}

		return jObject;

	}

	@Override
	public void syncInventario(String id) throws Exception {

		Log.e("syncInventario ", "entro");

		Inventario inventario = dataSource.getInventario(id);	

		JSONArray jCheck =  dataSource.getCheckin();
		
		HashMap<String,String> data = inventario.getData();

		boolean sentOk = sendInventario(inventario,jCheck);

		if(sentOk){

			String lastSaved = data.get(Inventario.LAST_SAVED);

			data.put(Inventario.LAST_SYNC, lastSaved);

			dataSource.setInventario(inventario);	
			
			dataSource.emptyCheckin();

		}


	}

	private  boolean  sendInventario(Inventario inventario, JSONArray jCheck ) throws Exception {

		HashMap<String, String> data = inventario.getData();

		String id = inventario.getId();

		JSONObject inventarioObj = new JSONObject();

		JSONObject jData = new JSONObject();

		inventarioObj.put("id", id);
		
		@SuppressWarnings("rawtypes")
		Iterator it = data.entrySet().iterator();


		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();

			Log.e("log_tag ", "pairs.getValue(): "+pairs.getValue());

			if(pairs.getValue()!=null&&!pairs.getValue().equals("")){

				Log.e("log_tag ", "entro");

				jData.put((String) pairs.getKey(), pairs.getValue());

			}

			//it.remove(); // avoids a ConcurrentModificationException
		}		

		jData.put("user", inventario.getUser());


		inventarioObj.put("data", jData);

		inventarioObj.put("id", id);

		List<byte[]> audioList = dataSource.getNoSyncMedia( id , "audio");

		List<byte[]> imgList = dataSource.getNoSyncMedia( id , "img");

		JSONObject result = getValuesFromServerMP(jCheck,inventarioObj,audioList,imgList);

		String code = (String) result.get("err");

		if(code.equals("0")){

			dataSource.setSyncMedia(inventario);

			return true;

		}

		return false;

	}

	@Override
	public void checkin(String user, String time, String latitude,
			String longitude) throws Exception {

		dataSource.checkin(user, time, latitude, longitude);		
	}



}
