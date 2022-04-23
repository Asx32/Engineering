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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asx
 */
public class OptionsMenu extends MouseAdapter
{
    
    private int mx, my;
    private GameController game;
    private OptionsManager optMan;
    private ArrayList<BasicButton> decButtons;
    private ArrayList<BasicButton> incButtons;
    private ArrayList<BasicButton> navButtons;
    private int[] yPositions;
    private int[] xPositions;
    private String[] names;
    private String[] values;
    
    private Font modFont;
    private Color modColor;
    
    private boolean updValues;
    
    public OptionsMenu(GameController _gc, OptionsManager _om)
    {
        mx = 0;
        my = 0;
        game = _gc;
        optMan = _om;
        optMan.loadValues();
        
        modFont = new Font("arial", Font.PLAIN, 12);
        modColor = Color.GRAY;
        updValues = false;
        
        decButtons = new ArrayList<BasicButton>();
        incButtons = new ArrayList<BasicButton>();
        navButtons = new ArrayList<BasicButton>();
        int n = optMan.getSize();
        
        yPositions = new int[n];
        xPositions = new int[n];
        values = new String[n];
        String[] tmp = {"Liczba wrogów w fali", "Rozmiar genomu", "Pdp krzyżowania","Pdp mutacji","Liczba fal do krzyżowania"};
        names = tmp;
        
        for(int i=0; i<n; i++)
        {
            yPositions[i] = GUIConsts.menuY + i*(GUIConsts.modBtnSize+GUIConsts.vOffset);
            decButtons.add(new BasicButton("<", GUIConsts.menuX , yPositions[i], GUIConsts.modBtnSize, GUIConsts.modBtnSize, modColor, modFont));
            incButtons.add(new BasicButton(">", GUIConsts.menuX +GUIConsts.hModOffset , yPositions[i], GUIConsts.modBtnSize, GUIConsts.modBtnSize, modColor, modFont));
        }
        //navButtons
        navButtons.add(new BasicButton("Save", GUIConsts.menuX - GUIConsts.hOffset, GUIConsts.navY));
        navButtons.add(new BasicButton("Back", GUIConsts.menuX + GUIConsts.hOffset, GUIConsts.navY));
        
        valueUpdate();
    }
    
    private void valueUpdate()
    {
        int n = optMan.getSize();
        for(int i=0; i<n; i++)
        {
            values[i] = Integer.toString(optMan.getValue(i));
            xPositions[i] = GUIConsts.menuX + (GUIConsts.hModOffset - (values[i].length()*7))/2;
        }
    }
    
    private boolean mouseOver(int btnNum)
    {
        BasicButton btn = null;
        
        if(btnNum < optMan.getSize())
        {
            btn = decButtons.get(btnNum);
        }
        else
            if(btnNum < 2*optMan.getSize())
            {
                btn = incButtons.get(btnNum-optMan.getSize());
            }
            else
                {
                    btn = navButtons.get(btnNum- 2*optMan.getSize());
                }
        
        if(btn != null)
        {
            if((mx > btn.getX()) && (mx < btn.getX() + btn.getWidth()))
            {
                int y1 = btn.getY();
                int y2 = y1 + btn.getHeight();
                return (my > y1) && (my < y2);
            }
        }
        return false;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        //System.out.println("mx: "+mx+ ", my: " +my);
        int n = decButtons.size() + incButtons.size() + navButtons.size();
        int oSize = optMan.getSize();
        
        int i = 0;
        
        while((i<n) && !mouseOver(i))
            i += 1;
        
        if(i < oSize)
        {
            optMan.decValue(i);
            //System.out.println("Dec: "+i);
            updValues = true;
        }
        else
        {
            i -= oSize;
            if(i < oSize)
            {
                optMan.incValue(i);
                //System.out.println("Inc: "+i);
                updValues = true;
            }
            else
            {
                i -= oSize;
                switch(i)
                {
                    case 0: //zapisz ustawione wartości
                        optMan.saveValues();
                        //System.out.println("Save options");
                        break;
                    case 1: //wróć do menu głównego
                        //System.out.println("Back");
                        game.setState(GameState.MENU);
                        break;
                }
            }
        }
            
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    public void tick()
    {
        //tu animacje menu?
        if(updValues)
        {
            this.valueUpdate();
        }
    }
    
    public void render(Graphics g)
    {
        int n = optMan.getSize();
        for(int i=0; i<n; i++)
        {
            g.setFont(modFont);
            g.setColor(modColor);
            
            BasicButton btn = decButtons.get(i);
            g.drawRect(btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight());
            g.drawString(btn.getText(), btn.getTextX(), btn.getTextY());
        
            btn = incButtons.get(i);
            g.drawRect(btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight());
            g.drawString(btn.getText(), btn.getTextX(), btn.getTextY());
            
            g.drawString(names[i], 8, yPositions[i]+12);
            g.drawString(values[i], xPositions[i], yPositions[i]+12);
        }
        
        //navButtons
        n = navButtons.size();
        for(int i=0; i<n; i++)
        {
            BasicButton btn = navButtons.get(i);
            g.setFont(btn.getFont());
            g.setColor(btn.getColor());
            g.drawRect(btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight());
            g.drawString(btn.getText(), btn.getTextX(), btn.getTextY());
        }
    }
}
