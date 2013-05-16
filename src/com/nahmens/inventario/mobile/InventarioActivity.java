package com.nahmens.inventario.mobile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nahmens.inventario.Inventario;
import com.nahmens.inventario.mobile.R;
import com.nahmens.inventario.sqlite.InventarioControllerImpl;

@SuppressLint("ParserError")
public class InventarioActivity extends Activity {

	final Context context = this;

	public final static int RETURN_PICTURE_CODE = 200;
	public final static int RETURN_AUDIO_CODE = 400;

	private static InventarioControllerImpl inventarioController;
	private ImageButton buttonPicture;
	private String path;
	private String audioPath;
	private LinearLayout imagesContainer;
	private List<byte[]> imgList = new ArrayList<byte[]>();
	private LinearLayout audioContainer;
	private List<byte[]> audioList = new ArrayList<byte[]>();
	private Inventario inventario = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_inventario);

		Bundle bundle = getIntent().getExtras();

		String inventarioId = (String) bundle.get(InventariosActivity.PROPERTY_KEY);

		inventarioController = new com.nahmens.inventario.sqlite.InventarioControllerImpl(this);

		inventario = inventarioController.getInventario(inventarioId);

		setPicture();

		setAudio();

		setGuardar();

		setProject();


	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	 
	   //Do nothing
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.i( "onActivityResult", "resultCode: " + resultCode );

		switch( requestCode ){

		case RETURN_PICTURE_CODE: 

			if(resultCode==-1){

				onPhotoTaken();

			}

			break;



		case RETURN_AUDIO_CODE: 


			if(data!=null){

				onAudio(data);

			}


			break;

		}

	}




	/*******************************************************************************
	 * 
	 * 			PICTURE
	 * 
	 * */

	private void setPicture() {

		imagesContainer =  (LinearLayout)findViewById( R.id.images ); 

		buttonPicture = (ImageButton) findViewById(R.id.picBtn);

		buttonPicture.setOnClickListener( new ButtonPictureClickHandler() );


	}

	public class ButtonPictureClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "ButtonPictureClickHandler", "onClick()" );

			UUID id = UUID.randomUUID();

			path = Environment.getExternalStorageDirectory() +"/"+id.toString()+".jpg";

			File file = new File( path );

			Uri outputFileUri = Uri.fromFile( file );
			
			try { 
				
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
				intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

				startActivityForResult( intent, RETURN_PICTURE_CODE );
				
			} catch (ActivityNotFoundException e) {
				
				showNoSupportedDialog();
				
			}

		}

	}


	protected void onPhotoTaken()
	{

		Log.i( "onPhotoTaken", "onPhotoTaken()");

		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = 4;

		try{

			ByteArrayOutputStream baos = new ByteArrayOutputStream();  

			Bitmap bitmap = BitmapFactory.decodeFile( path, options );
			
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);   

			byte[] photo = baos.toByteArray(); 

			ImageView image = new ImageView(this); 

	        Bitmap bitmap2 = BitmapFactory.decodeByteArray(photo, 0, photo.length);

			image.setImageBitmap(bitmap2);

			imagesContainer.addView(image, 200, 200); 

			imgList.add(photo);
			
		
		}catch (Exception e){

			Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Error:"+e.getCause(),Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Error:"+e.getStackTrace(),Toast.LENGTH_LONG).show();

		}
	}

	private void setImgOnDisplay(byte[] photo) {
		
		ImageView image = new ImageView(this); 

        Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

		image.setImageBitmap(bitmap);

		imagesContainer.addView(image, 200, 200); 

		imgList.add(photo);

		
	}




	/*******************************************************************************
	 * 
	 * 			AUDIO
	 * 
	 * */



	private void setAudio() {

		audioContainer = (LinearLayout)findViewById( R.id.audio );

		ImageButton buttonRecord = (ImageButton)findViewById(R.id.audioBtn);

		buttonRecord.setOnClickListener( new ButtonAudioClickHandler() );


	}


	public class ButtonAudioClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "AudioMachine", "startAudioActivity()" );

			try { 
				
				Intent intent =
						new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

				startActivityForResult(intent, RETURN_AUDIO_CODE);
				
			} catch (ActivityNotFoundException e) {
				
				showNoSupportedDialog();
				
			}
		
		}


	}

	private void onAudio(Intent data) {

		Log.i( "MakeMachine", "onAudio()");

		try{

			Uri audioUri = data.getData();

			InputStream in = getContentResolver().openInputStream(audioUri); 

			byte[] binaryData = readBytes(in);
			
			setDisplayAudio(binaryData);


		}catch (Exception e){

			Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Error:"+e.getCause(),Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Error:"+e.getStackTrace(),Toast.LENGTH_LONG).show();

		}


	}


	private void setDisplayAudio(byte[] binaryData) {

		ImageView imgView=new ImageView(this);

		imgView.setBackgroundResource(R.drawable.speaker);

		audioContainer.addView(imgView, 100, 100); 

		audioList.add(binaryData);

		AudioClickHandler audioClickHandler = new AudioClickHandler(binaryData);

		imgView.setOnClickListener( audioClickHandler);		
	}







	/*******************************************************************************
	 * 
	 * 			 fields
	 * 
	 * */	
	private void setText(HashMap<String, String> data, int mainFormProyectoId,
			String id) {

		EditText et = (EditText)findViewById(mainFormProyectoId ); 

		String value = data.get(id);

		if(value!=null){

			et.setText(value);		

		}

	}


	/*******************************************************************************
	 * 
	 * 			Guardar
	 * 
	 * */	
	private void setGuardar() {

		Button buttonSave = (Button) findViewById(R.id.GUARDAR);

		buttonSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				EditText et = (EditText)findViewById(R.id.MAIN_FORM_NOMBRE_ID ); 

				String nombre = et.getText().toString();

				if(nombre==null||nombre.length()==0){
					
					String msg = "Debe incluir el nombre del proyecto";
					
					Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_LONG).show();
					
				}else{
					
					save();

					inventarioController.saveInventario(inventario);

					setResult(1,null);	

					inventario = null;
					finish();

				}
				
			}


		});

	}

	private void setProject() {


		HashMap<String,String>data = inventario.getData();

		if(data!=null){

			TextView tv1 = (TextView)findViewById(R.id.MAIN_FORM_PROYECTO_PLANTA_ID ); 

			String value = data.get(Inventario.PROYECTO);

			value = value + ","+ data.get(Inventario.NOMBRE)+":";

			if(value!=null){

				tv1.setText(value);		

			}

			//setText(data, R.id.MAIN_FORM_PROYECTO_ID , Inventario.PROYECTO);
			//setText(data, R.id.MAIN_FORM_PLANTA_ID , Inventario.PLANTA);

			setText(data, R.id.MAIN_FORM_AREA_ID , Inventario.AREA);
			setText(data, R.id.MAIN_FORM_EDIFICIO_ID , Inventario.EDIFICIO);
			setText(data, R.id.MAIN_FORM_DEPARTAMENTO_ID , Inventario.DEPARTAMENTO);
			setText(data, R.id.MAIN_FORM_PISO_ID , Inventario.PISO);
			setText(data, R.id.MAIN_FORM_NOMBRE_ID , Inventario.NOMBRE);
			setText(data, R.id.MAIN_FORM_CODIGO_VASA_ID , Inventario.CODIGO_VASA);
			setText(data, R.id.MAIN_FORM_CODIGO_CLIENTE_ID , Inventario.CODIGO_CLIENTE);
			setText(data, R.id.MAIN_FORM_MARCA_ID , Inventario.MARCA);
			setText(data, R.id.MAIN_FORM_MODELO_ID , Inventario.MODELO);
			setText(data, R.id.MAIN_FORM_SERIAL_ID , Inventario.SERIAL);
			setText(data, R.id.MAIN_FORM_TIPO_ID , Inventario.TIPO);
			setText(data, R.id.MAIN_FORM_FABRICANTE_ID , Inventario.FABRICANTE);
			setText(data, R.id.MAIN_FORM_PAIS_ID , Inventario.PAIS_ORIGEN);
			setText(data, R.id.MAIN_FORM_CODIGO_PLANTA_ID , Inventario.CODIGO_PLANTA);
			setText(data, R.id.MAIN_FORM_CAPACIDAD_ID , Inventario.CAPACIDAD);
			setText(data, R.id.MAIN_FORM_DIMENSIONES_PUL_ID , Inventario.DIMENDIONES);
			setText(data, R.id.MAIN_FORM_DIMENSIONES_L_ID , Inventario.DIMENDIONES_L);
			setText(data, R.id.MAIN_FORM_DIMENSIONES_A_ID , Inventario.DIMENDIONES_A);
			setText(data, R.id.MAIN_FORM_DIMENSIONES_H_ID , Inventario.DIMENDIONES_H);
			setSpinner(data, R.id.ACCIONADO_LIST_ID,R.array.ACCIONADO_POR,Inventario.ACCIONADO_LIST);
			setText(data, R.id.MAIN_FORM_ACCIONADO_DE_ID , Inventario.ACCIONADO_DE);
			setRadio(data, R.id.ACCIONADO_RADIO_ID,Inventario.ACCIONADO_RADIO);
			setText(data, R.id.MAIN_FORM_ACCIONADO_POR_ID , Inventario.ACCIONADO_POR);
			setText(data, R.id.COMPLETO_ID , Inventario.COMPLETO);
			setSpinner(data, R.id.RECUBIERTO_CON_ID,R.array.RECUBIERTO_CON,Inventario.RECUBIERTO_CON);
			setSpinner(data, R.id.CONSTRUIDO_CON_ID,R.array.CONSTRUIDO_CON,Inventario.CONSTRUIDO_CON);
			setText(data, R.id.CONSTRUIDO_E_ID , Inventario.CONSTRUIDO_E);
			setSpinner(data, R.id.MONTADO_SOBRE_ID,R.array.MONTADO_SOBRE,Inventario.MONTADO_SOBRE);
			setSpinner(data, R.id.CONDICION_ID,R.array.CONDICION_VALUE,Inventario.CONDICION);
			setSpinner(data, R.id.EJE_BOMBA_ID,R.array.EJE_BOMBA,Inventario.EJE_BOMBA);

			setRadio(data, R.id.FUNCIONANDO_ID,Inventario.FUNCIONANDO);
			setText(data, R.id.FLUIDO_ID , Inventario.FLUIDO);
			setText(data, R.id.ESTIMADO_ID , Inventario.ESTIMADO);
			setText(data, R.id.FLUIDO_ENTRADA_ID , Inventario.FLUIDO_ENTRADA);
			setText(data, R.id.FLUIDO_SALIDA_ID , Inventario.FLUIDO_SALIDA);

			setFoto(inventario.getImageMedia());
			setAudio(inventario.getAudioMedia());


		}

	}

	private void setRadio(HashMap<String, String> data, int rid,
			String dataId) {

		String value = data.get(dataId);

		if(value!=null){

			RadioGroup radioGroup = (RadioGroup)findViewById(rid); 

			int count = radioGroup.getChildCount();

			for (int i=0;i<count;i++) {

				RadioButton rb = (RadioButton) radioGroup.getChildAt(i);

				String rbValue = (String) rb.getText();

				if(rbValue.equals(value)){

					radioGroup.check(rb.getId());

					return;
				}

			}

		}


	}



	private void setSpinner(HashMap<String, String> data, int listId, int rId, String dataId) {

		Resources res = getResources();

		Spinner sp = (Spinner)findViewById(listId ); 

		String value = data.get(dataId);

		if(value!=null){

			String[] array = res.getStringArray(rId);

			int pos = 0 ;

			for(String selectedValue : array){

				if(selectedValue.equals(value)){

					sp.setSelection(pos);

					return;
				}
				pos++;
			} 


		}

	}



	private void setFoto(List<byte[]> media) {

		if(media!=null){

			for(byte[] img:media){
				
				setImgOnDisplay(img);
			
			}

		}

	}

	private void setAudio(List<byte[]> media) {

		if(media!=null){

			for(byte[] audio:media){

				setDisplayAudio(audio);

			}

		}

	}


	public void save() {

		HashMap<String,String> data = inventario.getData();

		if(data==null){

			data = new HashMap<String,String>();

		}
		

		//	saveData(data, R.id.MAIN_FORM_PROYECTO_ID , Inventario.PROYECTO);
		//	saveData(data, R.id.MAIN_FORM_PLANTA_ID , Inventario.PLANTA);

		saveData(data, R.id.MAIN_FORM_AREA_ID , Inventario.AREA);
		saveData(data, R.id.MAIN_FORM_EDIFICIO_ID , Inventario.EDIFICIO);
		saveData(data, R.id.MAIN_FORM_DEPARTAMENTO_ID , Inventario.DEPARTAMENTO);
		saveData(data, R.id.MAIN_FORM_PISO_ID , Inventario.PISO);
		saveData(data, R.id.MAIN_FORM_NOMBRE_ID , Inventario.NOMBRE);
		saveData(data, R.id.MAIN_FORM_CODIGO_VASA_ID , Inventario.CODIGO_VASA);
		saveData(data, R.id.MAIN_FORM_CODIGO_CLIENTE_ID , Inventario.CODIGO_CLIENTE);
		saveData(data, R.id.MAIN_FORM_MARCA_ID , Inventario.MARCA);
		saveData(data, R.id.MAIN_FORM_MODELO_ID , Inventario.MODELO);
		saveData(data, R.id.MAIN_FORM_SERIAL_ID , Inventario.SERIAL);
		saveData(data, R.id.MAIN_FORM_TIPO_ID , Inventario.TIPO);
		saveData(data, R.id.MAIN_FORM_FABRICANTE_ID , Inventario.FABRICANTE);
		saveData(data, R.id.MAIN_FORM_PAIS_ID , Inventario.PAIS_ORIGEN);
		saveData(data, R.id.MAIN_FORM_CODIGO_PLANTA_ID , Inventario.CODIGO_PLANTA);
		saveData(data, R.id.MAIN_FORM_CAPACIDAD_ID , Inventario.CAPACIDAD);
		saveData(data, R.id.MAIN_FORM_DIMENSIONES_PUL_ID , Inventario.DIMENDIONES);
		saveData(data, R.id.MAIN_FORM_DIMENSIONES_L_ID , Inventario.DIMENDIONES_L);
		saveData(data, R.id.MAIN_FORM_DIMENSIONES_A_ID , Inventario.DIMENDIONES_A);
		saveData(data, R.id.MAIN_FORM_DIMENSIONES_H_ID , Inventario.DIMENDIONES_H);
		saveSpinner(data, R.id.ACCIONADO_LIST_ID,Inventario.ACCIONADO_LIST);
		saveData(data, R.id.MAIN_FORM_ACCIONADO_DE_ID , Inventario.ACCIONADO_DE);
		saveRadio(data, R.id.ACCIONADO_RADIO_ID,Inventario.ACCIONADO_RADIO);
		saveData(data, R.id.MAIN_FORM_ACCIONADO_POR_ID , Inventario.ACCIONADO_POR);
		saveData(data, R.id.COMPLETO_ID , Inventario.COMPLETO);
		saveSpinner(data, R.id.RECUBIERTO_CON_ID,Inventario.RECUBIERTO_CON);
		saveSpinner(data, R.id.CONSTRUIDO_CON_ID,Inventario.CONSTRUIDO_CON);
		saveData(data, R.id.CONSTRUIDO_E_ID , Inventario.CONSTRUIDO_E);
		saveSpinner(data, R.id.MONTADO_SOBRE_ID,Inventario.MONTADO_SOBRE);
		saveSpinner(data, R.id.CONDICION_ID,Inventario.CONDICION);
		saveSpinner(data, R.id.EJE_BOMBA_ID,Inventario.EJE_BOMBA);
		saveRadio(data, R.id.FUNCIONANDO_ID,Inventario.FUNCIONANDO);
		saveData(data, R.id.FLUIDO_ID , Inventario.FLUIDO);
		saveData(data, R.id.FLUIDO_ENTRADA_ID , Inventario.FLUIDO_ENTRADA);
		saveData(data, R.id.FLUIDO_SALIDA_ID , Inventario.FLUIDO_SALIDA);
		saveData(data, R.id.ESTIMADO_ID , Inventario.ESTIMADO);
		inventario.setData(data);
		inventario.setImageMedia(imgList);
		inventario.setAudioMedia(audioList);

	}

	private void saveRadio(HashMap<String, String> data, int radioId,
			String id) {

		RadioGroup rg = (RadioGroup)findViewById(radioId ); 

		int selectedId = rg.getCheckedRadioButtonId();

		RadioButton  rb = (RadioButton) findViewById(selectedId);

		String value = (String) rb.getText();

		if(value!=null){

			data.put(id, value);

		}						
	}


	private void saveSpinner(HashMap<String, String> data, int listId,
			String id) {

		Spinner sp = (Spinner)findViewById(listId ); 

		String value = String.valueOf(sp.getSelectedItem());

		if(value!=null){

			data.put(id, value);

		}				
	}


	private void saveData(HashMap<String, String> data,
			int wid, String id) {

		EditText et = (EditText)findViewById(wid ); 

		String value = et.getText().toString();

		if(value!=null){

			data.put(id, value);

		}				
	}

	public class AudioClickHandler implements View.OnClickListener 
	{

		byte[] soundByteArray ;

		AudioClickHandler(byte[] soundByteArray){

			this.soundByteArray=soundByteArray;

		}

		@Override
		public void onClick(View v) {

			playMp3(soundByteArray);
		}	

	}



	public byte[] readBytes(InputStream inputStream) throws IOException {
		// this dynamically extends to take the bytes you read
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}
	
	private void playMp3(byte[] mp3SoundByteArray) {
	    try {
	        // create temp file that will hold byte array
	        File tempMp3 = File.createTempFile("audiotemp", "mp3", getCacheDir());
	        tempMp3.deleteOnExit();
	        FileOutputStream fos = new FileOutputStream(tempMp3);
	        fos.write(mp3SoundByteArray);
	        fos.close();

	        // Tried reusing instance of media player
	        // but that resulted in system crashes...  
	        MediaPlayer mediaPlayer = new MediaPlayer();

	        // Tried passing path directly, but kept getting 
	        // "Prepare failed.: status=0x1"
	        // so using file descriptor instead
	        FileInputStream fis = new FileInputStream(tempMp3);
	        mediaPlayer.setDataSource(fis.getFD());

	        mediaPlayer.prepare();
	        mediaPlayer.start();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	    }
	}


	void showNoSupportedDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
 
			// set title
			alertDialogBuilder.setTitle("VASA");
 
			// set dialog message
			alertDialogBuilder
				.setMessage(R.string.ERROR_ACTION_NOT_SUPPORTED)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					
						dialog.cancel();

					}
				  });
				
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
		
					
	}

}