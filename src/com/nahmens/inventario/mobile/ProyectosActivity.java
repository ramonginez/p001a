package com.nahmens.inventario.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import com.nahmens.inventario.sqlite.InventarioControllerImpl;

public class ProyectosActivity extends Activity {

	private  InventarioControllerImpl controller;

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

				controller.saveInventario(inventario);

				setList();

			}

		}
	}


	/*******************************************************************************
	 * 
	 * 			List
	 * 
	 * */
	private void setList() {

		TableLayout tl = (TableLayout) findViewById(R.id.LISTA_PROYECTOS_ID);

		listMenu = (LinearLayout) findViewById(R.id.LISTA_PROYECTOS_ID);

		List<String> inventarios = controller.getInventarioIds();

		listMenu.removeAllViews();
		
		proyectos = new ArrayList<String>();

		if(inventarios!=null){

			int i = 1;
			for(String inventarioId : inventarios){
				
				Inventario inventario = controller.getInventario(inventarioId);

				HashMap<String,String> data = inventario.getData();
				
				String display = data.get(Inventario.PROYECTO);
				
				if(!proyectos.contains(display)){
					
					proyectos.add(display);
					
					TableRow tr = new TableRow(this);
					LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					tr.setLayoutParams(lp);


					TextView tv=new TextView(this);

					tv.setText(display);
					tv.setTypeface(null, Typeface.BOLD);
					tv.setPadding(10, 0, 0, 0);
					tv.setLayoutParams(lp);
					tv.setTextSize(20);
					tv.setLayoutParams(new LayoutParams(300, LayoutParams.WRAP_CONTENT));

					ImageView imgViewEdit = new ImageView(this);
					imgViewEdit.setImageDrawable(getResources().getDrawable(R.drawable.writing));
					imgViewEdit.setPadding(20, 0, 0, 0);
					imgViewEdit.setLayoutParams(lp);
					imgViewEdit.setOnClickListener( new ProyectoClickHandler(display) );


					if(i % 2 == 0){

						tr.setBackgroundColor(0xffcccccc);


					}else{

						tr.setBackgroundColor(0xff888888);

					}
					tr.addView(tv);
					tr.addView(imgViewEdit);

					tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
					i++;  
				}
				
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




}
