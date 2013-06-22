package com.nahmens.inventario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Inventario {

	private String id;
	private String user;
	
	private HashMap<String, String> data;
	private List<byte[]> imageMedia;
	private List<byte[]> audioMedia;
	
	public Inventario(String id, String user){

		this.id = id;
		this.data = new HashMap<String, String>();
		this.setImageMedia(new ArrayList<byte[]>());
		this.setAudioMedia(new ArrayList<byte[]>());
		this.user = user;

	}
	public String getId() {
		return id;
	}
	public HashMap<String, String> getData() {
		return data;
	}
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}
	public List<byte[]> getImageMedia() {
		return imageMedia;
	}
	public void setImageMedia(List<byte[]> imageMedia) {
		this.imageMedia = imageMedia;
	}
	public List<byte[]> getAudioMedia() {
		return audioMedia;
	}
	public void setAudioMedia(List<byte[]> audioMedia) {
		this.audioMedia = audioMedia;
	}
	public String getUser() {
		return user;
	}

	public static final String PROYECTO = "proyecto";
	public static final String PLANTA = "planta";
	public static final String AREA = "area";
	public static final String EDIFICIO ="edificio";
	public static final String DEPARTAMENTO ="departamento";
	public static final String PISO = "piso";
	public static final String LAMINA_ACERO_CARBONO = "laminaAceroCarbono";
	public static final String LAMINA_ACERO_INOXIDABLE = "laminaAceroInoxidable";
	public static final String RECUBIERTO_MATERIA_AISLANTE = "recubiertoMateriaAislante";
	public static final String MARCA ="marca";
	public static final String FABRICANTE = "fabricante";
	public static final String MODELO ="modelo";
	public static final String PAIS_ORIGEN = "paisOrigen";
	public static final String SERIAL = "serial";
	public static final String CODIGO_PLANTA = "codigoPlanta";
	public static final String TIPO = "tipo";
	public static final String CAPACIDAD = "capacidad";
	public static final String DIAMETRO = "diametro";
	public static final String LONGITUD = "longitud";
	public static final String ANCHO = "ancho";
	public static final String ALTURE ="altura";
	public static final String MOTOR_ELECTRICO = "motorElectrico";
	public static final String MOTOR_ELECTRICO_KW = "motorElectricoKw";
	public static final String MOTOR_ELECTRICO_HP = "motorElectricoHp";
	public static final String RPM = "rpm";
	public static final String ACOPLADO_REDUCTOR_VELOCIDAD ="acopladoReductorVelocidad";
	public static final String ACOPLADO_ATRAVES_DE="acopladoAtravesDe";
	public static final String COMPLETO_CON = "completoCon";
	public static final String BASE_PROPIA = "basePropia";
	public static final String ESTRUCTURA_SOPORTE_PERFILES_METALICOS_APOYADOS_PISO = "estructuraSoportePerfilesMetalicosApoyadoPiso";
	public static final String ESTRUCTURA_SOPORTE_PERFILES_METALICOS_APERNADOS_PISO = "estructuraSoportePerfilesMetalicosApernadaPiso";
	public static final String ESTADO_ACTUAL ="estadoActual";
	public static final String FUNCIONANDO ="funcionando";
	public static final String FECHA_FABRICACION ="fechaFabricacion";
	public static final String EDAD_APARENTE = "edadAparete";
	public static final String ANOS ="anos";
	public static final String OBSERVACIONES ="observaciones";
	public static final String FLUIDO = "fluido";
	public static final String ENTRADA ="entrada";
	public static final String SALIDA = "salida";
	public static final String GRUPO_INVENTARIO ="grupoInventario";
	public static final String GRUPO_INVENTARIO_FECHA="grupoInventarioFecha";
	public static final String INVENTARIADO = "inventariado";
	public static final String INVENTARIADO_FECHA = "inventariadoFecha";
	public static final String REVISADO ="revisado";
	public static final String REVISADO_FECHA ="revisadoFecha";
	public static final String ASEGURAMIENTO_CALIDAD = "aseguramientoDeCalidad";
	public static final String ASEGURAMIENTO_CALIDAD_FECHA ="aseguramientoDeCalidadFecha";
	public static final String TRANSCRIPCION = "transcripcion";
	public static final String TRANSCRIPCION_FECHA ="transcripcionFecha";
	public static final String FOTO ="foto";
	public static final String LAST_SAVED ="lastSaved";
	public static final String LAST_SYNC  ="lastSync";
	public static final String NOMBRE  ="nombre";
	public static final String CODIGO_VASA  ="codigoVasa";
	public static final String CODIGO_CLIENTE  ="codigoCliente";
	public static final String DIMENDIONES  ="dimensiones";
	public static final String DIMENDIONES_L  ="dimensionesL";
	public static final String DIMENDIONES_A  ="dimensionesA";
	public static final String DIMENDIONES_H  ="dimensionesH";
	public static final String ACCIONADO_LIST  ="accionadoList";
	public static final String ACCIONADO_DE  ="accionadoDe";
	public static final String ACCIONADO_RADIO  ="accionadoRadio";
	public static final String ACCIONADO_POR  ="accionadoPor";
	public static final String COMPLETO  ="completo";
	public static final String RECUBIERTO_CON  ="recubiertoCon";
	public static final String CONSTRUIDO_CON  ="construidoCon";
	public static final String CONSTRUIDO_E  ="construidoE";
	public static final String FLUIDO_ENTRADA  ="fluidoE";
	public static final String FLUIDO_SALIDA  ="FluidoS";
	public static final String MONTADO_SOBRE  ="montadoSobre";
	public static final String CONDICION  ="condicion";
	public static final String ESTIMADO  ="estimado";
	public static final String EJE_BOMBA ="ejeBomba";
	public static final String DESCRIPCION ="descripcion";
	public static final String MOTOR ="motor";
	public static final String KM ="km";
	public static final String CILINDROS ="cilindros";
	public static final String PLACA ="placa";

}
