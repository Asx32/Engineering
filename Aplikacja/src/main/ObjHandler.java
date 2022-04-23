/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 * Klasa do przechowywania i obsługi obiektów w grze (wrogów i pocisków)
 * @author Asx
 */
public class ObjHandler {
    private LinkedList<GameObject> objects;
    
    public ObjHandler()
    {
        objects = new LinkedList<GameObject>();
    }
    
    public void add(GameObject o)
    {
        objects.add(o);
        //System.out.println(this.objects.size());
    }
    
    /**
     * Czyści listę obiektów, ale tylko z elementów oznaczonych jako "martwe"
     */
    public void clearDead()
    {
        int i = objects.size()-1 ;
        
        while(i >= 0)
        {
            if(objects.get(i).isAlive() == false) objects.remove(i);
            i--;
        }
    }
    
    /**
     * Czyszczenie listy na potrzeby ponownego rozpoczęcia gry.
     */
    public void reset()
    {
        objects.clear();
    }
    
    public GameObject get(int i)
    {
        return objects.get(i);
    }
    
    public int getSize()
    {
        return objects.size();
    }
    
    public void tick()
    {
        for(GameObject o : objects) o.tick();
    }
    
    public void render(Graphics g)
    {
        for(GameObject o : objects) o.render(g);
    }
}
