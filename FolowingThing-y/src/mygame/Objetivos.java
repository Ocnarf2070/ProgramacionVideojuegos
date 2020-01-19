/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import java.util.Comparator;
import java.util.Random;
import javafx.util.Pair;

/**
 *
 * @author franc
 */
public class Objetivos{
    
    
    private static int tamaño;
    private static Distance dist;
    
    public static void start(int tam,Geometry or,SimpleApplication app) throws InterruptedException{
        tamaño=tam;
        dist = new Distance(or);
        Prioridades.start(dist,app);
        Oculto.start();
        Box b = new Box(1,1,1);
        /*BoundingBox contorno2 = new BoundingBox();
        b.setBound ( contorno2 );
        b.updateBound();*/
        Material matObj = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matObj.setColor("Color", ColorRGBA.Red);
        Random rnd = new Random();
        for(int i=0;i<tamaño/1000;i++){
            if(i<tamaño/3000){
                Geometry obj = new Geometry("obj"+i, b);
                obj.setMaterial(matObj);
                Pair<Integer,Integer> lugar = Libres.remove(rnd.nextInt(Libres.tam()));
                obj.setLocalTranslation(lugar.getKey(), b.yExtent, lugar.getValue());
                Prioridades.add(obj);
                app.getRootNode().attachChild(obj);
            }
            else{
                Geometry obj = new Geometry("obj"+i, b);
                obj.setMaterial(matObj);
                Pair<Integer,Integer> lugar = Libres.remove(rnd.nextInt(Libres.tam()));
                obj.setLocalTranslation(lugar.getKey(), b.yExtent, lugar.getValue());
                Oculto.add(obj);
            }
        }
        Cylinder cono = new Cylinder(30, 30, 0.5f, 0.01f, 2f, true, false);
        Material matPUP = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        matPUP.setColor("Color", ColorRGBA.Green);
        Prioridades.updating();
    }

    public static Distance getDist() {
        return dist;
    }
    
    
    public static void verDistCola(){
        System.out.println("Posicion Origen: "+dist.getOrigin().getWorldTranslation());
        for(Geometry aux: Prioridades.getCola()){
            System.out.print(aux.getName()+"("+aux.getWorldTranslation()+"): "+aux.getWorldTranslation().distance(dist.getOrigin().getWorldTranslation())+", ");
        }
        System.out.println();
        
    }
    
    
    private static class Distance implements Comparator<Geometry>{
        private final Geometry origin;

        public Distance(Geometry origin) {
            this.origin = origin;
        }
        
        @Override
        public int compare(Geometry t, Geometry t1) {
            Vector3f origen = origin.getWorldTranslation();
            Vector3f destino1 = t.getWorldTranslation();
            Vector3f destino2 = t1.getWorldTranslation();
            float D1 = destino1.distance(origen);
            float D2 = destino2.distance(origen);
            if(D1<D2)return -1;
            else if(D1==D2)return 0;
            else return 1;
        }

        public Geometry getOrigin() {
            return origin;
        }
        
    }
    
    
}
