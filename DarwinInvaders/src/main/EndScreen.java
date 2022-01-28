/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Asx
 */
public class EndScreen extends MouseAdapter
{
    private int mx, my;
    private BasicButton btnExit;
    private String label;
    private String score;
    private boolean prepared;
    
    private HUD hud;
    private GameController game;
    
    private Font endFont;
    private Color endColor;
    
    public EndScreen(GameController _gc, HUD _hud)
    {
        this.hud = _hud;
        this.game = _gc;
        this.label = "You died! Sorry.";
        score = "";
        prepared = false;
        
        btnExit = new BasicButton("Exit", GUIConsts.menuX , GUIConsts.navY);
        endFont = new Font("arial", Font.PLAIN, 24);
        endColor = Color.GRAY;
    }
    
    public void prepScore()
    {
        if(!prepared)
        {
            prepared = true;
            score = "Your score: " + Integer.toString(hud.getScore());
        }
    }
    
    private boolean mouseOver()
    {
        if((mx > btnExit.getX()) && (mx < btnExit.getX() + btnExit.getWidth()))
        {
            int y1 = btnExit.getY();
            int y2 = y1 + btnExit.getHeight();
            return (my > y1) && (my < y2);
        }
        return false;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        
        if(mouseOver() && (game.getState() == GameState.DEAD))
            game.setState(GameState.MENU);
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    public void tick()
    {
        //to raczej nie będzie używane...
    }
    
    public void render(Graphics g)
    {
        g.setFont(endFont);
        g.setColor(endColor);
        
        g.drawString(label, GUIConsts.menuX, GUIConsts.menuY);
        g.drawString(score, GUIConsts.menuX, GUIConsts.menuY+32);
        
        g.setFont(btnExit.getFont());
        g.setColor(btnExit.getColor());
        g.drawRect(btnExit.getX(), btnExit.getY(), btnExit.getWidth(), btnExit.getHeight());
        g.drawString(btnExit.getText(), btnExit.getTextX(), btnExit.getTextY());
    }
}
