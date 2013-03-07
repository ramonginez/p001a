package com.nahmens.inventario;


public enum ActivoProperty {
	 
	DEPARTAMENTO(1), UBICACION(2), PLANTA(3), DESCRIPCION(4), MARCA(5),
	MODELO(6), SERIAL(7), TIPO(8), FABRICACION(9), MATERIAL(10),DIMENSIONES(11),
	CONSERVACION(12),OBSERVACION(13);
	
	 private int code;
	 
	 private ActivoProperty(int c) {
	   code = c;
	 }
	 
	 public int getCode() {
	   return code;
	 }
	 
}