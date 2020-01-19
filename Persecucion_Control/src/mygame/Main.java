package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.Random;

public class Main extends SimpleApplication {
    public static void main(String args[]) {
        Main app = new Main();
        app.start();
        
    }
    private BulletAppState    estadosFisicos = new BulletAppState();
    private RigidBodyControl   fisicaSuelo, fisicaObj, fisicaCoche;
    Vector3f pos=Vector3f.ZERO;
    Vector3f CamPos=new Vector3f(5f, 5f, 5f);
    Geometry coche_geo=null, obj_geo=null;  float time; boolean controlTeclado, activarFisicas;
    boolean Fza_avanza, Fza_atras, Fza_der, Fza_izq, Vel_avanza, Vel_atras, Vel_der, Vel_izq;
    
    boolean objetivoAsignado=true;
    float fuerza=15f;
    float velocidad=2f;
    
    @Override
    public void simpleInitApp() {
        stateManager.attach(estadosFisicos);
        //cam.setLocation(new Vector3f(0f, 2f, 30f));
        this.flyCam.setEnabled(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.addLight(new DirectionalLight((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal()));
        Material mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat. setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
        
        //CREACION OBJETIVO
        obj_geo = new Geometry("pelota", new Sphere(32,32,0.4f,true,false));
        obj_geo.setMaterial(mat);
        rootNode.attachChild(obj_geo);
        obj_geo.setLocalTranslation(new Vector3f (5, 1f, 3f));
        fisicaObj = new RigidBodyControl(1f);
        obj_geo.addControl( fisicaObj );
        estadosFisicos.getPhysicsSpace().add( fisicaObj );
        fisicaObj.setRestitution(0.9f);
        
        //CREACION CAJA_NPC
        coche_geo = new Geometry("pelota", new Box(0.5f,0.5f,2));//  Sphere(32,32,0.4f,true,false));
        coche_geo.setMaterial(mat);
        rootNode.attachChild(coche_geo);
        coche_geo.setLocalTranslation(new Vector3f (0, 6, 3));
        fisicaCoche =new RigidBodyControl(10f);
        coche_geo.addControl( fisicaCoche );                         //asociacion con geometry esfera
        estadosFisicos.getPhysicsSpace().add( fisicaCoche );
        fisicaCoche.setRestitution(0.9f);
        
        //CREACON SUELO
        Geometry suelo_geo = new Geometry("Suelo", new Box(100f, 0.1f, 100f));
        suelo_geo.setMaterial(mat);
        rootNode.attachChild(suelo_geo);
        fisicaSuelo = new RigidBodyControl(0.0f);                //creación de suelo con masa 0
        suelo_geo.addControl( fisicaSuelo );                            //asociacion visual con fisica
        estadosFisicos.getPhysicsSpace().add( fisicaSuelo );
        fisicaSuelo.setRestitution(0.9f);
        
        inicTeclado();
    }
    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        cam.setLocation(obj_geo.getWorldTranslation().add(CamPos));
        cam.lookAt(obj_geo.getWorldScale().subtract(CamPos), Vector3f.UNIT_Y);
        time=time+tpf;
        fisicaObj.setLinearVelocity (new Vector3f (0, 0,5*(float)Math.sin(time)));
        
        // codigo fuerzas propias y externas
        if (activarFisicas) {         //activarFisicas hace que actue la gravedad y las colisiones
            activarFisicas=false;
        }else {                         //no_activarFisicas hace que solo la fuerzas propias actuen
            if (objetivoAsignado ){     //si hay objetivo, calcula fueza y la ejerce
                Vector3f dir = new Vector3f (obj_geo.getWorldTranslation().x- coche_geo.getWorldTranslation().x, obj_geo.getWorldTranslation().y- coche_geo.getWorldTranslation().y, obj_geo.getWorldTranslation().z- coche_geo.getWorldTranslation().z) ;
                fisicaCoche.applyCentralForce(new Vector3f(fuerza*dir.x, fuerza*dir.y, fuerza*dir.z));
                coche_geo.lookAt(obj_geo.getWorldTranslation(), Vector3f.UNIT_Y);
                pos= fisicaCoche.getPhysicsLocation();                         //copia la posicion sin alnterar
                fisicaCoche.setPhysicsRotation( coche_geo.getWorldRotation()); //actualiza la rotacion (...pero altera la posicion)
                fisicaCoche.setPhysicsLocation(pos);                            //actuliza posicion a la no alterada
            }
            if (controlTeclado){
                controlTeclado=false;
                if (Fza_avanza || Fza_atras || Fza_izq || Fza_der || Vel_avanza || Vel_atras || Vel_izq || Vel_der){
                    Vector3f dirFrente = this.coche_geo.getWorldTransform().getRotation().getRotationColumn(2).normalize();
                    if (Vel_der)   { fisicaCoche.setAngularVelocity(new Vector3f( 0, -velocidad/2f, 0)); Vel_der=false; }
                    if (Vel_izq)   { fisicaCoche.setAngularVelocity(new Vector3f( 0, velocidad/2f, 0));  Vel_izq=false; }
                    if (Vel_avanza){ fisicaCoche.setLinearVelocity(dirFrente.normalize().mult(velocidad)); Vel_avanza=false; }
                    if (Vel_atras) { fisicaCoche.setLinearVelocity(dirFrente.normalize().mult(-velocidad)); Vel_atras=false;  }
                    if (Fza_der)   {fisicaCoche.applyTorque(new Vector3f( 0, -fuerza, 0));  Fza_der=false;}
                    if (Fza_izq)   {fisicaCoche.applyTorque(new Vector3f( 0, fuerza, 0));   Fza_izq=false;}
                    if (Fza_avanza){fisicaCoche.applyCentralForce(new Vector3f(fuerza*dirFrente.x, fuerza*dirFrente.y, fuerza*dirFrente.z)); Fza_avanza=false;}
                    if (Fza_atras) {fisicaCoche.applyCentralForce(new Vector3f(-fuerza*dirFrente.x, -fuerza*dirFrente.y, -fuerza*dirFrente.z )); Fza_atras=false;}
                }
                
            } else controlTeclado=true;
            activarFisicas=true;
        }
        
    }
    private void inicTeclado() {
        inputManager.addMapping("AvanzarVel",   new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("AtrasVel",        new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("IzquierdaVel",  new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("DerechaVel",    new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("AvanzarFza",   new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("AtrasFza",        new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("IzquierdaFza",  new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("DerechaFza",    new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(analogListener,"IzquierdaVel", "DerechaVel", "AvanzarVel", "AtrasVel","IzquierdaFza", "DerechaFza", "AvanzarFza", "AtrasFza");
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
        }
    };
}
