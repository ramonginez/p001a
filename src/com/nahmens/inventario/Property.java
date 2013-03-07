package com.nahmens.inventario;

import com.nahmens.inventario.utils.Utils;

public class Property {


	private String id;
	private String key;
	private String value;

	
	Property(String key, String value){
		
		this.id  = Utils.generateId();
		this.key = key;
		this.value = value;

	}
	
	Property(){
		this.id = Utils.generateId();
		
	}
	
	Property(String id){
		this.id = id;
		
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

}
