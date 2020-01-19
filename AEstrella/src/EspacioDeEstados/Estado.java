package EspacioDeEstados;

import java.util.List;

/**
 * Representacion abstracta de un estado en un espacio de estados implicito.
 * El parametro E de la clase debe ser la propia clase que hereda de Estado. Las clases herederas
 * deben implementar equals, toString e hijos.
 * 
 * @author Lorenzo Mandow
 * 
 * @version 04-11-2013
 */
public abstract class Estado {

    /**
     * Metodo para el calculo de todos los sucesores directamente accesibles desde el estado en el espacio de estados.
     * 
     * @return Lista de estados directamente accesibles desde el estado.
     */
    public abstract List<? extends Estado> hijos();
   
    /**
     * 
     * Metodo para el calculo del coste de un arco en el espacio de estados desde el estado actual hasta e.
     * Supone que e es directamente accesible desde el estado actual.
     * 
     * @return coste de la transicion desde el estado actual al estado e (por defecto 1).
     */
    public int coste(Estado e){
        return 1;
    }     
        
    /**
     * Cadena de caracteres que representa univocamente cada estado.
     * 
     * @return cadena que muestra el contenido del estado de forma univoca (a cada
     * estado diferente debe corresponderle una cadena diferente). Vease la definicion
     * de hashCode y equals para esta clase.
     */
    public abstract String toString ();
    
    /**
     * Por defecto utilizaremos la cadena generada por toString(). Por ese motivo, 
     * toString() debe devolver una cadena que identifique univocamente el
     * estado.
     * 
     * Esta no es la manera mas eficiente de implementar equals, pero puede redefinirse
     * mas adelante.
     * 
     * @param  e El estado a comaprar
     * @return  verdadero si los dos estados son iguales.
     */
    public boolean equals(Object e)
    {
        return this.getClass().equals(e.getClass()) && this.toString().equals(e.toString());
    }    
    
    
    /**
     * Por defecto utilizaremos el codigo hash de la cadena generada por toString(). Por
     * ese motivo, toString() debe devolver una cadena que identifique univocamente el
     * estado.
     * 
     * Esta no es la manera mas eficiente de implementar hashCode, pero puede redefinirse
     * mas adelante.
     * 
     * @return codigo hash del estado para indexar una tabla hash (requerido por HashMap).
     */
    
    public int hashCode ()
    {
       return this.toString().hashCode();
    }
    
    /**
     * 
     * Muestra por pantalla el estado e.
     * 
     */
    public void ver(){
        System.out.println(this.toString());
    }     
}
