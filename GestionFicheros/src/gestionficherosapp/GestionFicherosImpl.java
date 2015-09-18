package gestionficherosapp;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JOptionPane;

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
		// calcular el n�mero de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila m�s
		}

		// dimensionar la matriz contenido seg�n los resultados

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
		
		//Comprobamos que  no existe y se tienen permisos
		try{
			if(file.exists()==false && carpetaDeTrabajo.canWrite()==true){
					//Creamos el directorio
					file.mkdir();
					//Mostramos por consola la confirmación
					System.out.println("Directorio creado correctamente.");
			}
			else{
					//Si baja el flujo y se entra  aquí, se muestra una ventanita informativa y se muestra un mensaje por consola
					JOptionPane.showMessageDialog(null, "El directorio ya existe.");
					System.err.println("No se pudo crear la carpeta.");
			}
		}
		//Capturamos la excepcion en caso de que falle la creación del fichero
		catch(SecurityException error){
			System.err.println("Error al crear la carpeta.");
		}
		//Este metodo actualiza la lista de archivos de dicho directorio
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Comprobamos que no existe y se tienen permisos
				try{
					if(file.exists()==false && carpetaDeTrabajo.canWrite()==true){
							try {
								//Creamos el nuevo fichero
								file.createNewFile();
								//Mostramos por consola la confirmación
								System.out.println("Fichero creado correctamente.");
							} 
							catch (IOException error) {
								System.err.println("No se ha podido crear el fichero");
							}
					}
					else{
							JOptionPane.showMessageDialog(null, "El fichero ya existe.");
							System.err.println("No se pudo crear el fichero.");
					}
				}
				//Capturamos la excepcion en caso de que falle la creación del fichero
				catch(SecurityException error){
					System.err.println("Error al crear el fichero.");
				}
				//Este metodo actualiza la lista de archivos de dicho directorio
				actualiza();
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo, arg0);
		
		//Si existe el archivo, lo eliminamos.
		try{
			if(file.exists()==true && file.canWrite()==true){
				//Eliminacion del fichero
				file.delete();
				//""
				System.out.println("Archivo eliminado correctamente.");
			}
		}
		catch(SecurityException error){
			System.err.println("No se pudo eliminar dicho archivo.");
		}
		//Este metodo actualiza la lista de archivos de dicho directorio
		actualiza();
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
		// nueva asignaci�n de la carpeta de trabajo
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
		
		//Controlar que existe. Si no, se lanzar� una excepci�n 
		//Controlar que haya permisos de lectura. Si no, se lanzar� una excepci�n
		try{
			if(file.exists()==true || file.canRead()==true){
				
				//T�tulo
				strBuilder.append("INFORMACI�N DEL SISTEMA");
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
					
				//Ubicaci�n
				strBuilder.append("Ubicacion: ");
				try{
					String rutaCanonica = file.getCanonicalPath();
					strBuilder.append(rutaCanonica);
					strBuilder.append("\n");
				}
				catch(IOException error){
					System.err.println("No se encuentra la ruta de dicho archivo.");
				}
				
				//Fecha de �ltima modificaci�n
				strBuilder.append("Ultima modificacion: ");
				try{
					Date fecha = new Date(file.lastModified());
					strBuilder.append(fecha);
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
				strBuilder.append("MAS INFORMACI�N");
				strBuilder.append("\n\n");
				
				try{
					if(file.isFile()==true){
						//Tama�o en bytes de el fichero seleccionado
						strBuilder.append("Tama�o: ");
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
						strBuilder.append("N� elementos: ");
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
	public void renombra(String arg0, String arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
		//File con nuevo nombre
		File file1 = new File(carpetaDeTrabajo, arg1);
		//Renombrar el fichero en caso de que exista y tenga permisos de escritura
		try{
			if(file.exists()==true && file.canWrite()==true){
						if(file.renameTo(file1)==true){
							System.out.println("Archivo renombrado correctamente");
						}
					}
		}
		catch(SecurityException error){
			System.err.println("No se pudo renombrar el archivo.");
		}
		//Este metodo actualiza la lista de archivos de dicho directorio
		actualiza();
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

		// se controla que la direcci�n exista y sea directorio
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
