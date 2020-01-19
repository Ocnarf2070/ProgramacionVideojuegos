package mygame;

import PreFabs.Golem;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.texture.Texture2D;
import com.jme3.water.WaterFilter;
import mygame.PreFabs.AvionFisico;
import mygame.PreFabs.ModeloFisicoEstatico;
public class Main extends SimpleApplication {
    public static void main(String args[]) {  Main app = new Main();   app.start();  }
    private BulletAppState  estadosFisicos = new BulletAppState();       public  static Material matPorDefecto=null;
    private AvionFisico avion1, avion2;  float tiempo=0; boolean proyectilSoltado;
    private ModeloFisicoEstatico pueblo;   Golem golem;         WaterFilter water;
    
    @Override
    public void simpleInitApp() {
        stateManager.attach(estadosFisicos);
        DirectionalLight sun = new DirectionalLight((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        AmbientLight al = new AmbientLight(new ColorRGBA(2, 2, 2, 1.0f));
        rootNode.addLight(sun);  rootNode.addLight(al);
        cam.setLocation(new Vector3f(-50f, 0f, 70f)); 
        //this.flyCam.setEnabled(false);
        this.flyCam.setMoveSpeed(10f);
        this.cam.lookAt(new Vector3f(30.5f, -10, 85f), Vector3f.ZERO);
        Material mat = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat. setTexture("DiffuseMap",assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg")); matPorDefecto = mat.clone();
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        
        
        //CREACION DE OBJETOS 3D A PARTIR DE PREFABRICADOS:
        pueblo    =   new ModeloFisicoEstatico( 0, assetManager.loadModel("main.scene"), null, estadosFisicos,  rootNode, assetManager);
        avion1    =   new AvionFisico (10, mat, "Avion_1", estadosFisicos, rootNode, assetManager);
        avion2    =   new AvionFisico (1f, mat,  "Avion_2", estadosFisicos, rootNode, assetManager);
        golem     =   new Golem(5.5f, 14f, 100f,  "Golem_1", estadosFisicos, rootNode, assetManager);
        
        
        //Añadiendo agua
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        water = new WaterFilter(rootNode, new Vector3f(-1, -1, -1));
        water.setWaterTransparency(0.4f);
        water.setFoamIntensity(0.02f);
        water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
        water.setWaterHeight(-1.5f);                   //ALTURA DEL AGUA
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
        //Añadiendo capacidad de dar sombras
        //FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 2);
        dlsf.setEnabledStabilization(true);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        fpp.addFilter(dlsf);
        
        //POSICIONAMIENTO DE OBJETOS EN ESCENA Y ASIGNACION DE ROLES
        pueblo.setPhysicsLocation(new Vector3f (0,-15,50));
        pueblo.modelo3D.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        avion1.setPhysicsLocation(new Vector3f (4, 15f, -10));
        avion1.remolcar(avion2);             //TAMBIEN:     //avion2.asignarObjetivo( avion1.geometria);
        avion2.setPhysicsLocation(new Vector3f (4, 15f,-15));
        avion2.setGravity(new Vector3f (0, -10 , 0));
        avion2.setRestitution(0.01f);
        golem.personaje.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        golem.situar (new Vector3f (10,-14,65));
        golem.ir(new Vector3f( 4,  0, -5)); 
    }
    boolean a=false;
    @Override
    public void simpleUpdate(float tpf) {
        tiempo=tiempo+tpf;
        if (tiempo>6 && tiempo<12.7) water.setWaterHeight(-1.5f-2*(tiempo-6));                   //ALTURA DEL AGUA
        golem.hacerGuardia(2,tpf);
        avion1.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
        avion2.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);;
        avion1.setLinearVelocity(new Vector3f (20f*(float)Math.cos(tiempo/2f), 0,20f*(float)Math.sin(tiempo/2f)));
        if (tiempo>16.7 && !proyectilSoltado){                                                                           //SOLTAR EL PROYECTIL EN EL SEGUNDO 4
        estadosFisicos.getPhysicsSpace().remove(avion1.remolque);
        avion2.estado= "caidaLibre";
        proyectilSoltado=true;
        }
    }
}
