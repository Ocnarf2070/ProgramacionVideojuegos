package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

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
    
    private final BulletAppState estadosFisicos = new BulletAppState();
    private RigidBodyControl fisicaPlaneta;
    Geometry geom;

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10f);
        stateManager.attach(estadosFisicos);
        cam.setLocation(new Vector3f(0, 10, 0));

        Box b = new Box(1, 1, 1);
        geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        fisicaPlaneta = new RigidBodyControl(20f);
        geom.addControl(fisicaPlaneta);
        estadosFisicos.getPhysicsSpace().add(fisicaPlaneta);
        fisicaPlaneta.setGravity(Vector3f.ZERO);

        rootNode.attachChild(geom);
    }
    int tiempo=0;
    boolean activarFisicas=true;
    @Override
    public void simpleUpdate(float tpf) {
        //cam.setLocation(geom.getLocalTranslation().add(5,5,5));
        cam.lookAt(geom.getLocalTranslation(), Vector3f.UNIT_Y);
        tiempo += tpf;
        if (activarFisicas) {
            activarFisicas=false;
        }else {
            Vector3f dirFrente = fisicaPlaneta.getLinearVelocity();
            Quaternion rot = geom.getLocalRotation();
            //rot.lookAt(dirFrente, Vector3f.UNIT_Y);
            Vector3f pos= fisicaPlaneta.getPhysicsLocation();
            fisicaPlaneta.setPhysicsRotation( rot);
            fisicaPlaneta.setPhysicsLocation(pos);
            activarFisicas=true;
        }
        //fisicaPlaneta.applyTorque(new Vector3f(10, 0, 0));
        fisicaPlaneta.setLinearVelocity(new Vector3f (5f*(float)Math.cos(tiempo/2f), 0,5f*(float)Math.sin(tiempo/2f)));
        Vector3f w = new Vector3f();
        fisicaPlaneta.getAngularVelocity(w);
        System.out.println(w);
        //TODO: add update code
        

    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
