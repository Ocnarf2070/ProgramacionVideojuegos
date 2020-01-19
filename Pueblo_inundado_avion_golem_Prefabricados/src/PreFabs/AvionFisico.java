package mygame.PreFabs;

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
import com.jme3.bullet.joints.Point2PointJoint;
import com.jme3.math.Quaternion;

public class AvionFisico extends RigidBodyControl implements PhysicsTickListener , PhysicsCollisionListener {
      boolean  activarFisicas, actualizaFuerzas, controlTeclado; Vector3f pos= Vector3f.ZERO;
      BulletAppState estadosFisicos;   public Point2PointJoint remolque=null;
      public Spatial geometria=null, spationalObjetivo=null; String nombre; 

      //CONFIGURACION FISICA
      public float fuerza=200.5f * this.mass;                        //Para mover el objeto, la fuerza es proporional a la masa;
      public float velocidad=2;
     public String estado="velocidadConstante";                             //otros: "conObjetivo",  "enCaidaLibre";
      
 public AvionFisico(float masa, Material mat, String nombre, BulletAppState estadosFisicos, Node rootNode, AssetManager assetManager){
      super(masa);  

      //CONFIGURACION GEOMETRICA            
      Spatial  modelo3D = assetManager.loadModel ("Models/SpaceCraft/Rocket.mesh.xml"); 
      modelo3D.scale(3f);  
      modelo3D.setName (nombre);
      rootNode.attachChild(modelo3D);              
      geometria=modelo3D;
      geometria.addControl( this );                      
      estadosFisicos.getPhysicsSpace().add( this );  
      estadosFisicos.getPhysicsSpace().addCollisionListener(this);
      this.estadosFisicos= estadosFisicos;
      this.setGravity(Vector3f.ZERO); 
}
 
 public void physicsTick (PhysicsSpace space, float tpf){     
     if (activarFisicas) {                               
          activarFisicas=false;       
       }else {                                          
            if (estado.equals("conObjetivo")){                     //Avanza hacia su objetivo y orienta su frente en esa direccion 
                Vector3f posObjetivo =this.spationalObjetivo.getWorldTranslation();
                Vector3f dir = new Vector3f (posObjetivo.x- geometria.getWorldTranslation().x,  0,  posObjetivo.z- geometria.getWorldTranslation().z).normalize() ;          
                this.setLinearVelocity(dir.mult(velocidad));
                geometria.lookAt(posObjetivo, Vector3f.UNIT_Y);
                pos= this.getPhysicsLocation();       
                this.setPhysicsRotation( new Quaternion().fromAngles(0, 3.1416f, 0).mult (geometria.getWorldRotation())); 
                this.setPhysicsLocation(pos);                     
            }  
            if (estado.equals("velocidadConstante")){     //Avanza en direccion a la velocidad que lleva
                Vector3f dirFrente = this.getLinearVelocity().negate();                
                Quaternion rot = geometria.getLocalRotation();
                rot.lookAt(dirFrente, Vector3f.UNIT_Y);
                pos= this.getPhysicsLocation();       
                this.setPhysicsRotation( rot); 
                this.setPhysicsLocation(pos); 
            }     
            if (estado.equals("caidaLibre"))          //Caida libre.  La orientación la controla la fisica
                    setGravity(new Vector3f (0,-10,0));        
        activarFisicas=true;
 }}    
 
public void collision(PhysicsCollisionEvent event) {  
         System.out.println(event.getNodeA().getName()+" colisionó con "+event.getNodeB().getName());
} 

public void asignarObjetivo (Spatial objetivo){
    estado="conObjetivo";
    spationalObjetivo= objetivo;
 }
public void remolcar (RigidBodyControl remolcado){
    Vector3f  localizacionPivoteEnA = new Vector3f( 0, 0, 5);
    Vector3f localizacionPivoteEnB  =  new Vector3f(0, 0, -5);
    remolque =new Point2PointJoint(this,  remolcado, localizacionPivoteEnA,localizacionPivoteEnB );
    estadosFisicos.getPhysicsSpace().add(remolque);
}
public  void prePhysicsTick(PhysicsSpace space, float tpf){ }
}