package com.nahmens.inventario.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nahmens.inventario.Inventario;
import com.nahmens.inventario.InventarioController;
import com.nahmens.inventario.mobile.InventariosActivity.idAlertHandler;
import com.nahmens.inventario.mobile.InventariosActivity.syncClickHandler;
import com.nahmens.inventario.sqlite.InventarioControllerImpl;

public class ProyectosActivity extends Activity {

	private  InventarioController controller;

	final Context context = this;

	private LinearLayout listMenu;

	private Button buttonNuevo;

	private String uid = null;
	
	private List<String> proyectos;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_proyectos);

		Bundle bundle = getIntent().getExtras();

		uid = (String) bundle.get(InventariosActivity.PROPERTY_KEY);

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


	/*******************************************************************************
	 * 
	 * 			Button
	 * 
	 * */

	private void setButon() {


		buttonNuevo = (Button) findViewById(R.id.BTN_NUEVO_PROYECTO_ID);

		buttonNuevo.setOnClickListener( new ButtonNuevoClickHandler() );


	}

	public class ButtonNuevoClickHandler implements View.OnClickListener 
	{	
		public void onClick( View view ) {

			Log.i( "Lista nuevo", "onClick()" );


			EditText et = (EditText)findViewById(R.id.PROYECTOS_PROYECTO_ID ); 

			String proyecto = et.getText().toString();
			
			if(proyecto==null||proyecto.length()==0){
				
				String msg = "Debe incluir el nombre del proyecto";
				
				Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_LONG).show();
				
			}else if(proyectos.contains(proyecto)){
				
				String msg = "El proyecto: "+proyecto+" ya existe!";
				
				Toast.makeText(getApplicationContext(), msg ,Toast.LENGTH_LONG).show();

			}else{
				
				Inventario inventario = controller.createNewInventario(uid);

				HashMap<String,String> data = inventario.getData();

				data.put(Inventario.PROYECTO, proyecto);

				inventario.setData(data);

				controller.saveInventarioForCreation(inventario);

				setList();


			}

		}
	}


	/*******************************************************************************
	 * 
	 * 			List
	 * @throws JSONException 
	 * 
	 * */
	private void setList()  {

		TableLayout tl = (TableLayout) findViewById(R.id.LISTA_PROYECTOS_ID);

		listMenu = (LinearLayout) findViewById(R.id.LISTA_PROYECTOS_ID);

		listMenu.removeAllViews();
		
		proyectos = new ArrayList<String>();

		List<String> proyectoList = controller.getProyectos();

		if(proyectoList!=null){

			int i = 1;

			for (String proyecto : proyectoList) {
				
				boolean isSync = controller.isProjectSync(proyecto);
				
				proyectos.add(proyecto);
				
				TableRow tr = new TableRow(this);
				LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				tr.setLayoutParams(lp);


				TextView tv=new TextView(this);

				tv.setText(proyecto);
				tv.setTypeface(null, Typeface.BOLD);
				tv.setPadding(10, 0, 0, 0);
				tv.setLayoutParams(lp);
				tv.setTextSize(20);
				tv.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));

				
				ImageView imgView = new ImageView(this);
				imgView.setPadding(10, 0, 0, 0);
				imgView.setLayoutParams(lp);

				int color = R.drawable.red_light;

				if(isSync){

					color = R.drawable.green_light;
				}


				imgView.setImageDrawable(getResources().getDrawable(color));

				
				ImageView imgViewSync = new ImageView(this);
				imgViewSync.setImageDrawable(getResources().getDrawable(R.drawable.sync));
				imgViewSync.setPadding(20, 0, 0, 0);
				imgViewSync.setLayoutParams(lp);
				imgViewSync.setOnClickListener( new syncClickHandler(proyecto) );

				ImageView imgViewEdit = new ImageView(this);
				imgViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.writing));
				imgViewEdit.setPadding(20, 0, 0, 0);
				imgViewEdit.setLayoutParams(lp);
				imgViewEdit.setOnClickListener( new ProyectoClickHandler(proyecto) );


			
				if(i % 2 == 0){

					tr.setBackgroundColor(0xffcccccc);


				}else{

					tr.setBackgroundColor(0xff888888);

				}
				
				tr.addView(imgView);
				tr.addView(tv);
				tr.addView(imgViewSync);
				tr.addView(imgViewEdit);

				tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				i++;  
			}
			
			
		}


	}
	
	
	
	public class ProyectoClickHandler implements View.OnClickListener 
	{	
		String id;

		ProyectoClickHandler(String id){

			this.id=id;
		}

		public void onClick( View view ) {

			Log.i( "Lista existente: "+id, "onClick()" );

			Intent intent = new Intent(ProyectosActivity.this,InventariosActivity.class);

			intent.putExtra(InventariosActivity.PROPERTY_PROJECT_KEY , id);

			intent.putExtra(InventariosActivity.PROPERTY_KEY , uid);

			startActivityForResult(intent, 1);

		}

	}


	public class idAlertHandler extends AlertDialog.Builder{

		String pid;

		public idAlertHandler(Context arg0, String pid) {

			super(arg0);

			this.pid = pid;
		} 

	}

	public class syncClickHandler implements View.OnClickListener 
	{

		String pid;

		syncClickHandler(String id){

			this.pid=id;
		}

		@Override
		public void onClick(View v) {

			AlertDialog.Builder alertDialogBuilder = new idAlertHandler(context,pid);

			// set title
			alertDialogBuilder.setTitle("VASA");

			// set dialog message
			alertDialogBuilder
			.setMessage("Seguro que deseas sincronizar los datos de "+pid+" ?")
			.setCancelable(false)
			.setPositiveButton("Si",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity

					try {

						controller.syncProyecto(pid);

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
