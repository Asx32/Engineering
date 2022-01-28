/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Asx
 */
public class Bullet extends GameObject{
    
    private final boolean enemy;
    
    public Bullet(int _x, int _y, int _sy, boolean enemy)
    {
        super(_x, _y);
        this.speedY = _sy;
        this.enemy = enemy;
    }
    
    @Override
    public Rectangle getHitbox()
    {
        return new Rectangle(x, y, GlobalVars.bulletSizeX, GlobalVars.bulletSizeY);
    }

    @Override
    public void tick()
    {
        y += speedY;
        //sprawdzenie kolizji?
        if((this.y < 0) || (this.y > GlobalVars.gameHeight)) this.alive = false;
    }

    @Override
    public void render(Graphics g) 
    {
        if(enemy) g.setColor(Color.red);
        else
            g.setColor(Color.cyan);
        g.fillRect(x, y, GlobalVars.bulletSizeX, GlobalVars.bulletSizeY);
    }
}
