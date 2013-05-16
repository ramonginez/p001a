package com.nahmens.inventario.mobile;

import java.util.HashMap;
import java.util.List;

import com.nahmens.inventario.Inventario;
import com.nahmens.inventario.mobile.R;
import com.nahmens.inventario.sqlite.InventarioControllerImpl;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.TableRow.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class InventariosActivity extends Activity {

	private  InventarioControllerImpl controller;

	final Context context = this;

	private LinearLayout listMenu;

	private Button buttonNuevo;

	private String proyecto = null;
	private String uid = null;
	public static final  String PROPERTY_PROJECT_KEY = "pProject";
	public static final  String PROPERTY_KEY = "pk";
	public static final  String NEW_INVENTARIO_PROPERTY_KEY = "new";


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_inventarios);

		Bundle bundle = getIntent().getExtras();

		uid = (String) bundle.get(InventariosActivity.PROPERTY_KEY);
		
		proyecto = (String) bundle.get(InventariosActivity.PROPERTY_PROJECT_KEY);
		
		controller = new com.nahmens.inventario.sqlite.InventarioControllerImpl(this);

		setButon();

		setList();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	 
	   //Do nothing
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		Log.i( "MakeMachine", "resultCode: " + resultCode );

		switch( requestCode ){

		case 1: 

			setList();

			break;


		}

	}

	private void setList() {

		TextView tv1 = (TextView)findViewById(R.id.INVENTARIOS_PROYECTO_ID ); 

		tv1.setText(proyecto);
		
		TableLayout tl = (TableLayout) findViewById(R.id.LISTA_INVENTARIO_ID);

		listMenu = (LinearLayout) findViewById(R.id.LISTA_INVENTARIO_ID);

		List<String> inventarios = controller.getInventarioIds();

		listMenu.removeAllViews();

		if(inventarios!=null){

			int i = 1;
			for(String inventarioId : inventarios){

				TableRow tr = new TableRow(this);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tr.setLayoutParams(lp);

				Inventario inventario = controller.getInventario(inventarioId);

				HashMap<String,String> data = inventario.getData();

				TextView tv=new TextView(this);

				String dProyecto = data.get(Inventario.PROYECTO);
				String display = data.get(Inventario.NOMBRE);

				if(dProyecto==null||!dProyecto.equals(proyecto)||display==null){
					
					continue;
				}
				
				String lastSaved = data.get(Inventario.LAST_SAVED);
				String lastSync = data.get(Inventario.LAST_SYNC);

				tv.setText(display);
				tv.setTypeface(null, Typeface.BOLD);
				tv.setPadding(10, 0, 0, 0);
				tv.setLayoutParams(lp);
				tv.setTextSize(20);
				tv.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));

				ImageView imgView = new ImageView(this);
				imgView.setPadding(10, 0, 0, 0);
				imgView.setLayoutParams(lp);

				int color = R.drawable.red_light;

				if(lastSaved!=null&&lastSync!=null&&lastSaved.equals(lastSync)){

					color = R.drawable.green_light;
				}

				imgView.setImageDrawable(getResources().getDrawable(color));

				if(i % 2 == 0){

					tr.setBackgroundColor(0xffcccccc);


				}else{

					tr.setBackgroundColor(0xff888888);

				}

				ImageView imgViewSync = new ImageView(this);
				imgViewSync.setImageDrawable(getResources().getDrawable(R.drawable.sync));
				imgViewSync.setPadding(20, 0, 0, 0);
				imgViewSync.setLayoutParams(lp);
				imgViewSync.setOnClickListener( new syncClickHandler(inventarioId,display) );

				ImageView imgViewRemove = new ImageView(this);
				imgViewRemove.setImageDrawable(getResources().getDrawable(R.drawable.remove));
				imgViewRemove.setPadding(20, 0, 0, 0);
				imgViewRemove.setLayoutParams(lp);
				imgViewRemove.setOnClickListener( new removeClickHandler(inventarioId,display) );

				ImageView imgViewEdit = new ImageView(this);
				imgViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.writing));
				imgViewEdit.setPadding(20, 0, 0, 0);
				imgViewEdit.setLayoutParams(lp);
				imgViewEdit.setOnClickListener( new InventarioClickHandler(inventario.getId()) );


				tr.addView(imgView);
				tr.addView(tv);
				tr.addView(imgViewSync);
				tr.addView(imgViewRemove);
				tr.addView(imgViewEdit);

				tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				i++;

			}
		}


	}


	public class InventarioClickHandler implements View.OnClickListener 
	{	
		String id;

		InventarioClickHandler(String id){

			this.id=id;
		}

		public void onClick( View view ) {

			Log.i( "Lista existente: "+id, "onClick()" );

			Intent intent = new Intent(InventariosActivity.this,InventarioActivity.class);

			intent.putExtra(InventariosActivity.PROPERTY_KEY , id);

			startActivityForResult(intent, 1); 	

		}

	}


	/*******************************************************************************
	 * 
	 * 			Button
	 * 
	 * */

	private void setButon() {


		Button regresar =(Button) findViewById(R.id.BTN_REGRESAR_ID);
		
		regresar.setOnClickListener( new ButtonRegresarClickHandler() );
		
		buttonNuevo = (Button) findViewById(R.id.BTN_NUEVO_ID);

		buttonNuevo.setOnClickListener( new ButtonNuevoClickHandler() );


	}
	public class ButtonRegresarClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "Regresar", "onClick()" );
			
			setResult(1,null);	

			finish();

		}
		
	}

	public class ButtonNuevoClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "Lista nuevo", "onClick()" );

			EditText et = (EditText)findViewById(R.id.INVENTARIOS_NOMBRE_ID ); 

			String nombre = et.getText().toString();


			if(nombre==null||nombre.length()==0||proyecto==null||proyecto.length()==0){

				Toast.makeText(getApplicationContext(), "Debe incluir Nombre",Toast.LENGTH_LONG).show();

			}else{

				Inventario inventario = controller.createNewInventario(uid);

				HashMap<String,String> data = inventario.getData();

				data.put(Inventario.PROYECTO, proyecto);

				data.put(Inventario.NOMBRE, nombre);

				inventario.setData(data);

				controller.saveInventario(inventario);

				Intent intent = new Intent(InventariosActivity.this,InventarioActivity.class);

				intent.putExtra(InventariosActivity.PROPERTY_KEY , inventario.getId());

				startActivityForResult(intent, 1);
			}		


		}

	}

	public class idAlertHandler extends AlertDialog.Builder{

		String pid;

		public idAlertHandler(Context arg0, String pid) {

			super(arg0);

			this.pid = pid;
		} 

	}

	public class removeClickHandler implements View.OnClickListener 
	{

		String display;

		String pid;

		removeClickHandler(String id, String display){

			this.pid=id;
			this.display = display;
		}
		@Override
		public void onClick(View v) {


			AlertDialog.Builder alertDialogBuilder = new idAlertHandler(
					context,pid);

			// set title
			alertDialogBuilder.setTitle("VASA");

			// set dialog message
			alertDialogBuilder
			.setMessage("Seguro que deseas remover "+display+" del dispositivo?")
			.setCancelable(false)
			.setPositiveButton("Si",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity

					controller.deleteInventario(pid);
					setList();
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

	public class syncClickHandler implements View.OnClickListener 
	{

		String display;

		String pid;

		syncClickHandler(String id, String display){

			this.pid=id;
			this.display = display;
		}

		@Override
		public void onClick(View v) {

			AlertDialog.Builder alertDialogBuilder = new idAlertHandler(
					context,pid);

			// set title
			alertDialogBuilder.setTitle("VASA");

			// set dialog message
			alertDialogBuilder
			.setMessage("Seguro que deseas sincronizar los datos de "+display+" ?")
			.setCancelable(false)
			.setPositiveButton("Si",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity

					try {
					
						controller.syncInventario(pid);
					
						setList();
						Toast.makeText(getApplicationContext(), "Sincronizaci—n exitosa!",Toast.LENGTH_LONG).show();

					} catch (Exception e) {

						Toast.makeText(getApplicationContext(), "Fallo la sincronizaci—n, intente otra vez!",Toast.LENGTH_LONG).show();

					}
					
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
