/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliar;

/**
 *
 * @author franc
 */
public class States {
    private int state;
    
    public States() {
        this.state = 0;
    }
    public void changeState(){
        if (state==0) state++;
        else state--;
    }
    
    public int getState() {
        return state;
    }
    
}
