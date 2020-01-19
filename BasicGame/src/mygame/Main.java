package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

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
    Geometry soporte, reloj;
    Node nodoAux;

    @Override
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(0, 10, 0));
        
        Box cubo = new Box(0.01f,3f,0.01f);
        Cylinder cilindro = new Cylinder(50, 50, 0.1f, 1.5f, true);
        soporte = new Geometry("soporte", cubo);
        reloj = new Geometry("reloj", cilindro);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Red);
        soporte.setMaterial(mat1);
        reloj.setMaterial(mat2);
        reloj.setLocalTranslation(cilindro.getHeight()/2f, cubo.yExtent+cilindro.getRadius(),0);
        reloj.setLocalRotation(new Quaternion().fromAngles(0, (float)(Math.PI)/2f ,0));
        nodoAux=new Node();
        nodoAux.attachChild(soporte);
        nodoAux.attachChild(reloj);
        rootNode.attachChild(nodoAux);
        cam.lookAt(soporte.getWorldTranslation(), Vector3f.UNIT_Y);
        
    }
    float [] angles = {0,0,0};
    boolean ver = true;
    @Override
    public void simpleUpdate(float tpf) {
        angles = reloj.getWorldRotation().toAngles(angles);
        float angulo = angles[1];
        System.out.println(angulo*FastMath.RAD_TO_DEG);
        if(ver && angulo <= (float)Math.PI/2f){
            nodoAux.rotate(0, 0.01f, 0);
        } else if(!ver && angulo >= -(float)Math.PI/2f){
            nodoAux.rotate(0, -0.01f, 0);
        }
        if(angulo >= (float)Math.PI/2f){
            ver=false;
        }
        else if(angulo <= (float)-Math.PI/2f){
            ver=true;
        }

        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
