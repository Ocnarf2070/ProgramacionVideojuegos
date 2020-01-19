package mygame.PreFabs;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;



import mygame.Main;
public class ModeloFisicoEstatico extends RigidBodyControl implements PhysicsCollisionListener {
   public Spatial  modelo3D=null, geomObj=null;    
   public ModeloFisicoEstatico(float masa, Spatial geometria, Material mat, BulletAppState estadosFisicos, Node rootNode, AssetManager assetManager){
      super(masa);  
       modelo3D = geometria;
       //if (mat==null) geometria.setMaterial(Main.matPorDefecto);
      rootNode.attachChild( modelo3D);              
       modelo3D.addControl( this );                      
      estadosFisicos.getPhysicsSpace().add( this ); 
      
      //CONFIGURACION FISICA
      this.setRestitution(0.9f);
}
    public void collision(PhysicsCollisionEvent event) {
          System.out.println(event.getNodeB().getName()+" colisionó con "+event.getNodeA().getName());
}}
