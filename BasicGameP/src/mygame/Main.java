package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
    Geometry geom;
    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        geom = new Geometry("Box", b);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        
        rootNode.attachChild(geom);
    }
    float time=0;
    @Override
    public void simpleUpdate(float tpf) {
        time += tpf;
        if(time>5){
            rootNode.detachChild(geom);
        }
        //TODO: add update code
        if(!rootNode.hasChild(geom)) System.out.println("Cubo no existente");
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
