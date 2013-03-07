package com.nahmens.inventario;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.nahmens.inventario.utils.Utils;

public class Activo {

	private String id;
	private String name;
	private String autor;
	private long timeStamp;
	private HashMap<String,Property> propertyList;
	private List<MediaProperty> audioPropertyList;
	private List<MediaProperty> imagePropertyList;
	
	Activo(){

		this.id = Utils.generateId();
		
		this.timeStamp =  new Date().getTime();
		
		
	}

	Activo(String id){

		this.id = id;		
		this.timeStamp =  new Date().getTime();
		
		
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public HashMap<String,Property> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(HashMap<String,Property> propertyList) {
		this.propertyList = propertyList;
	}
	public List<MediaProperty> getAudioPropertyList() {
		return audioPropertyList;
	}
	public void setAudioPropertyList(List<MediaProperty> audioPropertyList) {
		this.audioPropertyList = audioPropertyList;
	}
	public List<MediaProperty> getImagePropertyList() {
		return imagePropertyList;
	}
	public void setImagePropertyList(List<MediaProperty> imagePropertyList) {
		this.imagePropertyList = imagePropertyList;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}
	
}
