package uniandes.dpoo.aerolinea.persistencia;

import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;
import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.ClienteRepetidoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteTiqueteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea{

	public PersistenciaAerolineaJson() {
		super();
	}
	

	@Override
	public void cargarAerolinea(String archivo, Aerolinea aerolinea)throws IOException, InformacionInconsistenteException {
		// Carga la información de todos los elementos de una aerolínea, excepto los clientes y tiquetes , y actualiza la estructura de objetos que se encuentra dentro de la aerolínea
		
		String jsonCompleto = new String(Files.readAllBytes(new File(archivo).toPath()));
	    JSONObject raiz = new JSONObject(jsonCompleto);

	    Map<String, Aeropuerto> aeropuertos = new HashMap<>();
	    
	    cargarAeropuertos(raiz.getJSONArray("aeropuertos"), aeropuertos);
	    cargarRutas(raiz.getJSONArray("rutas"), aeropuertos, aerolinea);
	    cargarAviones(raiz.getJSONArray("aviones"), aerolinea);
	    cargarVuelos(raiz.getJSONArray("vuelos"), aerolinea);

	}
	

	@Override
	public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException {
		{
		    JSONObject raiz = new JSONObject();

		    salvarAeropuertos(aerolinea, raiz);
		    salvarRutas(aerolinea, raiz);
		    salvarAviones(aerolinea, raiz);
		    salvarVuelos(aerolinea, raiz);

		    Files.write(new File(archivo).toPath(),raiz.toString(2).getBytes());
		}
	}
		
	private void salvarRutas(Aerolinea aerolinea, JSONObject jobject)
		{
		    JSONArray jRutas = new JSONArray();

		    for (Ruta r : aerolinea.getRutas())
		    {
		        JSONObject jR = new JSONObject();
		        jR.put("codigoRuta", r.getCodigoRuta());
		        jR.put("origen", r.getOrigen().getCodigo());
		        jR.put("destino", r.getDestino().getCodigo());
		        jR.put("horaSalida", r.getHoraSalida());
		        jR.put("horaLlegada", r.getHoraLlegada());

		        jRutas.put(jR);
		    }

		    jobject.put("rutas", jRutas);
		}
	
	private void salvarAviones(Aerolinea aerolinea, JSONObject jobject)
	{
	    JSONArray jAviones = new JSONArray();

	    for (Avion a : aerolinea.getAviones())
	    {
	        JSONObject jA = new JSONObject();
	        jA.put("nombre", a.getNombre());
	        jA.put("capacidad", a.getCapacidad());

	        jAviones.put(jA);
	    }

	    jobject.put("aviones", jAviones);
	}
	
	private void salvarAeropuertos(Aerolinea aerolinea, JSONObject jobject)
	{
	    JSONArray jAeropuertos = new JSONArray();

	    
	    java.util.Set<String> codigos = new java.util.HashSet<>();

	    for (Ruta r : aerolinea.getRutas())
	    {
	        Aeropuerto origen = r.getOrigen();
	        Aeropuerto destino = r.getDestino();

	        agregarAeropuertoSiNoEsta(origen, codigos, jAeropuertos);
	        agregarAeropuertoSiNoEsta(destino, codigos, jAeropuertos);
	    }

	    jobject.put("aeropuertos", jAeropuertos);
	}

	private void agregarAeropuertoSiNoEsta(Aeropuerto a, java.util.Set<String> codigos, JSONArray jAeropuertos)
	{
	    if (a == null) return;

	    String codigo = a.getCodigo();
	    if (codigos.contains(codigo)) return;

	    JSONObject jA = new JSONObject();
	    jA.put("codigo", a.getCodigo());
	    jA.put("latitud", a.getLatitud());
	    jA.put("longitud", a.getLongitud());
	    jA.put("nombre", a.getNombre());
	    jA.put("nombreCiudad", a.getNombreCiudad()); // usa la llave que te pidan en el JSON

	    jAeropuertos.put(jA);
	    codigos.add(codigo);
	}
	
	private void salvarVuelos(Aerolinea aerolinea, JSONObject jobject)
	{
	    JSONArray jVuelos = new JSONArray();

	    for (Vuelo v : aerolinea.getVuelos())
	    {
	        JSONObject jV = new JSONObject();
	        jV.put("fecha", v.getFecha());
	        jV.put("ruta", v.getRuta().getCodigoRuta());
	        jV.put("avion", v.getAvion().getNombre());

	        jVuelos.put(jV);
	    }

	    jobject.put("vuelos", jVuelos);
	}
	
	private void cargarVuelos(JSONArray jVuelos, Aerolinea aerolinea) throws InformacionInconsistenteException
	{
	    for (int i = 0; i < jVuelos.length(); i++)
	    {
	        JSONObject jV = jVuelos.getJSONObject(i);

	        String fecha = jV.getString("fecha");
	        String codigoRuta = jV.getString("ruta");
	        String nombreAvion = jV.getString("avion");

	        try
	        {
	            aerolinea.programarVuelo(fecha, codigoRuta, nombreAvion);
	        }
	        catch (Exception e)
	        {
	            throw new InformacionInconsistenteException("No se pudo programar el vuelo " + codigoRuta + " " + fecha + ": " + e.getMessage());
	        }
	    }
	}
	
	private void cargarAeropuertos(JSONArray jAeropuertos, Map<String, Aeropuerto> aeropuertos)
	        throws InformacionInconsistenteException
	{
	    for (int i = 0; i < jAeropuertos.length(); i++)
	    {
	        JSONObject jA = jAeropuertos.getJSONObject(i);

	        String codigo = jA.getString("codigo");
	        double lat = jA.getDouble("latitud");
	        double lon = jA.getDouble("longitud");
	        String nombre = jA.getString("nombre");
	        String ciudad = jA.getString("nombreCiudad");

	        try
	        {
	            Aeropuerto a = new Aeropuerto(nombre, codigo, ciudad, lat, lon);
	            aeropuertos.put(codigo, a);
	        }
	        catch (AeropuertoDuplicadoException e)
	        {
	            throw new InformacionInconsistenteException("Aeropuerto duplicado: " + codigo);
	        }
	    }
	}

	private void cargarRutas(JSONArray jRutas, Map<String, Aeropuerto> aeropuertos, Aerolinea aerolinea)throws InformacionInconsistenteException
	{
	    for (int i = 0; i < jRutas.length(); i++)
	    {
	        JSONObject jR = jRutas.getJSONObject(i);

	        String codigoRuta = jR.getString("codigoRuta");
	        String codOrigen = jR.getString("origen");
	        String codDestino = jR.getString("destino");
	        String horaSalida = jR.getString("horaSalida");
	        String horaLlegada = jR.getString("horaLlegada");

	        Aeropuerto origen = aeropuertos.get(codOrigen);
	        Aeropuerto destino = aeropuertos.get(codDestino);

	        if (origen == null || destino == null) throw new InformacionInconsistenteException("Ruta " + codigoRuta + " referencia aeropuertos inexistentes");
	       
	        Ruta ruta = new Ruta(origen, destino, horaSalida, horaLlegada , codigoRuta);

	        aerolinea.agregarRuta(ruta);
	    }
	}

	private void cargarAviones(JSONArray jAviones, Aerolinea aerolinea)
	{
	    for (int i = 0; i < jAviones.length(); i++)
	    {
	        JSONObject jAv = jAviones.getJSONObject(i);
	        Avion avion = new Avion(jAv.getString("nombre"), jAv.getInt("capacidad"));
	        aerolinea.agregarAvion(avion);
	    }
	}


	
}
