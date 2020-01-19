package PreFabs;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class ModeloFisicoMovil extends RigidBodyControl implements  PhysicsTickListener, PhysicsCollisionListener {
      boolean objetivoAsignado, activarFisicas, actualizaFuerzas, controlTeclado; Vector3f pos= Vector3f.ZERO;
      Geometry thisGeometria=null, geomObj=null; boolean Fza_avanza, Fza_atras, Fza_der, Fza_izq, Vel_avanza, Vel_atras, Vel_der, Vel_izq; 
      
      public float fuerza=25f * this.mass;                        //Para mover el objeto, la fuerza es proporional a la masa;
      public float velocidad=2f;

 public ModeloFisicoMovil(float masa, Geometry geometria, Material mat, BulletAppState estadosFisicos, Node rootNode){
      super(masa);  
      thisGeometria = geometria;
      thisGeometria.setMaterial(mat);
      rootNode.attachChild(thisGeometria);              
      thisGeometria.addControl( this );                      
      estadosFisicos.getPhysicsSpace().add( this );  
      estadosFisicos.getPhysicsSpace().addCollisionListener(this);
      this.setRestitution(0.9f);
}
           
  public  void prePhysicsTick(PhysicsSpace space, float tpf){
}  
 public void physicsTick (PhysicsSpace space, float tpf){
     if (activarFisicas) {         //activarFisicas hace que actue la gravedad y las colisiones
          activarFisicas=false;       
       }else {                         //no_activarFisicas hace que solo la fuerzas propias actuen
            if (objetivoAsignado ){     //si hay objetivo, calcula fueza y la ejerce                
                Vector3f dir = new Vector3f (geomObj.getWorldTranslation().x- thisGeometria.getWorldTranslation().x, 
                                             geomObj.getWorldTranslation().y- thisGeometria.getWorldTranslation().y, 
                                             geomObj.getWorldTranslation().z- thisGeometria.getWorldTranslation().z).normalize() ;          
                setLinearVelocity(dir.mult(velocidad));
                //this.applyCentralForce(new Vector3f(fuerza*dir.x, fuerza*dir.y, fuerza*dir.z));  
                thisGeometria.lookAt(geomObj.getWorldTranslation(), Vector3f.UNIT_Y);
                pos= this.getPhysicsLocation();                         //copia la posicion sin alnterar 
                this.setPhysicsRotation( thisGeometria.getWorldRotation()); //actualiza la rotacion (...pero altera la posicion) 
                this.setPhysicsLocation(pos);                            //actuliza posicion a la no alterada       
            }          
            if (controlTeclado){  
              controlTeclado=false;
              if (Fza_avanza || Fza_atras || Fza_izq || Fza_der || Vel_avanza || Vel_atras || Vel_izq || Vel_der){
                 Vector3f dirFrente = this.thisGeometria.getWorldTransform().getRotation().getRotationColumn(2).normalize(); 
                  if (Vel_der)   { this.setAngularVelocity(new Vector3f( 0, -velocidad/2f, 0)); Vel_der=false; }  
                  if (Vel_izq)   { this.setAngularVelocity(new Vector3f( 0, velocidad/2f, 0));  Vel_izq=false; }     
                  if (Vel_avanza){ this.setLinearVelocity(dirFrente.normalize().mult(velocidad)); Vel_avanza=false; }
                  if (Vel_atras) { this.setLinearVelocity(dirFrente.normalize().mult(-velocidad)); Vel_atras=false;  }
                  if (Fza_der)   {this.applyTorque(new Vector3f( 0, -fuerza/2f, 0));  Fza_der=false;} 
                  if (Fza_izq)   {this.applyTorque(new Vector3f( 0, fuerza/2f, 0));   Fza_izq=false;}  
                  if (Fza_avanza){this.applyCentralForce(new Vector3f(fuerza*dirFrente.x, fuerza*dirFrente.y, fuerza*dirFrente.z)); Fza_avanza=false;}
                  if (Fza_atras) {this.applyCentralForce(new Vector3f(-fuerza*dirFrente.x, -fuerza*dirFrente.y, -fuerza*dirFrente.z )); Fza_atras=false;}  
                 } 
            } else controlTeclado=true;
        activarFisicas=true;
       }    

 }    
 
public void collision(PhysicsCollisionEvent event) {     
         // System.out.println(event.getNodeA().getName()+" colisionó con "+event.getNodeB().getName());
  }
 public void asignarObjetivo (Geometry geomObje){
    objetivoAsignado=true;
    controlTeclado=false;
    geomObj= geomObje;
 }

public void inicTeclado (com.jme3.input.InputManager inputManager){
    inputManager.addMapping("AvanzarVel",   new KeyTrigger(KeyInput.KEY_U));
    inputManager.addMapping("AtrasVel",        new KeyTrigger(KeyInput.KEY_J));
    inputManager.addMapping("IzquierdaVel",  new KeyTrigger(KeyInput.KEY_H));
    inputManager.addMapping("DerechaVel",    new KeyTrigger(KeyInput.KEY_K));    
    inputManager.addMapping("AvanzarFza",   new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("AtrasFza",        new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("IzquierdaFza",  new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("DerechaFza",    new KeyTrigger(KeyInput.KEY_D));
    inputManager.addListener(analogListener,"IzquierdaVel", "DerechaVel", "AvanzarVel", "AtrasVel","IzquierdaFza", "DerechaFza", "AvanzarFza", "AtrasFza");
    this.controlTeclado=true; 
}  
  private AnalogListener analogListener = new AnalogListener()  {
    public void onAnalog(String name, float value, float tpf) {
            if (name.equals("DerechaVel"))   Vel_der=true;  
            if (name.equals("IzquierdaVel")) Vel_izq=true;     
            if (name.equals("AvanzarVel"))   Vel_avanza=true;
            if (name.equals("AtrasVel"))     Vel_atras=true;   
            if (name.equals("DerechaFza"))   Fza_der=true;  
            if (name.equals("IzquierdaFza")) Fza_izq=true;     
            if (name.equals("AvanzarFza"))   Fza_avanza=true;
            if (name.equals("AtrasFza"))     Fza_atras=true;   
}};

}
