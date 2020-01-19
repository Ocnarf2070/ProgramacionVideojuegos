
package EspacioDeEstados;


/**
 * Clase para guardar la informacion de un estado en un arbol de busqueda. No se comprueba que
 * la estructura guardada sea un arbol, quedando como responsabilidad del usuario.
 *
 * @authors Esteban Calderon Pinto (2005), Jose Miguel Horcas Aguilera (2009), Lorenzo Mandow (2013)
 */
public class Nodo  {

    private Estado padre;		// El estado padre
    private int g;              // El coste del camino guardado en el arbol hasta el estado

    /**
     * Constructor.
     * Construye un nuevo nodo a partir de un estado.
     * @param e El estado.
     */
    public Nodo(){
        padre = null;
    }

    /**
     * Constructor.
     * Construye un nuevo nodo a partir de un estado y su padre.
     * @param e El estado.
     * @param p El estado padre de e.
     */
    public Nodo(Estado p){
        this.padre = p;
    }

        /**
     * Constructor.
     * Construye un nuevo nodo a partir de un estado y su padre.
     * @param e El estado.
     * @param p El estado padre de e.
     * @param g El valor del coste g(e).
     */
    public Nodo(Estado p, int g){
        this.padre = p;
        this.g = g;
    }
    
    public Estado padre(){
        return this.padre;
    }

    public int g(){
        return this.g;
    }
    
    
}
