package com.nahmens.inventario.sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.nahmens.inventario.Activo;
import com.nahmens.inventario.ActivoProperty;
import com.nahmens.inventario.Controller;
import com.nahmens.inventario.MediaProperty;
import com.nahmens.inventario.Property;
import com.nahmens.inventario.User;

public class ControllerImpl extends Controller{

	List<Activo> actvosStub;

	HashMap<String,List<Property>> properties;

	public ControllerImpl(){

		//Generate STUB
		properties = new HashMap<String,List<Property>>();

		for(ActivoProperty p : ActivoProperty.values()) {
			
			createStub(String.valueOf(p.getCode()),p.name());
		
		}
		
		

	}

	@Override
	public Activo createActivo(){

		return super.createActivo();

	}

	@Override
	public Property createProperty(String key, String value){

		return super.createProperty( key,  value);

	}
	@Override
	public List<Activo> getActivos() {

		//TODO do real 
		return actvosStub;
	}

	@Override
	public Activo getActivoById(String id) {
		// TODO Auto-generated method stub
		return actvosStub.get(0);
	}

	@Override
	public void saveActivo(Activo activo) {

		actvosStub = new ArrayList<Activo>();

		actvosStub.add(activo);

	}

	@Override
	public List<Property> getPropertiesByKey(String key) {
		return properties.get(key);
	}

	@Override
	public void saveProperties(List<Property> properties) {
		
		this.properties.put(properties.get(0).getKey(), properties);

	}

	@Override
	public void saveMediaProperties(List<MediaProperty> properties) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUserById(String id, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User saveUser(String id, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createStub(String key, String valueBase){

		List<Property> list = new ArrayList<Property>();

		Property p =  super.createProperty(key,valueBase+1);
		list.add(p);
		p =  super.createProperty(key,valueBase+2);
		list.add(p);
		p =  super.createProperty(key,valueBase+3);
		list.add(p);
		properties.put(key, list);
	}
}
