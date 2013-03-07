package com.nahmens.inventario.mysql;

import java.util.List;

import com.nahmens.inventario.Activo;
import com.nahmens.inventario.Controller;
import com.nahmens.inventario.MediaProperty;
import com.nahmens.inventario.Property;
import com.nahmens.inventario.User;

public class ControllerImpl extends Controller{

	@Override
	public List<Activo> getActivos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> getPropertiesByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveProperties(List<Property> properties) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void saveActivo(Activo activo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Activo getActivoById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
