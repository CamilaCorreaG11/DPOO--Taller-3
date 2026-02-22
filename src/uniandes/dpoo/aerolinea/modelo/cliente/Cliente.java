package uniandes.dpoo.aerolinea.modelo.cliente;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente {

	private List<Tiquete> tiquetesSinUsar ;
	private List<Tiquete> tiquetesUsados; 


public Cliente() {
	tiquetesSinUsar = new ArrayList<>();
	tiquetesUsados = new ArrayList<>();
	}

// Retorna el tipo del cliente
public abstract String getTipoCliente();
// Retorna el identificador del cliente
public abstract String getIdentificador();

public java.util.Collection<Tiquete> getTiquetesSinUsar() {
    return tiquetesSinUsar;
}
public void agregarTiquete(Tiquete tiquete) {
		this.tiquetesSinUsar.add(tiquete);
	}


public int calcularValorTotalTiquetes() {
	int valorTotal = 0;
	for (Tiquete tiquete: this.tiquetesUsados) {
		valorTotal += tiquete.getTarifa();
	}
	
	for (Tiquete tiquete: this.tiquetesSinUsar) {
		valorTotal += tiquete.getTarifa();
	}

	return valorTotal;
}



public void usarTiquetes(Vuelo vuelo) {
	
	Iterator<Tiquete> it = this.tiquetesSinUsar.iterator();
	
	while (it.hasNext()) {
		Tiquete tiquete = it.next();
		
		if (tiquete.getVuelo().equals(vuelo)) {
			it.remove();
			tiquete.marcarComoUsado();
			this.tiquetesUsados.add(tiquete);
			
		}
		
	}
	
	
}







}