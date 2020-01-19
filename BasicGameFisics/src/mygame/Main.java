package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
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

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(2, 2, 2));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        Box b = new Box(1, 1, 1);
        Box suelo = new Box(100,0.1f,100);
        Geometry geom = new Geometry("Box", b);
        Geometry geom2 = new Geometry("Suelo", suelo);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        geom2.setMaterial(mat2);

        rootNode.attachChild(geom);
        rootNode.attachChild(geom2);
        
        geom.addControl(new Control1());
        geom.addControl(new Control2());
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
