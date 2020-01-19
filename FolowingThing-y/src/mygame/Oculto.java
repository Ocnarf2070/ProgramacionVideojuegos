/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author franc
 */
public class Oculto {
    private static List<Geometry> oculto;
    public static void start(){
        oculto = new ArrayList<>();
    }
    public static void add(Geometry obj){
        oculto.add(obj);
    }
    public static Geometry remove(Geometry obj){
        return oculto.remove(oculto.indexOf(obj));
    }
    public static Geometry remove(int index){
        return oculto.remove(index);
    }

    public static List<Geometry> getOculto() {
        return oculto;
    }
    
    
}
