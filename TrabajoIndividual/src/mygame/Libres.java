/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author franc
 */
public class Libres {
    private static List<Pair<Integer,Integer>> libres;
    public static void start(int maxX,int maxZ){
        libres = new ArrayList<>();
        for(int i=0;i<maxX;i++){
            for(int j=0;j<maxZ;j++){
                libres.add(new Pair<>(i,j));
            }
        }
    }
    
    public static Pair<Integer,Integer> remove(int i){
        return libres.remove(i);
    }
    
    static int tam() {
        return libres.size()-1;
    }
}
