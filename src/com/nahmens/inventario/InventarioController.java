package com.nahmens.inventario;

import java.util.List;

public interface InventarioController {

	
	String PROYECTO_DN = "PROYECTO_DN";

	public  Inventario createNewInventario(String user);		
	
	public  List<String> getInventarioIds();

	public  Inventario getInventario(String id);
	
	public  void deleteInventario(String id);
	
	public  void saveInventario(Inventario inventario);

	public  void saveInventarioForCreation(Inventario inventario);

	public  List<String> getSavedAttributes(String key);
	
	public  User getUserById(String id,String password);

	public String getSyncServer();

	public void setSyncServer(String server);

	public void syncServer() throws Exception;

	public void syncInventario(String id) throws Exception;
	
	public void checkin(String user, String time, String latitude, String longitude) throws Exception;

	public List<String> getAutoCompleteValues(String key);

	public List<String> getProyectos();

	public boolean isProjectSync(String proyecto);

	public void syncProyecto(String pid) throws Exception;
}
