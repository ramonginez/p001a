package com.nahmens.inventario.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LogoutActivity extends Activity {
    
	private Button buttonNuevo;
	final Context context = this;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
		setContentView(R.layout.activity_logout);

		setLogOutButon();

    }


	private void setLogOutButon() {

		buttonNuevo = (Button) findViewById(R.id.BTN_LOGOUT);

		buttonNuevo.setOnClickListener( new ButtonLogOutClickHandler() );


	}
	

	public class ButtonLogOutClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "ButtonLogOutClickHandlero", "onClick()" );
			
			
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
	 
				// set title
				alertDialogBuilder.setTitle("VASA");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Seguro deseas salir?")
					.setCancelable(false)
					.setPositiveButton("Si",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							finish();
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();
			
			
			
			


		}

	}
}