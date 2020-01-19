package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
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
    
    Geometry soporte, limpiaparabrisas;
    Node nodoAux;
    
    @Override
    public void simpleInitApp() {
        
        cam.setLocation(new Vector3f(0, 0, 2));
        // this.flyCam.setEnabled(false);
        Box b = new Box(0.5f,0.5f,0.01f);
        Geometry geom = new Geometry("Parabrisas", b);
        Node nodo = new Node();
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, b.yExtent/2, 0);
        
        Box cubo = new Box(0.01f,0.01f,0.01f);
        Cylinder cilindro = new Cylinder(50, 50, 0.01f, 0.4f,true);
        soporte = new Geometry("soporte", cubo);
        limpiaparabrisas = new Geometry("limpia-parabrisas", cilindro);
        Material matC = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matC.setColor("Color", ColorRGBA.Red);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        limpiaparabrisas.setMaterial(matC);
        soporte.setMaterial(mat1);
        limpiaparabrisas.setLocalTranslation(cilindro.getHeight()/2f, cubo.yExtent+cilindro.getRadius(),0);
        limpiaparabrisas.setLocalRotation(new Quaternion().fromAngles(0, (float)(Math.PI)/2f ,0));
        nodoAux=new Node();
        nodoAux.attachChild(soporte);
        nodoAux.attachChild(limpiaparabrisas);
        
        nodoAux.setLocalRotation(new Quaternion().fromAngles((float)(Math.PI)/2f,0 ,0 ));
        nodoAux.setLocalTranslation(0, -0.20f, 0);
        
        nodo.attachChild(nodoAux);
        nodo.attachChild(geom);
        
        nodo.setLocalRotation(new Quaternion().fromAngles((float)-Math.PI/4f,0 ,0 ));
        
        rootNode.attachChild(nodo);
        
        setUpHUDText();
        
        vel = Velocity(1, 2, 180, new Vector3f(0, 1, 0));
        
        //rootNode.attachChild(cilindro);
        
        
    }
    float [] angles = {0,0,0};
    boolean ver = true;
    Vector3f vel;
    float time;
    int veces;
    @Override
    public void simpleUpdate(float tpf) {
        time += tpf;
        angles = limpiaparabrisas.getWorldRotation().toAngles(angles);
        float angulo = angles[1];
        if(angulo<0)angulo += 360*FastMath.DEG_TO_RAD;
        hudText.setText("Tiempo: "+time+"\t\tVeces: "+veces);
        
        nodoAux.rotate(vel.x*tpf, vel.y*tpf, vel.z*tpf);
        if(ver && angulo >= (float)3*Math.PI/2f){
            vel=vel.negate();
            ver=false;
            veces++;
            System.out.println("Tiempo: "+time+" \tVeces: "+veces);
        } else if(!ver && angulo <= (float)Math.PI/2f){
            vel=vel.negate();
            ver=true;
            veces++;
            System.out.println("Tiempo: "+time+" \tVeces: "+veces);
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
    
    private Vector3f Velocity(float time, float frecuency, float angle, Vector3f normalize){
        float rad = angle*FastMath.DEG_TO_RAD;
        float tpf = time/frecuency;
        float w = rad/tpf;
        return normalize.mult(w);
    }
}
