package PreFabs;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

public class ModeloGeometricoMovil extends AbstractControl {
      Geometry thisGeometria=null, geomObj=null;
      boolean objtivoAsignado;
      float velocidad=0.9f;
      
 public ModeloGeometricoMovil(Geometry geometria, Material mat, Node rootNode, com.jme3.asset.AssetManager assetManager){
      thisGeometria = geometria;
      thisGeometria.setMaterial(mat);
      rootNode.attachChild(thisGeometria);              
      thisGeometria.addControl( this ); 
}

@Override
protected void controlUpdate(float tps) {
    if (objtivoAsignado){
       Vector3f dir = new Vector3f (geomObj.getWorldTranslation().x- thisGeometria.getWorldTranslation().x, 
                                    geomObj.getWorldTranslation().y- thisGeometria.getWorldTranslation().y, 
                                    geomObj.getWorldTranslation().z- thisGeometria.getWorldTranslation().z).normalize() ;          
       float microDesplazamiento = velocidad*tps;
       thisGeometria.move(dir.mult(microDesplazamiento));
       thisGeometria.lookAt(geomObj.getWorldTranslation(), Vector3f.UNIT_Y);
    }
}
public void asignarObjetivo (Geometry geomObje){
    objtivoAsignado=true;
    geomObj= geomObje;
 }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp){
 }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}
