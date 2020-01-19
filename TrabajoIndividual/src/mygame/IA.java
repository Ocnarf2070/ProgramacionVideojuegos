/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franc
 */
public class IA extends AbstractControl{

    

    private Vector3f origen;
    private Vector3f destino;
    private final Vector3f spawn;
    private SimpleApplication app;
    
    public IA(Vector3f origen,Vector3f spawn,SimpleApplication app) throws InterruptedException {
        this.spawn=spawn;
        this.origen=origen;
        this.destino=Prioridades.first().getWorldTranslation();
        this.app=app;
        //Objetivos.verDistCola();
    }
    
    public void nuevaDireccion(Vector3f destino) {
        this.destino = destino;
        //Objetivos.verDistCola();
    }
    float velOrg;
    private boolean primera = true;
    @Override
    protected void controlUpdate(float tpf) {
        
        try {
            int red =(int) Math.pow(2,(Integer)spatial.getUserData("Objetos"));
            float vel = ((Float)spatial.getUserData("Velocidad"))/red;
            if(destino!=null){
                spatial.setUserData("Movilidad", (Float)spatial.getUserData("Movilidad")-tpf);
                if(Math.round((Float)spatial.getUserData("Movilidad"))==0)spatial.setUserData("Velocidad", 0f);
                if(!destino.equals(spawn)&&!Prioridades.vacio())destino=Prioridades.first().getWorldTranslation();
                //System.out.println((Float)spatial.getUserData("Movilidad"));
                Vector3f dir = (destino.subtract(origen)).normalize();
                //System.out.println("Spawn "+spawn);
                spatial.move(dir.mult(vel*tpf));
                if(llegada()){
                    if(destino.equals(spawn)){
                        spatial.setUserData("Objetos",0);
                        spatial.setUserData("Movilidad", 100f);
                        if(!Prioridades.vacio()){
                            nuevaDireccion(Prioridades.first().getWorldTranslation());
                        } else if (!Oculto.getOculto().isEmpty()){
                            Random rnd = new Random();
                            Geometry goe = Oculto.remove(rnd.nextInt(Oculto.getOculto().size()));
                            Prioridades.add(goe);
                            app.getRootNode().attachChild(goe);
                        } else{
                            destino=null;
                        }
                    }
                    else if(!Prioridades.vacio()){
                        spatial.setUserData("Objetos", (Integer)spatial.getUserData("Objetos")+1);
                        int red2 =(int) Math.pow(2,(Integer)spatial.getUserData("Objetos"));
                        float vel2 = ((Float)spatial.getUserData("Velocidad"))/red2;
                        if(vel2==0){
                            destino=null;
                        }
                        else if(vel2<10){
                            nuevaDireccion(spawn);
                            Geometry aux = Prioridades.remove();
                            app.getRootNode().detachChild(aux);
                        }else{
                            Geometry aux = Prioridades.remove();
                            app.getRootNode().detachChild(aux);
                            if(!Prioridades.vacio()){
                                nuevaDireccion(Prioridades.first().getWorldTranslation());
                            }
                        }
                    }else{
                        nuevaDireccion(spawn);
                    }
                }
             
                
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(IA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public boolean llegada(){
        return Math.round(destino.distance(origen))==0;
    }
   
}
