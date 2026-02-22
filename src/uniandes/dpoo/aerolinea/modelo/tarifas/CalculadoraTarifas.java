package uniandes.dpoo.aerolinea.modelo.tarifas;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;

public abstract class CalculadoraTarifas {

	public static final double IMPUESTO = 0.28;

	public CalculadoraTarifas() {
		super();
	}
	
	
	public int calcularTarifa(Vuelo vuelo, Cliente cliente) {
		int costoBase = calcularCostoBase(vuelo,cliente);
		double descuento = costoBase* calcularPorcentajeDescuento(cliente);
		int subtotal = (int) (costoBase - descuento) ;
		int impuestos = calcularValorImpuestos(subtotal);
		
		return subtotal + impuestos;
		
	}
	
	//Este método calcula cuál debe ser el costo base dado el vuelo y el cliente.
	protected abstract int calcularCostoBase (Vuelo vuelo, Cliente cliente);
	//Calcula el porcentaje de descuento que se le debería dar a un cliente dado su tipo y/o su historia. El método retorna un número entre 0 y 1: 0 significa que no hay descuento, y 1 significa que el descuento es del 100%.
	protected abstract double calcularPorcentajeDescuento(Cliente cliente);
	
	protected int calcularDistanciaVuelo(Ruta ruta) {
		return Aeropuerto.calcularDistancia(ruta.getOrigen(), ruta.getDestino());
		
	}
	
	protected int calcularValorImpuestos(int costoBase) {
		return (int) (costoBase*IMPUESTO);
		
	}
	
	
	
	
}
