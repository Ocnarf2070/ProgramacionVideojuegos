package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
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

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    public static void main(String args[]) { 
        Main app = new Main(); app.start(); 
    } 
    
    private BulletAppState estadosFisicos = new BulletAppState();
    private RigidBodyControl fisicaObjetivo; Geometry objetivo; Material mat;
    @Override
    public void simpleInitApp() {
        stateManager.attach(estadosFisicos);
        cam.setLocation(new Vector3f(2, 2f, 6f)); //this.flyCam.setEnabled(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        rootNode.addLight(new DirectionalLight((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal()));
        mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
        //CREACION objetivo (globo)
        objetivo = new Geometry("objetivo", new Box(0.2f, 0.2f, 0.2f));
        objetivo.setMaterial(mat);
        rootNode.attachChild(objetivo);
        objetivo.setLocalTranslation(new Vector3f(0.5f,2f,-0.5f));
        fisicaObjetivo = new RigidBodyControl(500f);
        objetivo.addControl( fisicaObjetivo );
        estadosFisicos.getPhysicsSpace().add( fisicaObjetivo);
        fisicaObjetivo.setGravity(Vector3f.ZERO); //Simulando que el globo no sube ni baja
        //CREACION SUELO (o MAR)
        Geometry suelo_geo = new Geometry("Suelo", new Box(100f, 0.01f, 100f));
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setColor("Color", ColorRGBA.Blue);
        suelo_geo.setMaterial(mat2);
        rootNode.attachChild(suelo_geo);
    }
    private int i = 0;
    private List<Geometry> bolas = new ArrayList<>();
    private List<Geometry> Rbolas = new ArrayList<>();
    private float counter = 0f;
    
    @Override
    public void simpleUpdate(float tpf) {
        RigidBodyControl fisicaBalon=null;
        if(counter>0.05){
            counter=0;
            Geometry balon = new Geometry("balon"+addOne(), new Sphere(32, 32, 0.05f, true, false));
            balon.setMaterial(mat);
            bolas.add(balon);
            rootNode.attachChild(balon);
            fisicaBalon = new RigidBodyControl(1f);
            balon.addControl( fisicaBalon );
            estadosFisicos.getPhysicsSpace().add( fisicaBalon );
            Vector3f Vo = calcularVelocidadInicial (objetivo.getWorldTranslation()); //Calcula Fuerza impulsiva= masa * velocidad por X, Y y Z
            fisicaBalon.applyImpulse(new Vector3f(fisicaBalon.getMass()*Vo.x,fisicaBalon.getMass()*Vo.y,fisicaBalon.getMass()*Vo.z), Vector3f.ZERO);
            
        }else{
            counter += tpf;
            //System.out.print(counter);
        }
        if(bolas.size()>20){
            for(Geometry ball : bolas){
                
                if(ball.getLocalTranslation().y <= -5){
                    System.out.println(ball.getName()+" "+ball.getLocalTranslation());
                    rootNode.detachChild(ball);
                    ball.removeControl(fisicaBalon);
                    Rbolas.add(ball);
                }
            }
            for(Geometry ball : Rbolas){
                bolas.remove(ball);
            }
            System.out.println("---------------------------------------------");
        }
        
    }
    
    private String addOne(){
        String res = (i++)+"";
        return res;
    }
    
    
    public Vector3f calcularVelocidadInicial ( Vector3f posObj ){
        Vector3f X= new Vector3f ( posObj.x, 0, 0).normalize(); //Eje X
        Vector3f Z= new Vector3f (posObj.x, 0, posObj.z).normalize(); //Vector distancia objetivo a "nivel de tierra"
        double anguloXZ = Math.acos( X.dot(Z)) /(X.length()*Z.length()); //Calcula angulo entre dos vectores. Sin signo
        if ((posObj.z<=0)&& (posObj.x<0)) {
            anguloXZ = -anguloXZ + Math.PI; //Asigna signo y ajusta dependiendo
        }
        if (posObj.z>0) {
            if (posObj.x>=0) {
                anguloXZ = -anguloXZ;
            } else {
                anguloXZ += Math.PI; // del cuadrante
            }
        }
        double L = Math.sqrt (posObj.x*posObj.x + posObj.z*posObj.z); //Calcula distancia a objetivo"a nivel de tierra"
        double angulo = Math.atan(0.66692f + 2* posObj.y/L); //Calcula angulo mínimo de disparo.
        float Vo = (float) Math.sqrt(L*9.8f/(2*Math.cos(angulo)*Math.cos(angulo)*(Math.tan(angulo)- posObj.y/L))); //Vo: múdulo velocidad de disparo
        return new Vector3f(Vo*(float)(Math.cos(angulo)*Math.cos(anguloXZ)),Vo*(float)Math.sin(angulo),-Vo*(float)(Math.cos(angulo)*Math.sin(anguloXZ)));
    }
} //devuelve preyecciones de Vo en ej

