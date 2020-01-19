package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

public class BulletTest extends SimpleApplication {
    
    public static void main(String args[]) {
        BulletTest app = new BulletTest();
        app.start();
    }
    
    /** Prepare the Physics Application State (jBullet) */
    private BulletAppState bulletAppState;
    
    /** Prepare Materials */
    Material wall_mat;
    Material slow_mat;
    Material fast_mat;
    Material floor_mat;
    
    
    private RigidBodyControl    brick_phy;
    private static final Box    box;
    private static final Sphere sphere;
    private RigidBodyControl    floor_phy;
    private static final Box    floor;
    
    /** dimensions used for wall */
    private static final float brickLength = 2f;
    private static final float brickWidth  = 0.015f;
    private static final float brickHeight = 1f;
    
    static {
        /** Initialize the cannon ball geometry */
        sphere = new Sphere(32, 32, 0.1f, true, false);
        /** Initialize the brick geometry */
        box = new Box(brickWidth, brickHeight, brickLength);
        /** Initialize the floor geometry */
        floor = new Box(10f, 0.1f, 5f);
        
    }
    
    private List<Pair<Geometry,RigidBodyControl>> bullets = new ArrayList<>();
    private List<Pair<Geometry,RigidBodyControl>> RBullets = new ArrayList<>();
    
    @Override
    public void simpleInitApp() {
        /** Set up Physics Game */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        /** Configure cam to look at scene */
        cam.setLocation(new Vector3f(0, 4f, 6f));
        cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);
        
        /** Initialize the scene, materials, and physics space */
        
        initMaterials();
        initBox();
        initWall();
        initFloor();
        setUpHUDText();
        flyCam.setMoveSpeed(10);
    }
    
    public static final float FIREPERIOD=0.5f;
    double fireTimer=0;
    
    public static final float TIMELIFE=10f;
    private float frameTime;
    
    @Override
    public void simpleUpdate(float tpf) {
        fireTimer+=tpf;
        frameTime=tpf;
        
        hudText.setText("Nº colitions on the wall: "+choques);
        if (fireTimer>FIREPERIOD){
            fireTimer=0;
             makeCannonBall(geo.getWorldTranslation(),new Vector3f(15,0.5f,0),slow_mat); //slow arcing ball
           // makeCannonBall(new Vector3f(-4,3,-0.5f),new Vector3f(10,-1,0),fast_mat,continuouslySweeping); //fast straight ball
        }
        Balas();
        borrarBalas();
        Colision();
       
           
    }
    
    private void Balas(){
         if(!bullets.isEmpty()){
             for(Pair tuple : bullets){
                 RigidBodyControl fisic = (RigidBodyControl) tuple.getValue();
                 fisic.setLinearVelocity(fisic.getLinearVelocity());
                 fisic.applyCentralForce(new Vector3f(0, fisic.getMass()*fisic.getGravity().length(), 0));
             }
         }
    }
    
    private void borrarBalas(){
        
        for(Pair tuple : bullets){
            Geometry bullet = (Geometry) tuple.getKey();
            RigidBodyControl fisic = (RigidBodyControl) tuple.getValue();
            float time = bullet.getUserData("time");
            boolean collition = bullet.getUserData("choque");
            
            if(time>=TIMELIFE||collition){
                rootNode.detachChild(bullet);
                bullet.removeControl(fisic);
                RBullets.add(tuple);
            }else{
                time += frameTime;
                bullet.setUserData("time", time);
            }
        }
        for(Pair ball : RBullets){
            
            bullets.remove(ball);
        }
    }
    int choques=0;
    private void Colision(){
        CollisionResults result = new CollisionResults();
        List<Geometry> list = new ArrayList<>();
        for(Pair tupla: bullets){
            //System.out.println(brick_geo.getWorldBound());
            Geometry aux = (Geometry) tupla.getKey();
            aux.collideWith(brick_geo.getWorldBound(), result);
            boolean choque = aux.getUserData("choque");
            if (result.size() > 0&&!choque) {
                choques++;
                aux.setUserData("choque", true);
                
            }
            result=new CollisionResults();
        }
    }
    
    
    public BitmapText hudText;
    
    private void setUpHUDText(){
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.White);                             // font color
        hudText.setText("ContinouslySweeping=true");             // the text
        hudText.setLocalTranslation(200, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);
    }
    
    /** Initialize the materials used in this scene. */
    public void initMaterials() {
        wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wall_mat.setColor("Color", ColorRGBA.Blue);
        
        fast_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        fast_mat.setColor("Color", ColorRGBA.Red);
        
        slow_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        slow_mat.setColor("Color", ColorRGBA.Green);
        
        floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floor_mat.setColor("Color", ColorRGBA.Gray);
    }
    
    private RigidBodyControl caja_phy;Geometry geo;
    public void initBox(){
        
        Box caja = new Box(1, 1, 1);
        geo = new Geometry("Caja", caja);
        geo.setMaterial(wall_mat);
        geo.setLocalTranslation(new Vector3f(-4,3,0));
        rootNode.attachChild(geo);
        caja_phy = new RigidBodyControl(2f);
        geo.addControl(caja_phy);
        bulletAppState.getPhysicsSpace().add(caja_phy);
    }
    
    /** Make a solid floor and add it to the scene. */
    public void initFloor() {
        Geometry floor_geo = new Geometry("Floor", floor);
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(0, -0.1f, 0);
        this.rootNode.attachChild(floor_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        bulletAppState.getPhysicsSpace().add(floor_phy);
    }
    Geometry brick_geo;
    /** This loop builds a wall out of individual bricks. */
    public void initWall() {
        
        Vector3f location=new Vector3f(2,1,0);
        brick_geo = new Geometry("brick", box);
        brick_geo.setMaterial(wall_mat);
        rootNode.attachChild(brick_geo);
        /** Position the brick geometry  */
        brick_geo.setLocalTranslation(location);
        
        //paper thin objects will fall down, mass 0 clamps it in position
        brick_phy = new RigidBodyControl(0);
        
        /** Add physical brick to physics space. */
        brick_geo.addControl(brick_phy);
        bulletAppState.getPhysicsSpace().add(brick_phy);
    }
    
    
    public void makeCannonBall(Vector3f startPoint,Vector3f velocity, Material material) {
        /** Create a cannon ball geometry and attach to scene graph. */
        Geometry ball_geo = new Geometry("cannon ball "+addOne(), sphere);
        ball_geo.setMaterial(material);
        rootNode.attachChild(ball_geo);
        /** Position the cannon ball  */
         Vector3f VN = velocity.normalize();
        Vector3f off = cubo(VN);
       
        ball_geo.setLocalTranslation(startPoint.add(off));
        /** Make the ball physcial with a mass > 0.0f */
        
        RigidBodyControl ball_phy = new RigidBodyControl(0.1f);
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        ball_phy.setLinearVelocity(velocity);
        
        ball_geo.setUserData("choque", false);
        ball_geo.setUserData("time", 0f);
        
        bullets.add(new Pair<>(ball_geo,ball_phy));
        
    }
    private int i=0;
    private String addOne(){
        String res = (i++)+"";
        return res;
    }

    private Vector3f cubo(Vector3f VN) {
        BoundingBox box = (BoundingBox)geo.getModelBound();
        float x=VN.x*box.getXExtent()/2;
        float y=VN.y*box.getYExtent()/2;
        float z=VN.z*box.getZExtent()/2;
        return new Vector3f(x, y, z);
    }
}