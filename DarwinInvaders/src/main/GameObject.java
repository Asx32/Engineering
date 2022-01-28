/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Klasa bazowa dla interaktywnych obiektów w grze
 * @author Asx
 */
public abstract class GameObject {
    
    protected int x;
    protected int y;
    protected int speedX;
    protected int speedY;
    protected boolean alive;
    
    /**
     *
     * @param _x Pozycja w poziomie
     * @param _y Pozycja w pionie
     */
    public GameObject(int _x, int _y)
    {
        this.x = _x;
        this.y = _y;
        
        this.speedX = 0;
        this.speedY = 0;
        this.alive = true;
        
        //add to handler? -> to it in children
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getHitbox();
    
    public void setX(int _x)
    {
        x = _x;
    }
    
    public int getX()
    {
        return x;
    }
    
    public void setY(int _y)
    {
        y = _y;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setSpeedX(int sx)
    {
        this.speedX = sx;
    }
    
    public int getSpeedX()
    {
        return this.speedX;
    }
    
    public void setSpeedY(int sy)
    {
        this.speedY = sy;
    }
    
    public int getSpeedY()
    {
        return this.speedY;
    }
    
    public boolean isAlive()
    {
        return alive;
    }
    
    public void kill()
    {
        alive = false;
    }
}
