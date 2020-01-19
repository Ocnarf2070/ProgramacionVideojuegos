package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class PhysicsKinematic extends SimpleApplication {
    
    public static void main(String[] args) {
        PhysicsKinematic app = new PhysicsKinematic();
        app.start();
    }
    
    private BulletAppState bulletAppState;
    private RigidBodyControl scenePhy;
    private Material brickMat, stoneMat, woodMat;
    private static final String ELEVATOR = "Elevator";
    private Geometry platformGeo;
    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        brickMat =assetManager.loadMaterial("Materials/brick.j3m");
        stoneMat = assetManager.loadMaterial("Materials/pebbles.j3m");
        woodMat = assetManager.loadMaterial("Materials/bark.j3m");
        
        Node sceneNode = new Node("Scene");
        /* Create and attach a floor geometry */
        Box floorMesh = new Box(10f, 0.5f, 10f);
        Geometry floorGeo = new Geometry("Floor", floorMesh);
        floorGeo.setMaterial(stoneMat);
        floorGeo.move(0, -.1f, 0);
        sceneNode.attachChild(floorGeo);
        /* Create and attach a slope geometry */
        Box slopeMesh = new Box(6f, 0.1f, 5f);
        Geometry slopeGeo = new Geometry("Slope", slopeMesh);
        slopeGeo.setMaterial(brickMat);
        slopeGeo.rotate(0, 0, FastMath.DEG_TO_RAD * 50);
        slopeGeo.move(4f, 4f, 0);
        sceneNode.attachChild(slopeGeo);
        /* Create and attach a wall geometry */
        Box wallMesh = new Box(5f, 0.4f, 5f);
        Geometry wallGeo = new Geometry("Wall", wallMesh);
        wallGeo.setMaterial(brickMat);
        wallGeo.rotate(0, 0, FastMath.DEG_TO_RAD * 90);
        wallGeo.move(-3.5f, 2, 0);
        sceneNode.attachChild(wallGeo);
        
        scenePhy = new RigidBodyControl(0.0f);
        sceneNode.addControl(scenePhy);
        bulletAppState.getPhysicsSpace().add(scenePhy);
        rootNode.attachChild(sceneNode);
        
        Box platformMesh = new Box(2f, 0.5f, 5f);
        platformGeo = new Geometry(ELEVATOR, platformMesh);
        platformGeo.setMaterial(woodMat);
        platformGeo.move(-1, 2, 0);
        rootNode.attachChild(platformGeo);
        RigidBodyControl platformPhy =
                new RigidBodyControl(100.0f);
        platformGeo.addControl(platformPhy);
        platformPhy.setKinematic(true);
        bulletAppState.getPhysicsSpace().add(platformPhy);
        
        
    }
    
    private static final float TOPFLOOR = 6f;
    private boolean isPlatformOnTop = false;
    @Override
    public void simpleUpdate(float tpf) {
        if (!isPlatformOnTop &&
                platformGeo.getLocalTranslation().getY() < TOPFLOOR) {
            platformGeo.move(0f, tpf, 0f);
        }if (!isPlatformOnTop &&
                platformGeo.getLocalTranslation().getY() >= TOPFLOOR) {
            isPlatformOnTop = true;
        }
        if (isPlatformOnTop &&
                platformGeo.getLocalTranslation().getY() <= .5f) {
            isPlatformOnTop = false;
        }
        if (isPlatformOnTop &&
                platformGeo.getLocalTranslation().getY() > .5f) {
            platformGeo.move(0f, -tpf * 4, 0f);
        }
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
