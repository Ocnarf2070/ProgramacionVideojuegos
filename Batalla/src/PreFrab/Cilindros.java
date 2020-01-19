/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package PreFrab;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import Auxiliar.*;
import java.util.HashMap;
import java.util.Map;
import mygame.Disparo;

/**
 *
 * @author alumno
 */
public class Cilindros extends RigidBodyControl implements PhysicsCollisionListener,PhysicsTickListener {
    
    
    
    BulletAppState estadosFisicos;
    private Spatial geometria=null, spationalObjetivo=null; String nombre;
    private States estado;
    private States modo;
    private Vector3f pos= Vector3f.ZERO;
    private Node rootNode; private AssetManager assetManager;
    
    
    public Cilindros(float masa,float altura, float radio, Material mat, String nombre, BulletAppState estadosFisicos, Node rootNode, AssetManager assetManager){
        super(masa);
        estado = new States();
        modo = new States();
        Cylinder c = new Cylinder(50, 50, radio, altura,true);
        Geometry geom = new Geometry(nombre,c);
        geom.setMaterial(mat);
        geom.setLocalRotation(new Quaternion().fromAngles((float)(Math.PI)/2f,0 ,0 ));
        
        rootNode.attachChild(geom);
        geometria = geom;
        geometria.addControl( this );
        estadosFisicos.getPhysicsSpace().add( this );
        estadosFisicos.getPhysicsSpace().addCollisionListener(this);
        this.estadosFisicos= estadosFisicos;
        this.rootNode=rootNode;
        this.assetManager = assetManager;
        impacts.put(nombre, 0);
    }
    
    
    static Map<String, Integer> impacts = new HashMap<>();
    public void collision(PhysicsCollisionEvent event) {
        Spatial a = event.getNodeA();
        Spatial b = event.getNodeB();
        if(a.equals(geometria)){
            if(b.getName().equals("Bala")){
                Spatial origen = b.getUserData("player");
                if(!a.equals(origen)&&modo.getState()==0){
                    System.out.println(a.getName()+" "+origen);
                    activarDisparo(origen);
                }
                int num = impacts.get(a.getName());
                if(!a.equals(origen)){
                    impacts.put(a.getName(), num+1);
                    estadosFisicos.getPhysicsSpace().removeTickListener(this);
                }
            }
        }
        
        
    }
    
    
    public Spatial getGeometria() {
        return geometria;
    }
    
    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
    }
    
    
    private float fuerza=6f * this.getMass();                        
    private float velocidad=2;
    private Vector3f vel;
    private int post=0;
    private static float time=0;
    @Override
    public void physicsTick(PhysicsSpace space, float tpf){
        if (estado.getState()==1 ){
            Vector3f posObjetivo =this.spationalObjetivo.getWorldTranslation();
            Vector3f dir = posObjetivo.subtract(geometria.getWorldTranslation()).normalize() ;
            this.applyCentralForce(dir.mult(fuerza));
            //this.setLinearVelocity(dir.mult(velocidad));
            
        }
        if (estado.getState()==0){
            this.setLinearVelocity(vel.mult(2));
            
            Vector3f pos = this.getPhysicsLocation();
            if(post==0&&pos.z>=4){
                vel= new Vector3f(-1, 0, 0);
                post++;
            }
            else if(post==1&&pos.x<=-4){
                vel = new Vector3f(0, 0, -1);
                post++;
            }
            else if(post==2&&pos.z<=-4){
                vel = new Vector3f(1, 0, 0);
                post++;
            }
            else if(post==3&&pos.x>=4){
                vel = new Vector3f(0, 0, 1);
                post=0;
            }
        }
        
        
        
    }
    public void asignarObjetivo (Spatial objetivo){
        estado.changeState();
        spationalObjetivo= objetivo;
    }

    public void setVel(Vector3f vel) {
        this.vel = vel;
    }
    
    
    Disparo disparo;
    public void activarDisparo(Spatial objetivo){
        disparo=new Disparo(geometria, objetivo, estadosFisicos, rootNode, assetManager);
        geometria.addControl(disparo);
        modo.changeState();
    }
    public void desactivarDisparo(){
        geometria.removeControl(disparo);
    }

    public static Map<String, Integer> getImpacts() {
        return impacts;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
}