package com.nahmens.inventario;

import java.util.List;

public abstract class Controller {

	public Activo createActivo() {
		return new Activo();
	}

	public Property createProperty(String key, String value) {

		return new Property(key,value);
	}

	public abstract List<Activo> getActivos();
	public abstract Activo getActivoById(String id); 
	public abstract void saveActivo(Activo activo); 

	public abstract List<Property> getPropertiesByKey(String key); 
	public abstract void saveProperties(List<Property> properties);
	public abstract void saveMediaProperties(List<MediaProperty> properties);

	public abstract User getUserById(String id,String password);
	public abstract User saveUser(String id,String password);


	public  MediaProperty createImageProperty(Object value){

		MediaProperty mp = new MediaProperty();

		mp.setKey(MediaProperty.IMAGE_KEY);

		mp.setValue(value);

		return mp;
	}

	public  MediaProperty createAudioProperty(Object value){

		MediaProperty mp = new MediaProperty();

		mp.setKey(MediaProperty.AUDIO_KEY);

		mp.setValue(value);

		return mp;
	}


}
