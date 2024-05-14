package com.practica.lista;

import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class ListaContactos {
	private NodoTemporal lista;
	private int size;
	
	/**
	 * Insertamos en la lista de nodos temporales, y a la vez inserto en la lista de nodos de coordenadas. 
	 * En la lista de coordenadas metemos el documento de la persona que está en esa coordenada 
	 * en un instante 
	 */
	public void insertarNodoTemporal (PosicionPersona p) {
		NodoTemporal aux = lista, ant=null;
		boolean salir=false,  encontrado = false;
		/**
		 * Busco la posición adecuada donde meter el nodo de la lista, excepto
		 * que esté en la lista. Entonces solo añadimos una coordenada.
		 */
		while (aux!=null && !salir) { // recorriendo la lista de contactos
			// y recorrer para buscar algo no debe ser asunto de esta clase, sino de Listacoordenada. 
			if(aux.getFecha().compareTo(p.getFechaPosicion())==0) { // Mirar si se encuentra el nodo temporal del fecha de p
				encontrado = true;
				salir = true;
				// encontrado y insertamos en la lista coordenada de este nodo temporal, el NodoPosicion que se corresponde con el de p
				// sumandole 1 en caso de que si existe este coordenada en este nodo temporal
				/**
				 * Insertamos en la lista de coordenadas
				 */
				NodoPosicion npActual = aux.getListaCoordenadas();
				NodoPosicion npAnt=null;		
				boolean npEncontrado = false;
				while (npActual!=null && !npEncontrado) { // recorrer lista coordenada de este nodo temporal
					if(npActual.getCoordenada().equals(p.getCoordenada())) {
						npEncontrado=true;
						npActual.setNumPersonas(npActual.getNumPersonas()+1);
					}else {
						npAnt = npActual;
						npActual = npActual.getSiguiente();
					}
				}
				if(!npEncontrado) { // terminar de recorrer y resulta que no existe este coordenada en la lista Coordenada del nodo temporal
					// hay que insertar un nuevo coordinada (mismo que el de p) en la lista coordinada del nodo temporal
					NodoPosicion npNuevo = new NodoPosicion(p.getCoordenada(),1, null);
					if(aux.getListaCoordenadas()==null) // caso de que no encuentra es debido que no tenia ninguno coordenada en la lista
						aux.setListaCoordenadas(npNuevo);
					else // en caso de que si habia alguna coordenada, insertamos ahora el nuevo en su posicion adecuada.
						npAnt.setSiguiente(npNuevo);			
				}
			}else if(aux.getFecha().compareTo(p.getFechaPosicion())<0) {
				ant = aux;
				aux=aux.getSiguiente(); // pasar a siguiente nodo temporal (esto es cosa de recorrer la lista nodo temporal)
			}else if(aux.getFecha().compareTo(p.getFechaPosicion())>0) {
				salir=true; // resulta que es un nuevo nodo temporal, que no existe todavia en la lista de contactos
			}

		}
		/**
		 * No hemos encontrado ninguna posición temporal, así que
		 * metemos un nodo nuevo en la lista
		 */
		if(!encontrado) { // no existe este nodo temporal, hay que crear uno nuevo, metiendo en la psoicion correcta
			NodoTemporal nuevo = new NodoTemporal();
			nuevo.setFecha(p.getFechaPosicion());


			NodoPosicion npActual = nuevo.getListaCoordenadas();
			NodoPosicion npAnt=null;
			boolean npEncontrado = false;
			while (npActual!=null && !npEncontrado) { // se tiene que volver a buscar el psoicion para insertar el nodo temporal nuevo
				// esta buscando otra vez en la lista coordenada del nodo temporal nuevo si existe la coordenada de p, pero no veo necesario
				// pq su listacoordenada es nuevo, entonce va a ser nulo.
				if(npActual.getCoordenada().equals(p.getCoordenada())) {
					npEncontrado=true;
					npActual.setNumPersonas(npActual.getNumPersonas()+1);
				}else {
					npAnt = npActual;
					npActual = npActual.getSiguiente();
				}
			}
			if(!npEncontrado) { // igual que antes
				NodoPosicion npNuevo = new NodoPosicion(p.getCoordenada(),  1, null);
				if(nuevo.getListaCoordenadas()==null)
					nuevo.setListaCoordenadas(npNuevo);
				else
					npAnt.setSiguiente(npNuevo);
			}

			// Todo lo anterior no tiene sentido, ya que es una lista coordenada nueva, no va a tener nada.

			if(ant!=null) { // significa que la lista contactos no estaba vacia, insertar el nodo nuevo en su posicion
				nuevo.setSiguiente(aux);
				ant.setSiguiente(nuevo);
			}else { // la lista contactos es vacia, el nodo nuevo va su primer nodo temporal.
				nuevo.setSiguiente(lista);
				lista = nuevo;
			}
			this.size++;

		}
	}
	
	private boolean buscarPersona (String documento, NodoPersonas nodo) {
		NodoPersonas aux = nodo;
		while(aux!=null) {
			if(aux.getDocumento().equals(documento)) {
				return true;				
			}else {
				aux = aux.getSiguiente();
			}
		}
		return false;
	}
	
	private void insertarPersona (String documento, NodoPersonas nodo) {
		NodoPersonas aux = nodo, nuevo = new NodoPersonas(documento, null);
		while(aux.getSiguiente()!=null) {				
			aux = aux.getSiguiente();				
		}
		aux.setSiguiente(nuevo);		
	}
	
	public int personasEnCoordenadas () {
		NodoPosicion aux = this.lista.getListaCoordenadas();
		if(aux==null)
			return 0;
		else {
			int cont;
			for(cont=0;aux!=null;) {
				cont += aux.getNumPersonas();
				aux=aux.getSiguiente();
			}
			return cont;
		}
	}
	
	public int tamanioLista () {
		return this.size;
	}

	public String getPrimerNodo() {
		NodoTemporal aux = lista;
		String cadena = aux.getFecha().getFecha().toString();
		cadena+= ";" +  aux.getFecha().getHora().toString();
		return cadena;
	}

	/**
	 * Métodos para comprobar que insertamos de manera correcta en las listas de 
	 * coordenadas, no tienen una utilidad en sí misma, más allá de comprobar que
	 * nuestra lista funciona de manera correcta.
	 */
	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		if(this.size==0)
			return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		int a;
		cont = 0;
		while(aux!=null) {
			if(aux.getFecha().compareTo(inicio)>=0 && aux.getFecha().compareTo(fin)<=0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while(nodo!=null) {
					cont = cont + nodo.getNumPersonas();
					nodo = nodo.getSiguiente();
				}				
				aux = aux.getSiguiente();
			}else {
				aux=aux.getSiguiente();
			}
		}
		return cont;
	}
	
	
	
	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		if(this.size==0)
			return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		int a;
		cont = 0;
		while(aux!=null) {
			if(aux.getFecha().compareTo(inicio)>=0 && aux.getFecha().compareTo(fin)<=0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while(nodo!=null) {
					cont = cont + 1;
					nodo = nodo.getSiguiente();
				}				
				aux = aux.getSiguiente();
			}else {
				aux=aux.getSiguiente();
			}
		}
		return cont;
	}
	
	
	
	@Override
	public String toString() {
		String cadena="";
		int a,cont;
		cont=0;
		NodoTemporal aux = lista;
		for(cont=1; cont<size; cont++) {
			cadena += aux.getFecha().getFecha().toString();
			cadena += ";" +  aux.getFecha().getHora().toString() + " ";
			aux=aux.getSiguiente();
		}
		cadena += aux.getFecha().getFecha().toString();
		cadena += ";" +  aux.getFecha().getHora().toString();
		return cadena;
	}
	
	
	
}
