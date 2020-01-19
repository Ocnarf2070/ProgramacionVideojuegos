package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

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
    
    private void Init(){
        this.flyCam.setDragToRotate(true);
        cam.setLocation(new Vector3f(0, 2f, 5f));
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(1, -1, -2)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        //Spatial mundo = assetManager.loadModel("Scenes/MiEscena.j3o");
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        cam.setLocation(new Vector3f(0, 2f, 5f));
        cam.lookAt(new Vector3f(0, 1, 0), Vector3f.UNIT_Y);
        //rootNode.attachChild(mundo);
        rootNode.addLight(sun);
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

    private Geometry player;
    private Geometry player2;
    @Override
    public void simpleInitApp() {
       // Init();
        ponerIluminacion(); 
        Sphere malla = new Sphere (30, 30, 1.9f);
        Geometry geom = new Geometry("bola", malla);
        TangentBinormalGenerator.generate(geom);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
        mat.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
        mat.setBoolean("UseMaterialColors",true);
        mat.setColor("Diffuse",ColorRGBA.LightGray);
        mat.setColor("Specular",ColorRGBA.White);
        mat.setFloat("Shininess", 64f); // 0, 128
        geom.setMaterial(mat);
        geom.move(0,10,0);
        Box suelo = new Box(20f, 0.1f, 20f);
        Geometry geom_suelo = new Geometry("Suelo", suelo);
        Material mat2;
        mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex2 = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        mat2.setTexture("ColorMap", tex2); //este material no tiene sombras…
        geom_suelo.setMaterial(mat2);
        geom_suelo.setLocalTranslation(0, -0.1f, 0);
        rootNode.attachChild(geom_suelo);
        cam.setLocation(new Vector3f(0, 2f, 5f));
        cam.lookAt(new Vector3f(0, 1, 0), Vector3f.UNIT_Y);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.attachChild(geom);
//        Box b = new Box(1, 1, 1);
//        player = new Geometry("blue box", b);
//        Material mat = new Material(assetManager,
//                "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.setColor("Color", ColorRGBA.Blue);
//        player.setMaterial(mat);
        
//        Sphere bola = new Sphere(32, 32, 0.4f);
//        Geometry geom_bola = new Geometry("Mi textura", bola);
//        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
//        Texture tex = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
//        mat.setTexture("DiffuseMap", tex);
//        geom_bola.setMaterial(mat);
//
//        Sphere c = new Sphere(32, 32, 2);
//        player2 = new Geometry("blue sphere", c);
//        player2.setMaterial(mat);
//        player2.move(5, 0, 0);
//
//        rootNode.attachChild(geom_bola);
//        rootNode.attachChild(player2);
    }

    @Override
    public void simpleUpdate(float tpf) {
//        player.rotate(0,2*tpf,0);
//        player2.rotate(0,-2*tpf,0);
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
