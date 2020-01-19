package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    public static void main(String args[]) { Main app = new Main(); app.start(); }
    private final BulletAppState estadosFisicos = new BulletAppState();
    private RigidBodyControl fisicaBola, fisicaSuelo, fisicaMontana;
    Geometry bola_geo,suelo_geo;
    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(5, 1f, 0f));
        this.flyCam.setEnabled(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.addLight(new DirectionalLight((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal()));
        
        stateManager.attach(estadosFisicos);
        bola_geo = new Geometry("pelota", new Sphere(32,32,0.4f,true,false));
        Material bola_mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        bola_geo.setMaterial(bola_mat);
        rootNode.attachChild(bola_geo); /** Position the cannon bola */
        bola_geo.setLocalTranslation(new Vector3f (0, 10, 0));
        fisicaBola = new RigidBodyControl(3f); //creación la fisicaBola con masa 1 Kg
        bola_geo.addControl( fisicaBola ); //asociación entre geometry y física de bola - sin material bola_geo.addControl( fisicaBola ); //asociación entre geometry y física de bola estadosFisicos.getPhysicsSpace().add( fisicaBola ); //integración de fisicaBola en entorno físico
        estadosFisicos.getPhysicsSpace().add(fisicaBola);
        fisicaBola.setRestitution(0.9f); //darndo rebote a fisicaBola
        suelo_geo = new Geometry("Suelo", new Box(100f, 0.1f, 100f));
        Material suelo_mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
        suelo_mat. setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
        suelo_geo.setMaterial(suelo_mat);
        rootNode.attachChild(suelo_geo);
        fisicaSuelo = new RigidBodyControl(0f); //creación la fisicaSuelo con masa 0
        suelo_geo.addControl( fisicaSuelo ); //asociación geometry y física de suelo
        estadosFisicos.getPhysicsSpace().add( fisicaSuelo ); //integración de fisicaSuelo en entorno físico
        fisicaSuelo.setRestitution(1f); //darndo rebote a fisicaSuelo}
        fisicaBola.setRestitution(1f);
    }   
    int mutex = 1;
    @Override
    public void simpleUpdate(float tpf) {
       // cam.setLocation(bola_geo.getLocalTranslation().add(0,5,0));
        cam.lookAt(bola_geo.getLocalTranslation(), Vector3f.UNIT_Y);
//        CollisionResults result = new CollisionResults();
//        bola_geo.collideWith(suelo_geo.getWorldBound(), result);
//        
//        if (result.size()>0&&mutex==1) {
//            mutex--;
//            System.out.println(result);
//            Vector3f fuerza = (new Vector3f(0, 20, 0));
//            fuerza.mult(fisicaBola.getGravity()).mult(fisicaBola.getMass());
//            fisicaBola.applyImpulse(fuerza,Vector3f.ZERO);
//            mutex++;
//        }
    }
}