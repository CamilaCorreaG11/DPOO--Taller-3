package uniandes.dpoo.aerolinea.persistencia;
import java.io.IOException;

	import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
	import uniandes.dpoo.aerolinea.modelo.Aerolinea;

public interface IPersistenciaAerolinea {

	/**
	 * Carga la información de todos los elementos de una aerolínea, excepto los clientes y tiquetes , y actualiza la estructura de objetos que se encuentra dentro de la aerolínea     
	 * @param archivo La ruta al archivo que contiene la información que se va a cargar
     * @param aerolinea La aerolínea dentro de la cual debe almacenarse la información
     * @throws IOException Se lanza esta excepción si hay problemas leyendo el archivo
     * @throws InformacionInconsistenteException Se lanza esta excepción si hay información inconsistente dentro del archivo, o entre el archivo y el estado de la aerolínea
     */
    public void cargarAerolinea( String archivo, Aerolinea aerolinea ) throws IOException, InformacionInconsistenteException;

    /**
     * Salva en un archivo toda la información de todos los elementos de una aerolinea excepto los clientes y los tiquetes 
     * @param archivo La ruta al archivo donde debe quedar almacenada la información
     * @param aerolinea La aerolínea que tiene la información que se quiere almacenar
     * @throws IOException Se lanza esta excepción si hay problemas escribiendo el archivo
     */
    public void salvarAerolinea( String archivo, Aerolinea aerolinea ) throws IOException;

}
	
	
