package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Main extends SimpleApplication {
    public static Main app=null;
    public static void main(String[] args) {
        app = new Main();
        app.start();
    }

    Geometry geom;
    public static boolean reiniciar=true;
    //Variable static entre la escena3D y la hebra. Puede no ser estática con la referencia del objeto Main
     public static float velocidadDesplazamiento=0;
     public static float velocidadRotacion=0;
    
    @Override    
    public void simpleInitApp() {
        cam.setLocation(new Vector3f(5, 4f, 10f));         cam.lookAt(new Vector3f(5f, 0, 0),Vector3f.UNIT_Y); this.flyCam.setEnabled(false); 
        geom = new Geometry("Box", new Box(1, 1, 1));
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        
        Aprendiz_polinomico a= new Aprendiz_polinomico();
        a.start();
    }
        
    @Override
    public void simpleUpdate(float tpf) {
        if (reiniciar){
            geom.setLocalTranslation(Vector3f.ZERO);
            reiniciar=false;
        }else{
            geom.move(new Vector3f( velocidadDesplazamiento*tpf,0,0));       
            geom.rotate(0,velocidadRotacion,0);        
  } }
}    
