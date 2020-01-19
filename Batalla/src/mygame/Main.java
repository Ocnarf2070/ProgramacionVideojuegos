package mygame;

import PreFrab.Cilindros;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.Iterator;
import java.util.Map;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private Cilindros a,b;
    private BulletAppState  estadosFisicos = new BulletAppState();
    private RigidBodyControl   fisicaSuelo;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        stateManager.attach(estadosFisicos);
        cam.setLocation(new Vector3f(3f, 2f, 0f));
        flyCam.setMoveSpeed(10);
        Box suelo = new Box(10, 1f, 10);
        Geometry geom = new Geometry("Box", suelo);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        geom.setMaterial(mat);
        Material matA = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material matB = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matA.setColor("Color", ColorRGBA.Blue);
        matB.setColor("Color", ColorRGBA.Red);
        fisicaSuelo = new RigidBodyControl(0.0f);                //creación de suelo con masa 0
        geom.addControl( fisicaSuelo );                            //asociacion visual con fisica
        estadosFisicos.getPhysicsSpace().add( fisicaSuelo );
        fisicaSuelo.setFriction(1f);
        
        setUpHUDText();
        
        a = new Cilindros(10, 0.5f, 0.25f, matA, "CilindroA", estadosFisicos, rootNode, assetManager);
        b = new Cilindros(10, 0.5f, 0.25f, matB, "CilindroB", estadosFisicos, rootNode, assetManager);
        
        a.setPhysicsLocation(new Vector3f(4, 0.5f, 0));
        a.setVel(new Vector3f(0, 0, 1));
        b.setPhysicsLocation(new Vector3f(0, 0.5f, 0));
        b.asignarObjetivo(a.getGeometria());
        
        rootNode.attachChild(geom);
        
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }
    
    private boolean ver=false;
    @Override
    public void simpleUpdate(float tpf) {
        a.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
        b.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
        Map<String,Integer> dic = Cilindros.getImpacts();
        String text="";
        Iterator<Map.Entry<String,Integer>> it = dic.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Integer> tuple = it.next();
            text+="Balas dadas a "+tuple.getKey()+" : "+tuple.getValue();
            if(it.hasNext())text+="\t";
        }
        hudText.setText(text);
        float dist = a.getPhysicsLocation().subtract(b.getPhysicsLocation()).length();
        if(!ver&&dist<1){
            b.activarDisparo(a.getGeometria());
            ver=true;
        }
        
        
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    public BitmapText hudText;
    private void setUpHUDText(){
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.White);                             // font color
        hudText.setLocalTranslation(200, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);
    }
}
