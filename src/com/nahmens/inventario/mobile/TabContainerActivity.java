package com.nahmens.inventario.mobile;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabContainerActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);

		Bundle bundle = getIntent().getExtras();

		String uid = (String) bundle.get(InventariosActivity.PROPERTY_KEY);

		Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 

		// inventarios tab
		
		Intent intentProyectos = new Intent().setClass(this, ProyectosActivity.class);
		intentProyectos.putExtra(InventariosActivity.PROPERTY_KEY , uid);

		TabSpec tabSpecInventarios = tabHost
				.newTabSpec("Proyectos")
				.setIndicator("", ressources.getDrawable(R.drawable.ic_launcher))
				.setContent(intentProyectos);

		// Settings tab
		Intent intentSettings = new Intent().setClass(this, SettingActivity.class);
		TabSpec tabSpecSettings = tabHost
				.newTabSpec("Setting")
				.setIndicator("", ressources.getDrawable(R.drawable.settings))
				.setContent(intentSettings);

		// logout tab
				Intent intentLogout = new Intent().setClass(this, LogoutActivity.class);
				TabSpec tabSpecLogout = tabHost
						.newTabSpec("Logout")
						.setIndicator("", ressources.getDrawable(R.drawable.logout))
						.setContent(intentLogout);


		// add all tabs 
		tabHost.addTab(tabSpecInventarios);
		tabHost.addTab(tabSpecSettings);
		tabHost.addTab(tabSpecLogout);

		//set Windows tab as default (zero based)
		tabHost.setCurrentTab(0);
	}

}