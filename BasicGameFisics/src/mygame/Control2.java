/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author franc
 */
public class Control2 extends AbstractControl{

    public Control2() {
    }

    @Override
    protected void controlUpdate(float tpf) {
        spatial.move(tpf, 0, 0);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
