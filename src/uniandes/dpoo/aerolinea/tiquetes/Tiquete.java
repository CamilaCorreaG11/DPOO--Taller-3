package uniandes.dpoo.aerolinea.tiquetes;

import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.Vuelo;


public class Tiquete {
/**
 * Esta clase agrupa la información de un tiquete, expedido para un vuelo específico
 *  en una cierta fecha, y que fue comprado por un cliente. Cuando se crea, un tiquete 
 *  no está usado. Después de que se haya realizado el vuelo, el tiquete debe quedar marcado como usado.
 */
	
	
	private Cliente cliente;
	private Vuelo vuelo;
	private String codigo;
	private int tarifa;
	private boolean usado;
	
	public Tiquete(String codigo,  Vuelo vuelo, Cliente cliente, int tarifa) {
		super();
		this.cliente = cliente;
		this.vuelo = vuelo;
		this.codigo = codigo;
		this.tarifa = tarifa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Vuelo getVuelo() {
		return vuelo;
	}

	public String getCodigo() {
		return codigo;
	}

	public int getTarifa() {
		return tarifa;
	}
	
	
	public void marcarComoUsado() {
		this.usado = true;
		
	}
	
	public boolean esUsado() {
		return this.usado;
		
	}
	
	
}
