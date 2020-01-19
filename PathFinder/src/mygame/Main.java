package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.List;
import mygame.astar.*;

public class Main extends SimpleApplication {
      Geometry geom=null;
      boolean[][] mapa;
      Point inicio, objetivo;
      SearchParameters parametrosBusqueda;
      float tiempo =0;
      int ultimoTiempoEntero =0;
      private BulletAppState estadosFisicos;

    public static void main(String[] args) {
        Main app = new Main();      
        app.start();
    }

 @Override
 public void simpleInitApp() {
        estadosFisicos = new BulletAppState();
        stateManager.attach(estadosFisicos);
        this.flyCam.setEnabled(false);
        cam.setLocation(new Vector3f(0, 2f, 10f));

        Box b = new Box(0.5f, 0.5f, 0.5f);
        geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        inicializaMapa();    
    }

    public void inicializaMapa()  {
          //Inicializa mapa
          this.mapa = new boolean[5][5];   //Coordenadas X , Z
          inicio     = new Point(0, 0);
          objetivo = new Point(4, 4);
          this.parametrosBusqueda = new SearchParameters(inicio, objetivo, mapa);

          //ColocaObstaculos();
          this.mapa[1][1] = true;
          this.mapa[1][2] = true;
          this.mapa[1][3] = true; 
          this.mapa[2][1] = true;
          this.mapa[3][1] = true;
          this.mapa[3][4] = true;
          this.mapa[3][3] = true;
          this.mapa[3][0] = true;
          this.mapa[3][4] = true;
          this.mapa[3][4] = true;
          for(int i = 0;i<mapa.length;i++){
              for(int j = 0; j< mapa[0].length;j++){
                  if(mapa[i][j]){
                      Box b = new Box(0.5f, 0.5f, 0.5f);
                      Geometry pared = new Geometry("Box", b);
                      Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                      mat.setColor("Color", ColorRGBA.Red);
                      pared.setMaterial(mat);
                      pared.setLocalTranslation(i, j, 0);
                      rootNode.attachChild(pared);
                  }
              }
          }
        }


   @Override
   public void simpleUpdate(float tpf) {
       tiempo += tpf;

       if (ultimoTiempoEntero != (int) tiempo){
              ultimoTiempoEntero = (int) tiempo;
 
              //Lee la posicion actual como número entero, la situa en el mapa, y busca el mejor camino
              int Xbox = (int) geom.getLocalTranslation().x;
              int Ybox = (int) geom.getLocalTranslation().y;
              if ( Xbox==objetivo.X && Ybox ==objetivo.Y){
                     System.out.println("Objetivo alcanzado");     
                     return;
              }
              System.out.println("Pocición actual real 3D = "+ geom.getLocalTranslation().toString()+"\n");
              Point PosicionActual = new Point(Xbox, Ybox);

              //Da parametros y ejecuta algortimo A*
              this.parametrosBusqueda = new SearchParameters( PosicionActual, objetivo, mapa);
              PathFinder pathFinder = new PathFinder(this.parametrosBusqueda);

              //Ejecucion del algortimo A*
              List<Point> path = pathFinder.FindPath();

              //Posiciona la caja en la primera posicón del camino  
              if (path!=null && path.size()>0 && path.get(0)!=null ){
                 Vector3f nuevaPosicion = new Vector3f( path.get(0).X,  path.get(0).Y, 0);
                 geom.setLocalTranslation( nuevaPosicion );

              }else {
                  System.out.println("NO HAY CAMINO POSIBLE");
              }
       }
    }

    @Override
    public void simpleRender(RenderManager rm) { }
}
