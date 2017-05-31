/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappybird;

import static flappybird.FlappyBird.g;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import pkg2dgamesframework.AFrameOnImage;
import pkg2dgamesframework.Animation;
import pkg2dgamesframework.GameScreen;

/**
 *
 * @author VietThang
 */
public class FlappyBird extends GameScreen{

    private BufferedImage birds;
    private Animation bird_anim;
    
    private ChimneyGroup chimneyGroup;
    
    public static float g = 0.25f;
    private Bird bird;
    
    private Ground ground;
    

    
    private int BEGIN_SCREEN = 0;
    private int GAMEPLAY_SCREEN = 1;
    private int GAMEOVER_SCREEN = 2;
    
    private int CurrentScreen = BEGIN_SCREEN;
    
    private int point = 0;
    private int best = 0;
    
    
    
    public FlappyBird() {
        super(800, 600);
        
        try {
            birds = ImageIO.read(new File("Assets/bird_sprite.png"));
            
        } catch (IOException ex) {
            Logger.getLogger(FlappyBird.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        AFrameOnImage f;
        bird_anim = new Animation(70);
        f = new AFrameOnImage(0, 0, 60, 60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60, 0, 60, 60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(120, 0, 60, 60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60, 0, 60, 60);
        bird_anim.AddFrame(f);
        
        bird = new Bird(350, 250, 50, 50);
        
        ground = new Ground();
        
        chimneyGroup = new ChimneyGroup();
        
        BeginGame();
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        new FlappyBird();
    }
    
    private void resetGame(){
        
        bird.setPos(350, 250);
        bird.setVT(0);
        bird.setLive(true);
        point = 0;
        chimneyGroup.resetChimneys();  
    }
    
    @Override
    public void GAME_UPDATE(long deltaTime) {
        
        if (CurrentScreen == BEGIN_SCREEN){
            resetGame();
        }
        else if (CurrentScreen == GAMEPLAY_SCREEN){
            if (bird.getLive() == true)
                bird_anim.Update_Me(deltaTime);
            bird.update(deltaTime);
            ground.update();
            
            chimneyGroup.update();
            
            for (int i = 0; i < ChimneyGroup.SIZE; i++){
                if (bird.getRect().intersects(chimneyGroup.getChimney(i).getRect())){
                    bird.setLive(false);
                    bird.fallSound.play();
                    CurrentScreen = GAMEOVER_SCREEN;
                }
            
            }
            
            for (int i = 0; i < ChimneyGroup.SIZE; i++){
                if (bird.getPosX() > chimneyGroup.getChimney(i).getPosX() && chimneyGroup.getChimney(i).getIsBehindBird() == false
                        && i%2 == 0){
                    point++;
                    bird.getPointSound.play();
                    chimneyGroup.getChimney(i).setIsBehindBird(true);
                }
            }
            
            if (bird.getPosY() + bird.getH() > ground.getYGround() || bird.getPosY() + bird.getH() < 0)
                CurrentScreen = GAMEOVER_SCREEN;
        }
        else {
            
        }
        if (point > best)
            best = point;
        
    }
    
    @Override
    public void GAME_PAINT(Graphics2D g2) {
        g2.setColor(Color.decode("#b8daef"));
        g2.fillRect(0, 0, MASTER_WIDTH, MASTER_HEIGHT);
        chimneyGroup.Paint(g2);
        ground.Paint(g2);
        
        if (bird.getIsFlying() == true)
            bird_anim.PaintAnims((int) bird.getPosX(),(int) bird.getPosY(), birds, g2, 0, -1);
        else
            bird_anim.PaintAnims((int) bird.getPosX(),(int) bird.getPosY(), birds, g2, 0, 0);
        
        
        if (CurrentScreen == BEGIN_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space to play game", 200, 300);
        }
        if (CurrentScreen == GAMEOVER_SCREEN){
            g2.setColor(Color.red);
            g2.drawString("Press space to turn back begin screen", 200, 300);
        }
        
        g2.setColor(Color.red);
        g2.drawString("Point: " + point, 20, 50);
        g2.drawString("Best: " + best, 20, 80);
    }

    @Override
    public void KEY_ACTION(KeyEvent e, int Event) {
        if (Event == KEY_PRESSED){
            if (CurrentScreen == BEGIN_SCREEN) {
                CurrentScreen = GAMEPLAY_SCREEN;
            }
            else if (CurrentScreen == GAMEPLAY_SCREEN) {
                if (bird.getLive())
                    bird.fly();
            }
            else if (CurrentScreen == GAMEOVER_SCREEN) {
                CurrentScreen = BEGIN_SCREEN;
            }
        }
    }
    
}
