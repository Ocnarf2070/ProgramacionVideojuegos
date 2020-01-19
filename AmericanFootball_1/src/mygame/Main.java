package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

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
        ponerIluminacion();
        Spatial mundo = assetManager.loadModel("Scenes/Suelo.j3o");
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.attachChild(mundo);
        fisicaSuelo=new RigidBodyControl(0f);
        mundo.addControl(fisicaSuelo);
        estadosFisicos.getPhysicsSpace().add(fisicaSuelo);
        fisicaSuelo.setFriction(0f);
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
    private BulletAppState estadosFisicos = new BulletAppState();
    private RigidBodyControl fisicaBola, fisicaSuelo, fisicaCono,fisicaPoste;
    private float gravedad;
    Geometry cone, ball;
    
    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(10);
        stateManager.attach(estadosFisicos);
        Init();
        float ancho = 0.3f;
        Box p1 = new Box(ancho,3.04f,ancho);
        Box p2 = new Box(5.55f, ancho, ancho);
        Box p3 = new Box(ancho, 9.14f,ancho);
        Box p4 = new Box(ancho, 9.14f,ancho);
        Geometry geom = new Geometry("p1", p1);
        Geometry geom1 = new Geometry("p2", p2);
        Geometry geom2 = new Geometry("p3", p3);
        Geometry geom3 = new Geometry("p4", p4);
        geom.setLocalTranslation(0, p1.yExtent, 0);
        geom1.setLocalTranslation(0,p1.yExtent*2 + p2.yExtent,0);
        geom2.setLocalTranslation(-p2.xExtent+p3.xExtent, p1.yExtent*2+p2.yExtent*2+p3.yExtent, 0);
        geom3.setLocalTranslation(p2.xExtent-p3.xExtent,  p1.yExtent*2+p2.yExtent*2+p4.yExtent, 0);
        Node nodo = new Node();
        nodo.attachChild(geom);
        nodo.attachChild(geom1);
        nodo.attachChild(geom2);
        nodo.attachChild(geom3);
        fisicaPoste=new RigidBodyControl(0f);
        nodo.addControl(fisicaPoste);
        estadosFisicos.getPhysicsSpace().add(fisicaPoste);
        

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex = assetManager.loadTexture("Textures/ColorRamp/toon.png");
        mat.setTexture("ColorMap", tex);
        nodo.setMaterial(mat);

        rootNode.attachChild(nodo);
        
        Sphere bola = new Sphere(32, 32, 0.7f);
        ball = new Geometry("Bola", bola);
        ball.setLocalTranslation(0,bola.radius , 10);
        Material matball = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matball.setTexture("ColorMap", assetManager.loadTexture("Blender/2.4x/WoodCrate_lighter - Height Map.png"));
        ball.setMaterial(matball);
        rootNode.attachChild(ball);
        fisicaBola = new RigidBodyControl(0.70f);
        ball.addControl(fisicaBola);
        estadosFisicos.getPhysicsSpace().add(fisicaBola);
                
        Cylinder cono = new Cylinder(30, 30, 1.5f, 0.01f, 2f, true, false);
        cone = new Geometry("cono",cono);
        cone.setLocalRotation (new Quaternion().fromAngles ((float)Math.PI/2f, 0, 0));
        cone.setLocalTranslation(0,cono.getHeight()/2,13);
        Material matcone = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matcone.setTexture("ColorMap", assetManager.loadTexture("Blender/2.4x/textures/Tar_Cracked.png"));
        cone.setMaterial(matcone);
        rootNode.attachChild(cone);
        fisicaCono = new RigidBodyControl(1f);
        cone.addControl(fisicaCono);
        estadosFisicos.getPhysicsSpace().add(fisicaCono);
        float angulo = angulo2V((float) Math.ceil(p1.yExtent+p2.yExtent), 20f);
     
        Vector3f velocidad = vector2V(angulo, (float) Math.ceil(p1.yExtent+p2.yExtent), false,false);
        fisicaCono.applyForce(new Vector3f(0, 0, -800),Vector3f.ZERO);
                
        cam.setLocation(new Vector3f(30,45,45));
        Vector3f media = new Vector3f(ball.getLocalTranslation().add(nodo.getLocalScale())).divide(2f);
        cam.lookAt(media
                , Vector3f.UNIT_Y);
        
        gravedad = (estadosFisicos.getPhysicsSpace().getGravity(Vector3f.ZERO)).length();       
    }
    private Vector3f vector2V(double angulo, float h, boolean isX, boolean pos){
        float p1 = 2*gravedad *h;
        float p2 = (float) Math.pow(Math.sin(angulo),2);
        float Vi = (float) Math.sqrt(p1/p2);
        //System.out.println(Vi);
        float xi=0,yi,zi=0;
        yi = Vi * (float) Math.sin(angulo);
        if (isX){
            xi = Vi * (float) Math.cos(angulo);
            xi = pos?xi:-xi;
        }
        else{
            zi = Vi * (float) Math.cos(angulo);
            zi = pos?zi:-zi;
        }
        return new Vector3f(xi, yi, zi);
    }
    
    private float angulo2V(float ymax, float xmax){
        float tg = (4*ymax)/xmax;
        //System.out.println(tg);
        return (float) Math.atan(tg);
    }

    @Override
    public void simpleUpdate(float tpf) {
        System.out.println("Cono: "+fisicaCono.getAngularVelocity());
        System.out.println("Bola: "+fisicaBola.getAngularVelocity());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
