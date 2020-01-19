package mygame;

import Auxiliares.Jugadores;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import Auxiliares.Mapa;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private Mapa mapa;

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

    @Override
    public void simpleInitApp() {
        try {
            crearMapa(new File("D:\\Users\\franc\\Google Drive\\Ingenieria de Informatica\\Curso2017-18\\Programación Videojuegos\\AEstrella\\Mapas\\AR0011SR.map"));
            Jugadores jp = new Jugadores(mapa);
            
            Material matj1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matj1.setColor("Color", ColorRGBA.Green);
        Material matj2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matj2.setColor("Color", ColorRGBA.Blue);
        jp.getJ1().setMaterial(matj1);
        jp.getJ2().setMaterial(matj2);
        rootNode.attachChild(jp.getJ1());
        rootNode.attachChild(jp.getJ2());
        
            ponerIluminacion();
           flyCam.setMoveSpeed(10);
            cam.setLocation(new Vector3f(0, 10, 0));
            cam.lookAt(new Vector3f(0,0,0), Vector3f.UNIT_Y);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
   
    private void crearMapa(File file) throws FileNotFoundException{
        mapa = new Mapa(file);
        float x =mapa.getVal_x()/2, y= mapa.getVal_y()/2;
        Box b = new Box(x, 0.1f, y);
        Geometry map = new Geometry("Mapa", b);
        System.out.println(map.getModelBound());
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg"));
        map.setMaterial(mat);
        rootNode.attachChild(map);
        int num=0;
        Node aux = new Node();
        for(int i=0;i<mapa.getVal_x();i++) {
            for(int j=0;j<mapa.getVal_y();j++) {
                if(mapa.getMap()[j][i]=='@'){
                //if(mapa.getMap()[j][i]=='@'&&pared(i, j)){
                    float tampared = 0.5f;
                    Box pared = new Box(tampared, 0.1f
                            , tampared);
                    Geometry geom = new Geometry("pared"+num, pared);
                    num++;
                    Material matpar = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                    matpar.setColor("Color", ColorRGBA.Brown);
                    geom.setMaterial(matpar);
                    geom.setLocalTranslation(x-i-tampared, pared.yExtent+0.1f,y-j-tampared);
                    aux.attachChild(geom);
                    
                }
            }
            GeometryBatchFactory.optimize(aux);
        }
        rootNode.attachChild(aux);
        //System.out.println(mapa.toString());
    }

    private boolean pared(int i, int j) {
        //System.out.println("-----------------------------------------------");
        boolean ver=false;
        int minX=0,minY=0,maxX=mapa.getVal_x()-1,maxY=mapa.getVal_y()-1;
        if(i!=0)minX=i-1;
        if(i+1!=mapa.getVal_x())maxX=i+1;
        if(j!=0)minY=j-1;
        if(j+1!=mapa.getVal_y())maxY=j+1;
        int x = minX,y;
        while(x<=maxX&&!ver){
            y =minY;
            while(y<=maxY&&!ver){
                //System.out.print(mapa.getMap()[x][y]);
                if(mapa.getMap()[y][x]=='.')ver = true;
                y++;
            }
            //System.out.println();
            x++;
        }
        //System.out.println(ver);
        return ver;
    }
}
