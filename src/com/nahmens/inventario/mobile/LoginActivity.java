package com.nahmens.inventario.mobile;

import java.util.Date;

import com.nahmens.inventario.User;
import com.nahmens.inventario.sqlite.InventarioControllerImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button buttonNuevo;
	private InventarioControllerImpl controller;
	GPSTracker gps;
	String location;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		controller = new com.nahmens.inventario.sqlite.InventarioControllerImpl(this);

		setButon();

		setSYNButon();

		gps = new GPSTracker(LoginActivity.this);
		 
        // check if GPS enabled
        if(!gps.canGetLocation()){

            gps.showSettingsAlert();

            
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }



	}

	private void setSYNButon() {


		buttonNuevo = (Button) findViewById(R.id.BTN_LONGIN_SYN_ID);

		buttonNuevo.setOnClickListener( new ButtonSyncHandler() );

	}

	public class ButtonSyncHandler implements View.OnClickListener 
	{	

		public void onClick( View view ) {


			Log.i( "ButtonSyncClickHandler", "onClick()" );

			try{

				controller.syncServer();
				Toast.makeText(getApplicationContext(), "Sincronizaci—n exitosa",Toast.LENGTH_LONG).show();


			}catch(Exception e){


				Toast.makeText(getApplicationContext(), "Error, verificar servidor. "+e.getMessage(),Toast.LENGTH_LONG).show();
			}



		}




	}


	/*******************************************************************************
	 * 
	 * 			Button
	 * 
	 * */

	private void setButon() {


		buttonNuevo = (Button) findViewById(R.id.BTN_LOGIN_ID);

		buttonNuevo.setOnClickListener( new ButtonNuevoClickHandler() );


	}

	public class ButtonNuevoClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "Lista nuevo", "onClick()" );


			EditText et = (EditText)findViewById(R.id.LOGIN_FORM_UID ); 

			String usuario = et.getText().toString();

			et = (EditText)findViewById(R.id.LOGIN_FORM_PASSWORD ); 

			String pwd = et.getText().toString();


			if(usuario==null||usuario.length()==0||pwd==null||pwd.length()==0){

				Toast.makeText(getApplicationContext(), "Debe incluir usuario y password",Toast.LENGTH_LONG).show();

			}else{

				User user = controller.getUserById(usuario, pwd);

				if(user==null||!usuario.equals(user.getId())||!pwd.equals(user.getPassword())){

					Toast.makeText(getApplicationContext(), "Usuario/password invalido",Toast.LENGTH_LONG).show();
				}
				else{

					double latitude = gps.getLatitude();
		            double longitude = gps.getLongitude();
		            		            
		            Date now = new Date();
		            
		            try {
		            	
						controller.checkin(usuario,String.valueOf(now.getTime()),
								String.valueOf(latitude),String.valueOf(longitude));
						
						Intent intent = new Intent(LoginActivity.this,TabContainerActivity.class);

						intent.putExtra(InventariosActivity.PROPERTY_KEY , usuario);

						InventarioActivity.clearToStart();

						startActivityForResult(intent, 1);
						
					} catch (Exception e) {
						
			            Toast.makeText(getApplicationContext(), "Ha ocurrido un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
					}
		            
					

				}



			}		


		}

	}

}
