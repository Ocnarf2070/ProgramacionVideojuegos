package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

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
    
    void ponerIluminacion(){
        DirectionalLight sun1 = new DirectionalLight();
        DirectionalLight sun2 = new DirectionalLight();
        sun1.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun2.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        sun1.setColor(ColorRGBA.White);
        sun2.setColor(ColorRGBA.Gray);
        rootNode.addLight(sun1);
        rootNode.addLight(sun2);
    }
     private void Init(){
        ponerIluminacion();
        Spatial mundo = assetManager.loadModel("Scenes/Suelo.j3o");
        BoundingBox box = (BoundingBox)mundo.getWorldBound();
        Vector3f boxSize = new Vector3f(0, 0, 0);
        box.getExtent(boxSize);
        System.out.println(boxSize);
        mundo.setLocalTranslation(boxSize.x-jugador.entornoJugador().getXExtent(),
                0,boxSize.z-jugador.entornoJugador().getZExtent());
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.attachChild(mundo);
        
    }
    
    private Jugador jugador;
    @Override
    public void simpleInitApp() {
       
        Libres.start(100, 100);
        Pair<Integer,Integer> lugar = Libres.remove(0);
        
        try {
            jugador = new Jugador(this, lugar);
            Objetivos.start(100*100, jugador.getJugador(),this);
            IA control = new IA(jugador.getJugador().getWorldTranslation(),new Vector3f(lugar.getKey(),jugador.entornoJugador().yExtent, lugar.getValue()),this);
            jugador.Control(control);
            Init();
            paredes(100,100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        flyCam.setMoveSpeed(30);
        cam.setLocation(new Vector3f(-30, 30, -30));
        contorno = new BoundingSphere(10f, jugador.getJugador().getWorldTranslation());
    }
    BoundingSphere contorno;
    @Override
    public void simpleUpdate(float tpf) {
        cam.lookAt(jugador.getJugador().getWorldTranslation().add(-10,-10,-10), Vector3f.UNIT_Y);
        contorno.setCenter(jugador.getJugador().getWorldTranslation());
        CollisionResults result = new CollisionResults();
        List<Geometry> list = new ArrayList<>();
        for(Geometry aux: Oculto.getOculto()){
            //System.out.println(contorno);
            aux.collideWith(contorno, result);
            
            if (result.size() > 0) {
                CollisionResult closest = result.getClosestCollision();
                System.out.println(aux.getName()+" Distancia? "+ closest.getDistance());
                rootNode.attachChild(aux);
                try {
                        Prioridades.add(aux);
                        list.add(aux);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            result=new CollisionResults();
        }
        for(Geometry rev: list)Oculto.remove(rev);
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void paredes(int ancho, int largo) {
        Box A = new Box(ancho/2, 1, 1);
        Box L = new Box(1, 1, largo/2);
        Geometry A1 = new Geometry("A1", A), A2 = new Geometry("A2", A);
        Geometry L1 = new Geometry("L1", L), L2 = new Geometry("L2", L);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Brown);
        A1.setMaterial(mat);
        A2.setMaterial(mat);
        L1.setMaterial(mat);
        L2.setMaterial(mat);        
        Node aux = new Node();
        A1.setLocalTranslation(A.xExtent-1, A.yExtent, jugador.entornoJugador().getZExtent()-3);
        aux.attachChild(A1);
        A2.setLocalTranslation(A.xExtent-1, A.yExtent, jugador.entornoJugador().getZExtent()+largo-1);
        aux.attachChild(A2);
        L1.setLocalTranslation(jugador.entornoJugador().getXExtent()-3, L.yExtent, largo/2-1);
        aux.attachChild(L1);
        L2.setLocalTranslation(jugador.entornoJugador().getXExtent()-1+ancho, L.yExtent, largo/2-1);
        aux.attachChild(L2);
        rootNode.attachChild(aux);
    }
}
