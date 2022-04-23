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
 * Klasa reprezentująca obiekt (statek?) sterowany przez gracza
 * @author Asx
 */
public class Player extends GameObject{

    private final ObjHandler projectiles;
    private int gunCooldown;
    private int hp;
    
    private boolean pause;
    
    /**
     *
     * @param _x Początkowa pozycja w poziomie
     * @param _y Początkowa pozycja w pionie
     * @param handler Uchwyt do kontenera przechowującego "pociski" wystrzelone przez "statek" gracza
     */
    public Player(int _x, int _y, ObjHandler handler) {
        super(_x, _y);
        projectiles = handler;
        gunCooldown = 0;
        hp = GlobalVars.playerHP;
        
        pause = false;
    }
    
    /**
     * Reset pól klasy na potrzeby ponownego rozpoczęcia gry (w ramach jednego uruchomienia programu)
     */
    public void reset()
    {
        gunCooldown = 0;
        hp = GlobalVars.playerHP;
        this.alive = true;
        this.x = GlobalVars.playerPosX;
    }
    
    /**
     * Tworzy nowy pocisk i umieszcza go w kontenerze
     */
    public void shoot()
    {
        if(gunCooldown >= GlobalVars.cooldownPlayerWeapon)
        {
            //utwórz nowy pocisk i dodaj do projectiles...
            projectiles.add(new Bullet(x+((GlobalVars.playerSizeX/2)-(GlobalVars.bulletSizeX/2)), y+GlobalVars.bulletSizeY-1, -GlobalVars.bulletSpeed, false));
            gunCooldown = 0;
        }
            
    }
    
    /**
     * Tworzy i zwraca "hitbox" w postaci prostokątu
     * @return Prostokąt reprezentujacy hitbox "statku"
     */
    @Override
    public Rectangle getHitbox()
    {
        return new Rectangle(x, y, GlobalVars.playerSizeX, GlobalVars.playerSizeY);
    }
    
    /**
     *
     * @return Liczba pozostałych "punktów życia" statku gracza
     */
    public int getHP()
    {
        return hp;
    }
    
    /**
     *
     * @param hp Nowa liczba "punktów życia" statku gracza
     */
    public void setHP(int hp)
    {
        this.hp = hp;
    }
    
    @Override
    public void tick()
    {
        x += speedX; //y pomijam, bo nie będzie ruchu gracza w pionie...
        
        //"ramka" gry
        if(x <0) x = 0;
        else
            if(x > GlobalVars.gameWidth - GlobalVars.playerSizeX -16) x = GlobalVars.gameWidth - GlobalVars.playerSizeX -16;
        
        if(gunCooldown < GlobalVars.cooldownPlayerWeapon) gunCooldown++;
        
        //kolizje?...
    }
    
    @Override
    public void render(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(x, y, GlobalVars.playerSizeX, GlobalVars.playerSizeY);
    }
    
    /**
     * Funkcja aktualizująca "punkty życia" po kolizji (zmniejsza je o 1)
     * oraz status obiektu (żywy/martwy) na potrzeby sprawdzenia warunku zakończenia gry.
     */
    @Override
    public void kill()
    {
        hp--;
        if(hp <= 0) alive = false;
    }
    
    /**
     * Żądanie pauzy.
     * Funkcja pośredniczy pomiędzy klawiaturą (klasa KeyInput) a głównym obiektem gry (GameController).
     * 
     */
    public void requestPause()
    {
        pause = true;
    }
    
    /**
     * Metoda dostępowa na potrzeby sprawdzenia czy podczas rozgrywki zażądano pauzy.
     * Podczas wywołania zeruje "żądanie".
     * @return Czy zażądano pauzy/przycisk Esc zistał naciśnięty podczas rozgrywki?
     */
    public boolean pauseRequested()
    {
        boolean ans = pause;
        if(pause) pause = false;
        return ans;
    }
}
