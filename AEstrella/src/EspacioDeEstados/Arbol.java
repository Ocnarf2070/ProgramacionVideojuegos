
package EspacioDeEstados;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.naming.PartialResultException;

/**
 * Codigo de la asignatura "IA en juegos".
 * Departamento de Lenguajes y Ciencias de la Computacion.
 * Universidad de Malaga.
 *
 *
 * Representacion de un arbol de busqueda basico en espacios de estados. El arbol se representa 
 * internamente como una tabla donde la clave es un estado y el contenido informacion sobre el
 * mismo de la clase Nodo.
 *
 * @authors Esteban Calderon Pinto (2005), Jose Miguel Horcas Aguilera (2009), Lorenzo Mandow (2013)
 * 
 * version 04-11-2013
 */

public class Arbol {

    private Map<Object,Nodo> contenido;
    
    /**
     * Construye un arbol vacio.
     */
    public Arbol() {
        this.contenido = new Hashtable<Object, Nodo>();
        
    }
   
    /**
     * Construye un arbol con raiz en el estado e.
     * 
     * @param e Raiz del arbol
     */
    public Arbol(Estado e) {
        this.contenido = new Hashtable<Object, Nodo>();
        
        Nodo n = new Nodo(null);        // el padre de la raiz es null.
        this.insertarEnArbol(e, n);      
    }
   
    /**
     * 
     * @return El arbol.
     */
    public Map<Object, Nodo> contenido(){
        return contenido;
    }

    public void insertarEnArbol(Estado e, Nodo n) {
        contenido.put(e, n);
    }

    public boolean perteneceAlArbol(Estado e) {
        return contenido.containsKey(e);
    }

    /**
     * @param e un Estado .
     * @return en Nodo asociado al estado e en el arbol.
     */
    
    public Nodo nodo(Estado e) {
        return contenido.get(e);
    }

    /**
     * @param e Estado a comprobar.
     * @return Verdero si el estado e es estado raiz del arbol, falso en otro caso.
     */
    public boolean esRaiz(Estado e) {
        return perteneceAlArbol(e) && nodo(e).padre()==null; 
    }

    public LinkedList<Estado> camino(Estado e) {      
        LinkedList<Estado> c = new LinkedList<Estado>();
        if (esRaiz(e)) {
            c.add(e);
            return c;
        }   
        c = this.camino(nodo(e).padre());
        c.add(e);
        return c;
    }

    public int nNodos() {
        return contenido.size();
    }
    

}
