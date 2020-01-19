/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package PreFrab;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author franc
 */
public class Balas extends RigidBodyControl implements PhysicsCollisionListener,PhysicsTickListener {
    BulletAppState estadosFisicos;
    private Spatial geometria=null;
    private Vector3f origen, objetive;
    private Spatial player;
    private Node rootNode;
    
    
    public Balas(Spatial origen, BulletAppState estadosFisicos, Node rootNode, AssetManager assetManager){
        super(0.1f);
        player = origen;
        this.origen=origen.getWorldTranslation();
        Sphere c = new Sphere(30, 30, 0.1f);
        Geometry geom = new Geometry("Bala",c);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color",ColorRGBA.Orange);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
        geometria = geom;
        geometria.addControl( this );
        estadosFisicos.getPhysicsSpace().add( this );
        estadosFisicos.getPhysicsSpace().addCollisionListener(this);
        geometria.setLocalTranslation(this.origen);
        this.estadosFisicos= estadosFisicos;
        this.rootNode=rootNode;
        
        geometria.setUserData("time", 0f); 
        
        geometria.setUserData("player", origen);
        
        
    }
        private boolean impactado=false;

    public void collision(PhysicsCollisionEvent event) {
        if(!impactado &&!(event.getNodeA().getName().equals("Box")||event.getNodeA().equals(player)) && event.getNodeB().equals(geometria)){
            rootNode.detachChild(geometria);
            estadosFisicos.getPhysicsSpace().remove(this);
            estadosFisicos.getPhysicsSpace().removeTickListener(this);
            impactado=true;
        }
    }
    
    
    public Spatial getGeometria() {
        return geometria;
    }
    
    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
       
    }
    
    private static final float LIFETIME = 5;
    
    @Override
    public void physicsTick(PhysicsSpace space, float tpf) {
        actualizarTiempo(tpf);
        this.applyCentralForce(new Vector3f(0, this.getMass()*this.getGravity().length(), 0));
        this.setLinearVelocity(objetive.mult(5));
        borrarBala(space);
        
    }
    
    public void AsignarLugar(Spatial objetivo){
        Vector3f lugar= objetivo.getWorldTranslation();
        objetive = (lugar.subtract(geometria.getWorldTranslation())).normalize();
        BoundingBox sphere = (BoundingBox) objetivo.getWorldBound();
        this.setPhysicsLocation(geometria.getWorldTranslation().add(objetive.mult(sphere.getXExtent())));
    }

    
    private void actualizarTiempo(float tpf) {
        float time = geometria.getUserData("time");
        time += tpf;
        geometria.setUserData("time", time);    }
    
    private void borrarBala(PhysicsSpace space) {
        float time = geometria.getUserData("time");
        if(time>LIFETIME){
            rootNode.detachChild(geometria);
            estadosFisicos.getPhysicsSpace().remove(this);

        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
