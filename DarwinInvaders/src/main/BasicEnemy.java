/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Asx
 */
public class BasicEnemy extends GameObject {
    
    private ObjHandler projectiles;
    private int id;
    private int[] parents;
    private int value;
    private int cycleCooldown;
    private int behaviourStep;
    private LinkedList<Integer> behaviour;
    
    private Sprite sprite;
    private BufferedImage model;
    
    private Random r;
    
    public BasicEnemy(int _id, int _x, int _y, ObjHandler handler)
    {
        super(_x, _y);
        this.id = _id;
        this.projectiles = handler;
        value = 10;
        cycleCooldown = 0;
        behaviourStep = 0;
        
        sprite = new Sprite("/res/Sprite_Inv01.png", GlobalVars.enemySizeX, GlobalVars.enemySizeY);
        model = sprite.getImage(0);
        
        r = new Random();
        
        behaviour = new LinkedList<Integer>();
        this.parents = new int[2];
        parents[0] = parents[1] = -1;   //=brak rodziców
        // To już robimy gdzie indziej...
        //for(int i=0; i<GlobalVars.behaviourSize; i++) behaviour.add(r.nextInt(3));
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public void setId(int _id)
    {
        this.id = _id;
    }
    
    public int[] getParents()
    {
        return this.parents;
    }
    
    public void setParents(int p1, int p2)
    {
        this.parents[0] = p1;
        this.parents[1] = p2;
    }
    
    public void setParents(int[] p)
    {
        if(p.length >= 2)
        {
            this.parents[0] = p[0];
            this.parents[1] = p[1];
        }
    }
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int _value)
    {
        this.value = _value;
    }
    
    public LinkedList<Integer> copyBehaviour()
    {
        LinkedList<Integer> list = new LinkedList<Integer>();
        int n = behaviour.size();
        for(int i=0; i<n; i++)
        {
            list.add(behaviour.get(i));
        }
        return list;
    }
    
    public LinkedList<Integer> getBehaviour()
    {
        return this.behaviour;
    }
    
    public void setBehaviour(LinkedList<Integer> list)
    {
        behaviour.clear();
        int n = list.size();
        for(int i=0; i<n; i++)
        {
            behaviour.add(list.get(i));
        }
    }
    
    public void shoot()
    {
        projectiles.add(new Bullet(x+((GlobalVars.enemySizeX/2)-(GlobalVars.bulletSizeX/2)), y+GlobalVars.bulletSizeY-1, GlobalVars.bulletSpeed, true));
    }
    
    @Override
    public Rectangle getHitbox()
    {
        return new Rectangle(x, y, GlobalVars.enemySizeX, GlobalVars.enemySizeY);
    }

    @Override
    public void tick() 
    {
        if(cycleCooldown < GlobalVars.cooldownEnemyCycle)
            cycleCooldown++;
        else
        {
            cycleCooldown = 0;
            //zmień zachowanie - następny krok
            behaviourStep++;
            if(behaviourStep >= GlobalVars.behaviourSize) behaviourStep = 0;
            int move = behaviour.get(behaviourStep);
            model = sprite.getImage(move);
            if(move == 0)
            {
                this.speedX = GlobalVars.enemySpeed;
                this.speedY = 0;
            }
            else
                if(move == 1)
                {
                    this.speedX = -GlobalVars.enemySpeed;
                    this.speedY = 0;
                }
                else
                    {
                        this.speedY = GlobalVars.enemySpeed;
                        this.speedX = 0;
                        value += 10;    //Zwiększaj wartość tylko podczas ruchu w pionie
                    }
        }
        //ruch - ogranicz w poziomie; jeśli dotrze do dołu ekranu - ...
        x += speedX;
        y += speedY;
        
        if(x <0) x = 0;
        else
            if(x > GlobalVars.gameWidth - GlobalVars.enemySizeX -16)
                x = GlobalVars.gameWidth - GlobalVars.enemySizeX -16;
        //if(y > GlobalVars.gameHeight-44) this.kill();
    }

    @Override
    public void render(Graphics g) 
    {
       //g.setColor(Color.MAGENTA);
       //g.fillRect(x, y, GlobalVars.enemySizeX, GlobalVars.enemySizeY);
       g.drawImage(model, x, y, null);
    }
    
}
