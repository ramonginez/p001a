package com.nahmens.inventario;

import com.nahmens.inventario.utils.Utils;

public class MediaProperty {

	private String id;
	private String key;
	private Object value;
	
	public static final String AUDIO_KEY = "audio";
	public static final String IMAGE_KEY = "audio";
	
	MediaProperty(){
		this.id = Utils.generateId();
		
	}
	
	MediaProperty(String id){
		this.id = id;
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

}
