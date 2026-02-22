package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;


public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas{

	
	protected int COSTO_POR_KM_CORPORATIVO = 900 ;
	protected int COSTO_POR_KM_NATURAL = 600 ;
	protected double DESCUENTO_GRANDES = 0.2;
	protected double DESCUENTO_MEDIANAS = 0.1;
	protected double DESCUENTO_PEQ = 0.02;
	
	public CalculadoraTarifasTemporadaBaja() {
		super();
	}

	@Override
	protected int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
		int distancia = this.calcularDistanciaVuelo(vuelo.getRuta());
		
		if (cliente.getTipoCliente().equalsIgnoreCase("corporativo")) {
			return COSTO_POR_KM_CORPORATIVO*distancia;
		}		

		else {
			return COSTO_POR_KM_NATURAL*distancia;
			
		}
	}

	@Override
	protected double calcularPorcentajeDescuento(Cliente cliente) {
		
		if (cliente instanceof ClienteCorporativo) {
			ClienteCorporativo corp = (ClienteCorporativo) cliente;
		    int tamano = corp.getTamanoEmpresa();
		    
		    if (tamano == 1) return DESCUENTO_GRANDES;
		    else if (tamano ==2) return DESCUENTO_MEDIANAS;
		    else if (tamano == 3) return DESCUENTO_PEQ;
		}
		
		return 0;
	}
	
	
	
}
