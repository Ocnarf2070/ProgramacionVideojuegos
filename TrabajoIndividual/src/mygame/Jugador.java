/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Box;
import javafx.util.Pair;

/**
 *
 * @author franc
 */
public class Jugador{
    private Geometry jugador;
    private Box b;
    public Jugador(SimpleApplication app,Pair<Integer,Integer> lugar) throws InterruptedException {
        b = new Box(1, 1, 1);
        jugador = new Geometry("Box", b);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        jugador.setMaterial(mat);
        jugador.setLocalTranslation(lugar.getKey(), b.yExtent, lugar.getValue());
        
        app.getRootNode().attachChild(jugador);
        jugador.setUserData("Velocidad", 30f);
        jugador.setUserData("Movilidad", 50f);
        jugador.setUserData("Objetos", 0);  
    }

    public Geometry getJugador() {
        return jugador;
    }

    public Box entornoJugador() {
        return b;
    }
    
    

    void Control(AbstractControl control) {
        jugador.addControl(control);
    }
  
    
}
