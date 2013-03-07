package com.nahmens.inventario.mobile;

import com.nahmens.inventario.sqlite.InventarioControllerImpl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {
	
	private  InventarioControllerImpl controller;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
		setContentView(R.layout.activity_settings);

		controller = new com.nahmens.inventario.sqlite.InventarioControllerImpl(this);
		
		setText();
		
		setButonSave();

		setButonSync();

    }
    
	private void setText() {

		EditText et = (EditText)findViewById(R.id.SETTINGS_ID); 

		String value = controller.getSyncServer();

		if(value!=null){

			et.setText(value);		

		}		
	}

	/*******************************************************************************
	 * 
	 * 			Button Save
	 * 
	 * */

	private void setButonSave() {


		Button buttonNuevo = (Button) findViewById(R.id.BTN_SERVER_SAVE_ID);

		buttonNuevo.setOnClickListener( new ButtonSaveClickHandler() );


	}
	
	
	public class ButtonSaveClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "ButtonSaveClickHandler", "onClick()" );

			EditText et = (EditText)findViewById(R.id.SETTINGS_ID); 
			
			String server = et.getText().toString();

			controller.setSyncServer(server);
			
			Toast.makeText(getApplicationContext(), "Transacci—n exitosa",Toast.LENGTH_LONG).show();


		}

	}

	
	/*******************************************************************************
	 * 
	 * 			Button Save
	 * 
	 * */

	private void setButonSync() {


		Button buttonNuevo = (Button) findViewById(R.id.BTN_SYNC_ID);

		buttonNuevo.setOnClickListener( new ButtonSyncClickHandler() );


	}
	
	
	public class ButtonSyncClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {
			

			Log.i( "ButtonSyncClickHandler", "onClick()" );

			try{
				
				controller.syncServer();
				Toast.makeText(getApplicationContext(), "Sincronizaci—n exitosa",Toast.LENGTH_LONG).show();


			}catch(Exception e){
				
			
				Toast.makeText(getApplicationContext(), "Error, verificar servidor",Toast.LENGTH_LONG).show();
			}
			


		}

	}

}