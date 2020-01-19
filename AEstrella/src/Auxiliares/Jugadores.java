/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliares;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.List;
import java.util.Random;

/**
 *
 * @author franc
 */
public class Jugadores extends SimpleApplication{

    Pair<Integer,Integer> spawnJ1, spawnJ2;
    Geometry j1,j2;
    public Jugadores(Mapa map) {
        float x =map.getVal_x()/2, y= map.getVal_y()/2;
        Box jugadores = new Box(0.5f,0.5f,0.5f);
        j1  = new Geometry("jugador1", jugadores);
        j2 = new Geometry("jugador2", jugadores);
        Random rnd = new Random();
        int nJ1=rnd.nextInt(map.getLibreJ1().size());
        int nJ2=rnd.nextInt(map.getLibreJ2().size());
        spawnJ1 = map.getLibreJ1().get(nJ1);
        spawnJ2 = map.getLibreJ2().get(nJ2);
        System.out.println("p1: "+spawnJ1.toString()+"p2: "+spawnJ2.toString());
        j1.setLocalTranslation(x-spawnJ1.getFirst()+1-0.5f, jugadores.yExtent+0.1f, y-spawnJ1.getSecond()+1-0.5f);
        j2.setLocalTranslation(x-spawnJ2.getFirst()+1-0.5f, jugadores.yExtent+0.1f, y-spawnJ2.getSecond()+1-0.5f);
    }

    public Pair<Integer, Integer> getSpawnJ1() {
        return spawnJ1;
    }

    public Pair<Integer, Integer> getSpawnJ2() {
        return spawnJ2;
    }

    public Geometry getJ1() {
        return j1;
    }

    public void setJ1(Geometry j1) {
        this.j1 = j1;
    }

    public Geometry getJ2() {
        return j2;
    }

    public void setJ2(Geometry j2) {
        this.j2 = j2;
    }

    @Override
    public void simpleInitApp() {
    }
    
    
}
