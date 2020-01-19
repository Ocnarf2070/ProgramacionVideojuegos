/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import PreFrab.Balas;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author franc
 */
public class Disparo extends AbstractControl{
    private List<Balas> balas;
    private float time =0;
    private Spatial spationalObjetivo;
    private Spatial player=null;
    BulletAppState estadosFisicos;
    private Node rootNode; private AssetManager assetManager;

    public Disparo(Spatial player,Spatial spationalObjetivo, BulletAppState estadosFisicos, Node rootNode, AssetManager assetManager) {
        balas = new ArrayList<>();
        this.player=player;
        this.spationalObjetivo = spationalObjetivo;
        this.estadosFisicos = estadosFisicos;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }
    
    
    
    @Override
    protected void controlUpdate(float tpf) {
        
        if(time==0){
            Balas bala = new Balas(player, estadosFisicos, rootNode, assetManager);
            bala.AsignarLugar(spationalObjetivo);
            balas.add(bala);
            
        }
        if(!balas.isEmpty()){
            for(Iterator<Balas> it = balas.iterator();it.hasNext();){
                Balas b = it.next();
                if(!rootNode.hasChild(b.getGeometria())) {
                    it.remove();
                }
            }
            for(Balas b : balas) {
                b.physicsTick(estadosFisicos.getPhysicsSpace(), tpf);
            }
        }
        time+=tpf;
        if(time>0.5f)time=0;
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
