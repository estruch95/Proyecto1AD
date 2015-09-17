package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {

		System.out.println("holaaa");
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		//que no exista -> lanzará una excepción
		//crear la carpeta -> lanzará una excepción
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción 
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
		try{
			if(file.exists()==true || file.canRead()==true){
				
				//Título
				strBuilder.append("INFORMACIÓN DEL SISTEMA");
				strBuilder.append("\n\n");
				
				//Nombre
				strBuilder.append("Nombre: ");
				strBuilder.append(arg0);
				strBuilder.append("\n");
				
				//Tipo: fichero o directorio
				strBuilder.append("Tipo de archivo: ");
				try{
					if(file.isDirectory()==true){
						strBuilder.append("Directorio");
						strBuilder.append("\n");
					}
					else if(file.isFile()==true){
						strBuilder.append("Fichero");
						strBuilder.append("\n");
					}
				}
				catch(SecurityException error){
					System.err.println("Error al detectar el tipo de archivo.");
				}
					
				//Ubicación
				strBuilder.append("Ubicacion: ");
				try{
					String rutaCanonica = file.getCanonicalPath();
					strBuilder.append(rutaCanonica);
					strBuilder.append("\n");
				}
				catch(IOException error){
					System.err.println("No se encuentra la ruta de dicho archivo.");
				}
				
				//Fecha de última modificación
				strBuilder.append("Ultima modificacion: ");
				try{
					Long fechaUltimaMod = file.lastModified();
					String fechaUltimaModificacion = String.valueOf(fechaUltimaMod);
					
					// Creacion de un formato de fecha
					SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
					try {
						//Conversion a dato de tipo Date el String que recoje la fecha de la ultima modificacion del archivo
						Date fecha = (Date) formatoFecha.parse(fechaUltimaModificacion);
					} catch (ParseException error) {
							System.err.println("Error al convertir a formato de fecha.");
					}
					strBuilder.append(fechaUltimaModificacion);
					strBuilder.append("\n");
				}
				catch(SecurityException  error){
					System.err.println("Error al detectar la fecha de ultima modificacion de dicho fichero.");
				}
				
				//Si es un fichero oculto o no
				strBuilder.append("Oculto?: ");
				try{
					if(file.isHidden()==true){
						strBuilder.append("Si");
						strBuilder.append("\n\n");
					}
					else{
						strBuilder.append("No");
						strBuilder.append("\n\n");
					}
				}
				catch(SecurityException error){
					System.err.println("Error al detectar si el fichero es oculto o no.");
				}
				
				//Si es directorio: Espacio libre, espacio disponible, espacio total
				strBuilder.append("MAS INFORMACIÓN");
				strBuilder.append("\n\n");
				
				try{
					if(file.isFile()==true){
						//Tamaño en bytes de el fichero seleccionado
						strBuilder.append("Tamaño: ");
						//Almacenamiento de datos en variable
						Long tamañoBytes = file.length();
						//Conversion de datos tipo Long a String
						String tBytes = String.valueOf(tamañoBytes);
						//Concatenacion a la cadena 
						strBuilder.append(tBytes+" bytes");
						strBuilder.append("\n");
					}
					else if(file.isDirectory()==true){
						//Numero de elementos que contiene el directorio seleccionado
						strBuilder.append("Nº elementos: ");
						Long numArchivos = file.length();
						String nArchivos = String.valueOf(numArchivos);
						strBuilder.append(nArchivos+" en total");
						
						//Salto de linea
						strBuilder.append("\n");
					
						//Espacio libre del que consta el directorio seleccionado
						strBuilder.append("Espacio libre: ");
						String espacioLibre = String.valueOf(file.getFreeSpace());
						strBuilder.append(espacioLibre+" bytes libres");
						
						strBuilder.append("\n");
						
						//Espacio disponible
						strBuilder.append("Espacio disponible: ");
						String espacioDisponible = String.valueOf(file.getUsableSpace());
						strBuilder.append(espacioDisponible+" bytes disponibles");
						
						strBuilder.append("\n");
						
						//Espacio total
						strBuilder.append("Espacio total: ");
						String espacioTotal = String.valueOf(file.getTotalSpace());
						strBuilder.append(espacioTotal+" de espacio total");
						
						strBuilder.append("\n");
			
					}
				}
				catch(RuntimeException error){
					System.err.println("Error al detectar el tipo de archivo.");
				}
			}
		}
		catch(RuntimeException error){
			System.err.println("El archivo no existe o no tiene permisos de lectura.");
		}
		
		return strBuilder.toString();
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
