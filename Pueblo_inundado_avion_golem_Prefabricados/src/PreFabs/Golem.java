/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PreFabs;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author owner
 */
public class Golem extends BetterCharacterControl {
      float tiempoEnCirculos=0; 
      Vector3f velMaxima;
      Spatial geom_anim_personaje ; AnimChannel animaciones;
      public  Node personaje = new Node();
      
  public Golem(float radio, float altura, float masa, String nombre, BulletAppState estadosFisicos, Node rootNode,  AssetManager assetManager){
      super(radio, altura, masa );  
    geom_anim_personaje =  assetManager.loadModel("Models/Oto/Oto.mesh.xml");
    //geom_anim_personaje.scale(1.4f);
    geom_anim_personaje.move(0,6.8f,0);                                 
    personaje.attachChild(geom_anim_personaje);  
    rootNode.attachChild(personaje); 

    AnimControl animControl = geom_anim_personaje.getControl(AnimControl.class);
    animaciones = animControl.createChannel();
    //[Walk, pull, Dodge, stand, push]
     animaciones.setAnim("Walk");      
     
   //El nodo Padre se asocia el control físico especia. "BetterCharacterControl"
    //BetterCharacterControl control_Personaje= new BetterCharacterControl(radio, altura,masa); // construct character. (If your character bounces, try increasing height and weight.)
    personaje.addControl(this); 
    
    //this.setWalkDirection(new Vector3f (1.7f*2.26f,  0,  1*2.26f));
    //this.setViewDirection(this.getWalkDirection());
    estadosFisicos.getPhysicsSpace().add(this);           //O TAMBIEN   estadosFisicos.getPhysicsSpace().addAll(personaje);   
    
}
  public void ir(Vector3f velocidad){      
      animaciones.setSpeed(velocidad.length()/5f);
       this.setWalkDirection(velocidad);
      this.setViewDirection(this.getWalkDirection());
      velMaxima = velocidad;//this.getWalkDirection();
  }


public void hacerGuardia(float recorrido, float tpf){
     tiempoEnCirculos += tpf;
     Vector3f v= new Vector3f (velMaxima.x*(float)Math.cos(tiempoEnCirculos/recorrido), 0,velMaxima.z*(float)Math.cos(tiempoEnCirculos/recorrido)); 

     animaciones.setSpeed(velMaxima.length()/12f);
     this.setWalkDirection(v);
     this.setViewDirection(this.getWalkDirection());
}

public void detenerse(){
      animaciones.setAnim("stand");        
      animaciones.setLoopMode(LoopMode.DontLoop);
      this.setWalkDirection(Vector3f.ZERO);
  }

public void situar (Vector3f pos){
       this.setPhysicsLocation(pos); //.personaje.setLocalTranslation(pos);
}}