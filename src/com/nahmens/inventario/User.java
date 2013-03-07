package com.nahmens.inventario;

import java.util.Date;
import java.util.List;

import com.nahmens.inventario.utils.Utils;

public class User {

	private String id;
	private String displayName;
	private String password;
	private List<Property> propertyList;
	private long timeStamp;
	
	User(){
		
		this.id = Utils.generateId();
		
		this.setTimeStamp(new Date().getTime());

	}
	
	public User(String id){
		
		this.id = id;
		
	}	
	
	public List<Property> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	
	
}
