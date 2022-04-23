/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Klasa obsługująca klawiaturę.
 * Wykorzystuje ja do sterowania "statkiem" gracza.
 * @author Asx
 */
public class KeyInput extends KeyAdapter{
    
    private final GameObject handle;
    private final boolean[] keyBuffer = {false, false};
    
    /**
     * Konstruktor
     * @param handle Obiekt gracza (statek),
     * którym będzie sterować za pomocą klawiatury
     */
    public KeyInput(GameObject handle)
    {
        this.handle = handle;
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        
        if((key == KeyEvent.VK_A) || (key == KeyEvent.VK_LEFT))
        {
            handle.setSpeedX(-GlobalVars.playerSpeed);
            keyBuffer[0] = true;
        }
        else
            if((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT))
            {
                handle.setSpeedX(GlobalVars.playerSpeed);
                keyBuffer[1] = true;
            }
        if((key == KeyEvent.VK_W) || (key == KeyEvent.VK_UP))
            ((Player)handle).shoot();
        
        //esc ->exit
        //Trzeba to zmienić na przejście do Pauzy
        if(key == KeyEvent.VK_ESCAPE) ((Player) handle).requestPause();
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        
        if((key == KeyEvent.VK_A) || (key == KeyEvent.VK_LEFT))
            //handle.setSpeedX(0);
            keyBuffer[0] = false;
        else
            if((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT))
            //handle.setSpeedX(0);
            keyBuffer[1] = false;
        if(!keyBuffer[0] && !keyBuffer[1])
            handle.setSpeedX(0);
    }
}
