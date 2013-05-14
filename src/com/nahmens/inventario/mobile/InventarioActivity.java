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
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import android.text.Editable;
import android.text.TextWatcher;

import com.nahmens.inventario.Inventario;
import com.nahmens.inventario.sqlite.InventarioControllerImpl;
import com.nahmens.inventario.utils.InstantAutoComplete;

@SuppressLint("ParserError")
public class InventarioActivity extends Activity {
	/** Called when the activity is first created. */

	final Context context = this;

	public final static int RETURN_PICTURE_CODE = 200;
	public final static int RETURN_AUDIO_CODE = 400;
	protected static final String PHOTO_TAKEN = "photo_taken";

	//private static Controller controller = new com.nahmens.activos.sqlite.ControllerImpl();
	private static InventarioControllerImpl inventarioController;
	private ImageButton buttonPicture;
	private boolean _taken;
	static String _path;
	static String _audioPath;

	LinearLayout _imagesContainer;
	List<byte[]> _imgList = new ArrayList<byte[]>();

	LinearLayout _audioContainer;
	List<byte[]> _audioList = new ArrayList<byte[]>();

	static View _view;
	static Inventario inventario;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_inventario);
		
		
		

		Bundle bundle = getIntent().getExtras();

		if(inventario == null){

			String inventarioId = (String) bundle.get(InventariosActivity.PROPERTY_KEY);

			inventarioController = new com.nahmens.inventario.sqlite.InventarioControllerImpl(this);

			inventario = inventarioController.getInventario(inventarioId);
			
			

		}

		

		setPicture();

		setAudio();

		setGuardar();

		setProject();

		//Asignamos un listener al campo nombre_id para ajustar los valores del tipo segun
		//corresponda.
		EditText teNombre = (EditText) findViewById(R.id.MAIN_FORM_NOMBRE_ID);
		String nombre = teNombre.getText().toString();
		teNombre.addTextChangedListener(new TextWatcher() {
			 
			   public void afterTextChanged(Editable s) {
			   }
			 
			   public void beforeTextChanged(CharSequence s, int start, 
			     int count, int after) {
			   }
			 
			   public void onTextChanged(CharSequence s, int start, 
			     int before, int count) {
				   EditText teNombre = (EditText) findViewById(R.id.MAIN_FORM_NOMBRE_ID);
				   String nombre = teNombre.getText().toString();
				   setAutocompleteTipo(nombre);
			   }
			  });
		
		setAutocompleteTipo(nombre);

	}




	/*
	 * Funcion que muestra lista de sugerencias sobre el campo tipo
	 * @param nombre Valor del edit text nombre
	 */
	private void setAutocompleteTipo(String nombre){
		

		if(nombre!=null){
			nombre = nombre.toLowerCase();

			String[] tipo = null;

			if(nombre.equals("bomba")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_BOMBA);
				
			}else if(nombre.equals("compresor de aire")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_COMPRESOR);
				
			}else if(nombre.equals("torre de enfriamiento")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TORRE_ENFR);
				
			}else if(nombre.equals("caldera")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_CALDERA);
				
			}else if(nombre.equals("intercambiador de calor")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_INTERCAMBIADOR_CALOR);
				
			}else if(nombre.equals("chiller")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_CHILLER);
				
			}else if(nombre.equals("transformador monofasico")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TRANSFORMADOR_MONOFASICO);
				
			}else if(nombre.equals("transformador trifasico")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TRANSFORMADOR_TRIFASICO);
				
			}else if(nombre.equals("llenadora")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_LLENADORA);
				
			}else if(nombre.equals("cerradora de tapas")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_CERRADORA_TAPAS);
				
			}else if(nombre.equals("balanza")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_BALANZA);
				
			}else if(nombre.equals("torno")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TORNO);
				
			}else if(nombre.equals("transportador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TRANSPORTADOR);
				
			}else if(nombre.equals("agitador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_AGITADOR);
				
			}else if(nombre.equals("sierra")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_SIERRA);
				
			}else if(nombre.equals("microscopio")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_MICROSCOPIO);
				
			}else if(nombre.equals("prensa")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_PRENSA);
				
			}else if(nombre.equals("fresadora")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_FRESADORA);
				
			}else if(nombre.equals("taladro")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TALADRO);
				
			}else if(nombre.equals("esmeril")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_ESMERIL);
				
			}else if(nombre.equals("elevador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_ELEVADOR);
				
			}else if(nombre.equals("silo")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_SILO);
				
			}else if(nombre.equals("tamiz")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TAMIZ);
				
			}else if(nombre.equals("molino")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_MOLINO);
				
			}else if(nombre.equals("colector de polvos")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_COLECTOR_POLVOS);
				
			}else if(nombre.equals("formadora de tubos")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_FORMADORA_TUBOS);
				
			}else if(nombre.equals("tanque pulmon de aire")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_TANQUE_PULMON_AIRE);
				
			}else if(nombre.equals("compactadora")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_COMPACTADORA);
				
			}else if(nombre.equals("colador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_COLADOR);
				
			}else if(nombre.equals("romana para gandolas")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_ROMANA_GANDOLAS);
				
			}else if(nombre.equals("refinador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_REFINADOR);
				
			}else if(nombre.equals("impresora")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_IMPRESORA);
				
			}else if(nombre.equals("despastillador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_DESPASTILLADOR);
				
			}else if(nombre.equals("espesador")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_ESPESADOR);
				
			}else if(nombre.equals("rebobinadora")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_REBOBINADORA);
				
			}else if(nombre.equals("maquina de soldar")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_MAQUINA_SOLDAR);
				
			}else if(nombre.equals("rectificadora")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_RECTIFICADORA);
				
			}else if(nombre.equals("guillotina")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_GUILLOTINA);
				
			}else if(nombre.equals("montacarga")){
				tipo =  getResources().getStringArray(R.array.AUTOCOMPLETE_TIPO_MONTACARGA);
				
			}else{
				Log.e("InventarioActivity", "Valor de nombre no concuerda con los predefinidos");
			}

			InstantAutoComplete textView = (InstantAutoComplete) findViewById(R.id.MAIN_FORM_TIPO_ID);

			if (tipo != null){

				ArrayAdapter<String> adapter = 
						new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tipo);
				textView.setAdapter(adapter);
				
			}else{
				ArrayAdapter<String> adapter = 
						new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
				textView.setAdapter(adapter);
			}
		}
	}


	public static void clearToStart(){

		inventario = null;

	}


	// Function to read the result from newly created activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.i( "MakeMachine", "resultCode: " + resultCode );

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

		_imagesContainer =  (LinearLayout)findViewById( R.id.images ); 

		buttonPicture = (ImageButton) findViewById(R.id.picBtn);

		buttonPicture.setOnClickListener( new ButtonPictureClickHandler() );


	}

	public class ButtonPictureClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "MakeMachine", "onClick()" );

			_view= view;

			UUID id = UUID.randomUUID();

			_path = Environment.getExternalStorageDirectory() +"/"+id.toString()+".jpg";

			startCameraActivity();

		}

	}


	protected void startCameraActivity()
	{

		Log.i( "MakeMachine", "startCameraActivity()" );

		/*//define the file-name to save photo taken by Camera activity
		String fileName = "new-photo-name.jpg";
		//create parameters for Intent with filename
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);
		values.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");
		//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		Uri imageUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		//create new Intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, RETURN_PICTURE_CODE);*/


		File file = new File( _path );
		Uri outputFileUri = Uri.fromFile( file );

		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
		intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

		save();
		startActivityForResult( intent, RETURN_PICTURE_CODE );
	}

	protected void onPhotoTaken()
	{


		Log.i( "MakeMachine", "onPhotoTaken()");

		_taken = true;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;

		try{

			ByteArrayOutputStream baos = new ByteArrayOutputStream();  

			Bitmap bitmap = BitmapFactory.decodeFile( _path, options );

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);   

			byte[] photo = baos.toByteArray(); 

			setImgOnDisplay(photo);


		}catch (Exception e){

			Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Error:"+e.getCause(),Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Error:"+e.getStackTrace(),Toast.LENGTH_LONG).show();

		}
	}

	private void setImgOnDisplay(byte[] photo) {

		ImageView image = new ImageView(_view.getContext()); 

		Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

		image.setImageBitmap(bitmap);

		_imagesContainer.addView(image, 200, 200); 

		_imgList.add(photo);


	}







	@Override
	protected void onSaveInstanceState( Bundle outState ) {
		Log.i( "MakeMachine", "onSaveInstanceState()");

		outState.putBoolean( PHOTO_TAKEN, _taken );
	}

	@Override 
	protected void onRestoreInstanceState( Bundle savedInstanceState)
	{
		Log.i( "MakeMachine", "onRestoreInstanceState()");
		if( savedInstanceState.getBoolean( PHOTO_TAKEN ) ) {
			onPhotoTaken();
		}
	}



	/*******************************************************************************
	 * 
	 * 			AUDIO
	 * 
	 * */



	private void setAudio() {

		_audioContainer = (LinearLayout)findViewById( R.id.audio );

		ImageButton buttonRecord = (ImageButton)findViewById(R.id.audioBtn);

		buttonRecord.setOnClickListener( new ButtonAudioClickHandler() );


	}


	public class ButtonAudioClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "AudioMachine", "startAudioActivity()" );

			_view= view;


			Intent intent =
					new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

			startActivityForResult(intent, RETURN_AUDIO_CODE);


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

		_audioContainer.addView(imgView, 100, 100); 

		_audioList.add(binaryData);

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
		saveRadio(data, R.id.FUNCIONANDO_ID,Inventario.FUNCIONANDO);
		saveData(data, R.id.FLUIDO_ID , Inventario.FLUIDO);
		saveData(data, R.id.FLUIDO_ENTRADA_ID , Inventario.FLUIDO_ENTRADA);
		saveData(data, R.id.FLUIDO_SALIDA_ID , Inventario.FLUIDO_SALIDA);
		saveData(data, R.id.ESTIMADO_ID , Inventario.ESTIMADO);
		inventario.setData(data);
		inventario.setImageMedia(_imgList);
		inventario.setAudioMedia(_audioList);

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


}