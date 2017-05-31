/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybird;

import java.awt.Rectangle;
import java.io.File;
import pkg2dgamesframework.Objects;
import pkg2dgamesframework.SoundPlayer;

/**
 *
 * @author VietThang
 */
public class Bird extends Objects{
    
    private float vt = 0;
    
    private boolean isFlying = false;
    
    private Rectangle rect;
    
    private boolean isLive = true;
    
    public SoundPlayer fapSound, fallSound, getPointSound;
    
    public Bird(int x, int y, int w, int h){
        super(x, y, w, h);
        rect = new Rectangle(x, y, w, h);
        fapSound = new SoundPlayer(new File("Assets/fap.wav"));
        getPointSound = new SoundPlayer(new File("Assets/getpoint.wav"));
        fallSound = new SoundPlayer(new File("Assets/fall.wav"));
    }
    
    public void update(long deltaTime){
        
        vt += FlappyBird.g;
        
        this.setPosY(this.getPosY()+vt);
        this.rect.setLocation((int) this.getPosX(), (int) this.getPosY());
        
        
        if (vt < 0) isFlying = true;
        else
            isFlying = false;
    }
    
    public void fly(){
        vt = -3;
        fapSound.play();
    }
    
    public boolean getIsFlying(){
        return isFlying;
    }
    public void setLive(boolean b){
        isLive = b;
    }
    
    public boolean getLive(){
        return isLive;
    }
    public void setVT(float vt){
        this.vt = vt;
    }
    public Rectangle getRect()
    {
        return rect;
    }
}
