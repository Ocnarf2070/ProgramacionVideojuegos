package EspacioDeEstados;


/**
 * Contenedor para un valor numerico y un estado. Se utiliza como elemento para insertar
 * en un heap, ordenado por el valor numerico.
 * 
 * @author Jose Miguel Horcas Aguilera, Lorenzo Mandow
 * @version 2013-10-08
 */
public class FE implements Comparable<FE>
{
    private Estado e;
    private int f;

    /**
     * Constructor 
     */
    public FE(int f, Estado e)
    {
        this.f = f;
        this.e = e;
    }

    /**
     * @return el valor de f 
     */
    public int f()
    {
        return this.f;
    }
    
    /**
     * @return el valor de e 
     */
    public Estado estado()
    {
        return this.e;
    }
    
    public boolean equals(Object obj){
        return obj instanceof FE && e.equals(((FE)obj).estado());
    }
    
    public int compareTo(FE e1) {
        if (f < e1.f()){
            return -1;
        } else if (f > e1.f()){
            return 1;
        } else {
            return 0;
        }
    }
    @Override
    public String toString() {
    	return e+" "+f;
    }
    
}
