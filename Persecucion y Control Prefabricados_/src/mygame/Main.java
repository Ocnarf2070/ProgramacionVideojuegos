package mygame;

import PreFabs.ModeloFisicoMovil;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.Point2PointJoint;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.Random;
import PreFabs.AvionFisico;
import PreFabs.ModeloFisicoEstatico;
import PreFabs.ModeloGeometricoMovil;

public class Main extends SimpleApplication {
    public static void main(String args[]) {  Main app = new Main();   app.start();  }
    private BulletAppState  estadosFisicos = new BulletAppState();       public  static Material matPorDefecto=null;
    private AvionFisico avion1, avion2;  float tiempo=0; boolean proyectilSoltado;
    private ModeloFisicoEstatico pueblo;
    
    @Override
public void simpleInitApp() {      
    stateManager.attach(estadosFisicos);
   //cam.setLocation(new Vector3f(-40f, 0f, 60f));  
   //this.flyCam.setEnabled(false); 
    this.cam.lookAt(new Vector3f(30.5f, -10, 70f), Vector3f.ZERO);
    rootNode.addLight(new DirectionalLight((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal())); 
    Material mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
    mat. setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg")); matPorDefecto = mat.clone();  
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));   
       
    
    //CREACION DE OBJETOS 3D A PARTIR DE PREFABRICADOS:
    pueblo    =   new ModeloFisicoEstatico( 0, assetManager.loadModel("main.scene"), null, estadosFisicos,  rootNode, assetManager); 
    avion1    =   new AvionFisico (10, mat, "Avion_1", estadosFisicos, rootNode, assetManager);     
    avion2    =   new AvionFisico (1f, mat,  "Avion_2", estadosFisicos, rootNode, assetManager);

    
    //POSICIONAMIENTO DE OBJETOS EN ESCENA Y ASIGNACION DE ROLES    
    pueblo.setPhysicsLocation(new Vector3f (0,-15,50));        
    avion1.setPhysicsLocation(new Vector3f (4, 200f, 0));
    //avion1.remolcar(avion2);             
    //TAMBIEN:     
    avion2.asignarObjetivo( avion1.geometria);    
    avion2.setPhysicsLocation(new Vector3f (4, 150f,-30));
    avion2.setGravity(new Vector3f (0, -10 , 0));
    avion2.setRestitution(0.01f);
}

    Vector3f CamPos=new Vector3f(10f, 10f, 10f);

@Override
public void simpleUpdate(float tpf) {
    cam.setLocation(avion1.getPhysicsLocation().add(CamPos));
    //cam.lookAt(avion1.getPhysicsLocation(), Vector3f.UNIT_Y);
   tiempo=tiempo+tpf;
   avion1.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
   avion2.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
   avion1.setLinearVelocity(new Vector3f (20f*(float)Math.cos(tiempo/2f), 0,20f*(float)Math.sin(tiempo/2f))); 
   if (tiempo>4 && !proyectilSoltado){                                                                           //SOLTAR EL PROYECTIL EN EL SEGUNDO 4
        estadosFisicos.getPhysicsSpace().remove(avion1.remolque);
       avion2.estado= "caidaLibre"; 
       proyectilSoltado=true;
}}}

/*package mygame;

import mygame.PreFabs.ModeloFisicoMovil;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.Random;
import mygame.PreFabs.AvionFisico;
import mygame.PreFabs.ModeloFisicoEstatico;
import mygame.PreFabs.ModeloFisicoMovil;
import mygame.PreFabs.ModeloGeometricoMovil;

public class Main extends SimpleApplication {
    public static void main(String args[]) {  Main app = new Main();   app.start();  }
    private BulletAppState    estadosFisicos = new BulletAppState();   Geometry cuboide_geom=null, esfera_geom=null;  float time=0;
    public static Material matPorDefecto;
    private ModeloFisicoEstatico   suelo;
    private ModeloFisicoMovil cuboide, esfera;
    private AvionFisico avion;
    private ModeloGeometricoMovil fantasmita;
    
    @Override
public void simpleInitApp() {      
    stateManager.attach(estadosFisicos);  
    cam.setLocation(new Vector3f(-30f, 10f, 60f));  this.flyCam.setEnabled(false); 
    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
    rootNode.addLight(new DirectionalLight((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal())); 
    Material mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
    mat. setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));  matPorDefecto = mat.clone();  
    
    //CREACION SUELO
    suelo = new ModeloFisicoEstatico (0, new Geometry("Suelo", new Box(100f, 0.5f, 100f)), null, estadosFisicos, rootNode, assetManager);
    //CREACION FANTASMA
    Geometry fantasma_geom = new Geometry("Fantasma", new Box(0.5f, 1f, 0.5f));
    fantasmita = new ModeloGeometricoMovil (fantasma_geom, mat, rootNode, assetManager);
    //CREACION DIRIGIBLE    
    avion = new AvionFisico(1f, mat, "avion", estadosFisicos, rootNode, assetManager);    
    

    //CREACION ESFERA 
    esfera_geom = new Geometry("Pelota", new Sphere(32,32,0.4f,true,false));
    esfera_geom.setLocalTranslation(new Vector3f (5, 1, 3));
    esfera =new ModeloFisicoMovil(1f, esfera_geom, mat,  estadosFisicos, rootNode);  
                                                                
    //CREACION COCHE
    cuboide_geom = new Geometry("Coche", new Box(0.5f,0.5f,2));
    cuboide_geom.setLocalTranslation(new Vector3f (-20, 4, 3));
    cuboide =new ModeloFisicoMovil(5f, cuboide_geom, mat, estadosFisicos, rootNode);
    
    //ASIGNACION ROLES
    fantasmita.asignarObjetivo(cuboide_geom);
    avion.asignarObjetivo(cuboide_geom);
    esfera.asignarObjetivo(cuboide_geom);
    cuboide.inicTeclado(inputManager);   // cuboide.asignarObjetivo(esfera_geom);    
}
@Override
public void simpleUpdate(float tpf) {  
    this.time= time+tpf;
      cuboide.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);  
      esfera.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
      avion.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
      
      cuboide.applyCentralForce(new Vector3f (20f*(float)Math.cos(time/2f), 0,20f*(float)Math.sin(time/2f))); 
}}





*/