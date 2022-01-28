/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Do wyświetlania grafiki pomocniczej w trakcie gry.
 * Do rozbudowy.
 * @author Asx
 */
public class HUD {
    
    private final Font font;
    
    private int playerHP;
    private int score;
    private String strScore;
    private int scorePosX;
    private Color scoreColor;
    
    public HUD()
    {
        playerHP = GlobalVars.playerHP;
        score = 0;
        strScore = "0";
        scorePosX = GlobalVars.gameWidth - 16;
        
        font = new Font("arial", Font.PLAIN, 16);
        scoreColor = Color.CYAN;
    }
    
    private void prepScore()
    {
        strScore = Integer.toString(score);
        int n = strScore.length();
        scorePosX = GlobalVars.gameWidth - (5 + 9*n);
        if(score>=0)
            scoreColor = Color.CYAN;
        else
            scoreColor = Color.RED;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public void setScore(int s)
    {
        score = s;
        prepScore();
    }
    
    public void incScore(int value)
    {
        score += value;
        prepScore();
    }
    
    public void decScore(int value)
    {
        score -= value;
        prepScore();
    }
    
    /**
     * Aktualizacja HUD.
     * Obecnie wykorzystujemy tylko dane (HP) "statku" gracza.
     * @param p
     */
    public void updateHP(Player p)
    {
        playerHP = p.getHP();
    }
    
    public int getHP()
    {
        return playerHP;
    }
    
    public void setHP(int _hp)
    {
        playerHP = _hp;
    }
    
    public void tick()
    {
        
    }
    
    public void render(Graphics g)
    {
        g.setColor(Color.GREEN);
        for(int i=0; i<playerHP; i++)
        {
            g.fillRect(15+25*i, 15, 16, 16);
        }
        
        g.setColor(scoreColor);
        g.setFont(font);
        g.drawString(strScore, scorePosX, 24);
        
        g.setColor(Color.GRAY);
        g.drawLine(0, 40, GlobalVars.gameWidth, 40);
    }
}
