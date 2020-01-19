package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

       
    private BulletAppState    estadosFisicos = new BulletAppState();
    private RigidBodyControl   fisicaSuelo, fisicaObj, fisicaPared;
    Vector3f pos=Vector3f.ZERO;
    Vector3f CamPos=new Vector3f(10f, 10f, 10f);
    Geometry obj=null;
    boolean controlTeclado, activarFisicas;
    boolean Fza_avanza, Fza_atras, Fza_der, Fza_izq, Vel_avanza, Vel_atras, Vel_der, Vel_izq;
    
    boolean objetivoAsignado=true;
    float fuerza=15f;
    float velocidad=2f;
    
    @Override
    public void simpleInitApp() {
        stateManager.attach(estadosFisicos);
        //cam.setLocation(new Vector3f(10f, 12f, 10f));
        this.flyCam.setEnabled(false);
        flyCam.setMoveSpeed(30f);
        flyCam.setDragToRotate(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.addLight(new DirectionalLight((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal()));
        Material mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat. setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
        
        //CREACION OBJETIVO
        obj = new Geometry("pelota", new Sphere(32,32,0.4f,true,false));
        obj.setMaterial(mat);
        rootNode.attachChild(obj);
        obj.setLocalTranslation(new Vector3f (5, 1f, 3f));
        fisicaObj = new RigidBodyControl(1f);
        obj.addControl( fisicaObj );
        estadosFisicos.getPhysicsSpace().add( fisicaObj );
        fisicaObj.setRestitution(0.9f);
        
        //CREACON SUELO
        Geometry suelo_geo = new Geometry("Suelo", new Box(100f, 0.1f, 100f));
        suelo_geo.setMaterial(mat);
        rootNode.attachChild(suelo_geo);
        fisicaSuelo = new RigidBodyControl(0.0f);                //creación de suelo con masa 0
        suelo_geo.addControl( fisicaSuelo );                            //asociacion visual con fisica
        estadosFisicos.getPhysicsSpace().add( fisicaSuelo );
        fisicaSuelo.setRestitution(0.9f);
        fisicaSuelo.setFriction(0.7f);
        
        //CREACION PAREDES
        Box b1 = new Box(100f, 2f, 1f);
        Box b2 = new Box(1f, 2f, 100f);
        Geometry p1 = new Geometry("Suelo", b1);
        Geometry p2 = new Geometry("Suelo", b1);
        Geometry p3 = new Geometry("Suelo", b2);
        Geometry p4 = new Geometry("Suelo", b2);
        p1.setLocalTranslation(0,0,b1.xExtent+1f);
        p2.setLocalTranslation(0,0,-b1.xExtent-1f);
        p3.setLocalTranslation(b2.zExtent+1f,0,0);
        p4.setLocalTranslation(-b2.zExtent-1f,0,0);
        Node paredes = new Node();
        paredes.attachChild(p1);
        paredes.attachChild(p2);
        paredes.attachChild(p3);
        paredes.attachChild(p4);
        paredes.setMaterial(mat);
        rootNode.attachChild(paredes);
        fisicaPared = new RigidBodyControl(0.0f);                //creación de suelo con masa 0
        paredes.addControl( fisicaPared );                            //asociacion visual con fisica
        estadosFisicos.getPhysicsSpace().add( fisicaPared );
        fisicaPared.setRestitution(0f);
        inicTeclado();
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        super.simpleUpdate(tpf); //To change body of generated methods, choose Tools | Templates.
        cam.setLocation(obj.getWorldTranslation().add(CamPos));
        cam.lookAt(obj.getLocalTranslation(), Vector3f.UNIT_Y);
        
         if (controlTeclado){
                controlTeclado=false;
                if (Fza_avanza || Fza_atras || Fza_izq || Fza_der || Vel_avanza || Vel_atras || Vel_izq || Vel_der){
                    //Vector3f dirFrente = this.obj.getWorldTranslation().normalize();
                    if (Fza_der)   {fisicaObj.applyCentralForce(new Vector3f(0, 0, fuerza));  Fza_der=false;}
                    if (Fza_izq)   {fisicaObj.applyCentralForce(new Vector3f(0, 0, -fuerza));   Fza_izq=false;}
                    if (Fza_avanza){fisicaObj.applyCentralForce(new Vector3f(fuerza, 0, 0)); Fza_avanza=false;}
                    if (Fza_atras) {fisicaObj.applyCentralForce(new Vector3f(-fuerza, 0, 0 )); Fza_atras=false;}
                }
                
            } else controlTeclado=true;
            activarFisicas=true;
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
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

