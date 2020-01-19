package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
    Geometry geom=null;
    Node NewWorld = new Node();
    @Override
    public void simpleInitApp() {
        Init();
        this.flyCam.setEnabled(false);
        Box b = new Box(0.5f, 0.5f, 0.5f);
        geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(NewWorld);
        NewWorld.attachChild(geom);
        inicTeclado();
    }
    private void Init(){
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(1, -1, -2)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        Spatial mundo = assetManager.loadModel("Scenes/Suelo.j3o");
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        cam.setLocation(new Vector3f(0, 2f, 5f));
        cam.lookAt(new Vector3f(0, 1, 0), Vector3f.UNIT_Y);
        rootNode.attachChild(mundo);
        rootNode.addLight(sun);
    }
    private void inicTeclado() {
        inputManager.addMapping("Avanzar", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Atras", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Izquierda", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Derecha", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(analogListener,"Izquierda", "Derecha", "Avanzar", "Atras");
    }
    private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            NewWorld.setLocalTransform(geom.getWorldTransform()); //El world del geom es World padre
            geom.setLocalTransform(new Transform()); //Se reinicia la transf. local del geom
            if (name.equals("Derecha")) {
                geom.rotate(0, - 0.001f, 0);
                Vector3f camact = cam.getLocation();
                Vector3f camnext = camact.subtract(0, - 0.001f, 0);
                System.out.println(camnext);
                
            }
            if (name.equals("Izquierda")) {
                geom.rotate( 0, 0.001f, 0);
            }
            if (name.equals("Avanzar")) {
                geom.move(0, 0, 0.005f);
            }
            if (name.equals("Atras")) {
                geom.move(0,0, -0.005f);
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        //System.out.println(cam.getLocation());
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
