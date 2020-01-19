package EspacioDeEstados;

import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Problema para Es de clase E. El problema viene definido por el E inicial y
 * la condicion objetivo (metodo esFinal).
 * 
 * @author Jose Miguel Horcas Aguilera, Lorenzo Mandow
 * 
 * @version 08-10-2013
 */
public abstract class Problema<E extends Estado> { // extends E> {
    public E ini;
    public int iteraciones;
    
    /**
     * 
     * @return Verdadero si el E es un E objetivo, falso en otro caso.
     */
    public int iteraciones() {
    	return iteraciones;
    }
    
    public abstract boolean esFinal(E e);
    
    /**
     * 
     * @return el E inicial del problema. Debe iniciarse al construir la instancia.
     */
    public E ini() {
        return this.ini;
    };
    
    /**
     * 
     * Establece el E inicial del problema.
     */
    public void establecerInicial(E e) {
        this.ini = e;
    };
    
   /**
     * Heuristico por defecto (valor 0 para todos los Es).
     *
     * @param  e   E
     * @return     estimacion del coste de un camino desde e hasta un E final.
     */
    public int h(E e)
    {
        return 0;
    }

       
    /*******************************************************************************************
     * Implementacion del metodo de búsqueda a ciegas "primero en amplitud".
     * 
     * @return List de Es que llevan del inicial a uno que cumple la condicion
     *         objetivo del problema (metodo esFinal).
     */
       
    public LinkedList<E> amplitud (){
        E e = this.ini();
        Arbol a = new Arbol();
        a.insertarEnArbol(e, new Nodo(null));
        int i=0;
        LinkedList<E> abiertos = new LinkedList<E>();
        iteraciones=0;

               
        while (e!=null && !this.esFinal(e)) {
            iteraciones++;
            System.out.println(e.toString()+" h"+i+" "+e.hijos());
           // List lista = e.hijos();
            
            for (Estado e2 : e.hijos()){//lista) {//Ettado.hijos()) {
                if (!a.perteneceAlArbol(e2)) {
                    abiertos.offer((E)e2);
                    a.insertarEnArbol(e2, new Nodo(e));
                }
            }
            i++;
            e = abiertos.poll();
        }

        if (e==null) {
            return null;
        } else {
            return (LinkedList<E>)a.camino(e);
        }
    }

    
    /*******************************************************************************************
     * Funcion que realiza una búsqueda con retroceso a ciegas con una cota de profundidad.
     * 
     * @param cota Cota de profundidad maxima (la raiz tiene cota 0).
     * 
     * @return Camino solucion (lista de Es).
     */
    public List<E> backtrackC(int cota) {
        return backtrackC(this.ini, cota, 0);
    }

    /**
     * Funcion recursiva que realiza una búsqueda con retroceso a ciegas.
     * 
     * @param e     E inicial.
     * @param cota  Cota de profundidad.
     * @param prof  Profundidad actual.
     * @return      Camino solucion (lista de Es).
     */
    private List<E> backtrackC(E e, int cota, int prof) {
        List<E> res = new LinkedList<E>();
        
        if (this.esFinal(e)) {
            res.add(0, e);
            return res;
        } else if (prof >= cota) {
            return null;                                // poda
        } else {
           //List<E> lista = e.hijos();
            for (Estado e2 : e.hijos()){ //lista) {
                res = backtrackC((E)e2, cota, prof+1);
                if (res != null) {
                    res.add(0, e);
                    return res;
                }
            }
            return null;
        }
    }   
    
    /*******************************************************************************************
     * Funcion BID.
     * 
     * @return  Camino solucion (lista de Es).
     */
    /*
    public List<E> bid() {
        return bid(this.ini, 0);
    }
    */
    /**
     * Funcion recursiva BID.
     * 
     * @param e     E inicial.
     * @param cota  Cota.
     * @return      Camino solucion (lista de Es).
     */
    /*
    private List<E> bid(E e, int cota) {
        List<E> res;
                
        res =  this.backtrackC(cota);
        if (res != null) {
            return res;
        } else {
            return bid(e, cota+1);
        }
    }
    */
    /*****************************************************************************************
     * Implementacion del Algoritmo A*. Supone que el heuristico es monotono (consistente).
     * 
     * 
     * @return  Lista de Es del camino solucion desde el E inicial hasta un E final.
     */
    public List<E> aMono() {
        E estado = this.ini;
        Arbol a = new Arbol();
        PriorityQueue<FE> abiertos = new PriorityQueue<FE>();
        FE fe;
        int g2;
        
        a.insertarEnArbol(estado, new Nodo(null,0));
        iteraciones=0;
        while ((estado != null) && (!this.esFinal(estado))) {
            //List<E> lista = E.hijos();
        	iteraciones++;
            for (Estado e2 : estado.hijos()){  //lista) {
                g2 = ((Nodo)a.nodo(estado)).g() + estado.coste(e2);
                if (!a.perteneceAlArbol(e2) || g2<((Nodo)a.nodo(e2)).g()) {
                    if (a.perteneceAlArbol(e2)) {
                        int f2 = ((Nodo)a.nodo(e2)).g() + this.h((E)e2);// f antiguo
                        abiertos.remove(new FE(f2, e2));
                        
                    }
                    abiertos.offer(new FE(g2+this.h((E)e2), e2));         // f nuevo
                    a.insertarEnArbol(e2, new Nodo(estado,g2));
                }
                
                
            }           
            fe = abiertos.poll();
            if (fe == null){  //abiertos esta vacia
                estado = null;
            } else {
                estado = (E)fe.estado();
            }
        }
        if (estado == null) {
            return null;
        } else {
            List<E> solucion = (List<E>)a.camino(estado);
            System.out.println("H inicial: "+this.h(this.ini));
            System.out.println("Longitud de la solucion: " + solucion.toArray().length);
            Nodo k = a.nodo(estado);
            System.out.println("Coste de la solucion: " + (a.nodo(estado).g()));
            return solucion;
        }
    }
}
