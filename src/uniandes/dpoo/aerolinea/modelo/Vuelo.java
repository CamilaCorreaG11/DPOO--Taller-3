package uniandes.dpoo.aerolinea.modelo;
import java.util.Map;
import java.util.Objects;

import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;
import java.util.HashMap;
import java.util.Collection;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;

public class Vuelo {

	private Ruta ruta;
	private String fecha;
	private Avion avion; 
	private Map<String, Tiquete> tiquetes;
	
	public Vuelo(Ruta ruta, String fecha, Avion avion) {
		this.ruta = ruta;
		this.fecha = fecha;
		this.avion = avion;
		this.tiquetes = new HashMap<String, Tiquete>();
		
	}

	public Ruta getRuta() {
		return ruta;
	}


	public String getFecha() {
		return fecha;
	}


	public Avion getAvion() {
		return avion;
	}

	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes.values();
	}

	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) throws VueloSobrevendidoException{
		int total = 0;
		// verificar que hay cupo en el vuelo
		
		if (this.tiquetes.size() + cantidad > this.getAvion().getCapacidad()) {
			throw new VueloSobrevendidoException(this);
		}
		
		//calcular precio tiquetes
		
		int precio = calculadora.calcularTarifa(this, cliente);
		
		for (int i = 0; i < cantidad; i++) {
			Tiquete tiq = GeneradorTiquetes.generarTiquete(this, cliente, precio);
			GeneradorTiquetes.registrarTiquete(tiq);
			
			this.tiquetes.put(tiq.getCodigo(), tiq);
			cliente.agregarTiquete(tiq);
			
			total += precio;
		}
		
		return total;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Vuelo otro = (Vuelo) obj;
		return Objects.equals(this.ruta, otro.ruta) && Objects.equals(this.fecha,  otro.fecha);
		
	}
	
	@Override
	
	public int hashCode() {
		return Objects.hash(ruta,fecha);
		
	}
	
}
