/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franc
 */
public class Prioridades {
    private static Comparator<Geometry> comparator;
    private static List<Geometry> cola;
    private static Semaphore mutex = new Semaphore(1, true);
    private static SimpleApplication app;
    public static void start(Comparator<Geometry> comparator,SimpleApplication app){
        Prioridades.comparator=comparator;
        cola = new ArrayList<>();
        Prioridades.app= app;
    }
    static Updates thread;
    public static void updating(){
        thread = new Updates();
        thread.start();
    }
    
    public static void add(Geometry obj) throws InterruptedException{
        mutex.acquire();
        cola.add(obj);
        Collections.sort(cola, comparator);
        mutex.release();
    }
    
    public static Geometry first() throws InterruptedException{
        mutex.acquire();
        Geometry aux = cola.get(0);
        mutex.release();
        return aux;
    }

    public static List<Geometry> getCola() {
        return cola;
    }
    
    public static boolean vacio(){
        return cola.isEmpty();
    }
    
    public static Geometry remove() throws InterruptedException{
        mutex.acquire();
        Geometry aux = cola.remove(0);
        Collections.sort(cola,Objetivos.getDist());
        mutex.release();
        return aux;
    }
    
    private static void update() throws InterruptedException{
        
        while(!cola.isEmpty()){
            mutex.acquire();
            Collections.sort(cola, comparator);
            mutex.release();
        }
        
        thread.interrupt();
        
    }
    
    private static class Updates extends Thread{

        public Updates() {
        }
        

        @Override
        public void run() {
            try {
                setDaemon(true);
                Prioridades.update();
            } catch (InterruptedException ex) {
                Logger.getLogger(Prioridades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
