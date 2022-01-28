/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
/**
 * Klasa do obsługi róznych typów menu
 * @author Asx
 */
public class Menu extends MouseAdapter
{
    private int mx, my;
    private GameController game;
    private ArrayList<BasicButton> buttons;
    private MenuType type;
    
    private BufferedImage logo;
    private int lx, ly;
    
    public static enum MenuType
    {
        MAIN, PAUSE, OPTIONS, END;
    }
    
    /**
     *
     * @param _gc Główny obiekt gry
     * @param _type Typ menu (główne lub pauzy)
     */
    public Menu(GameController _gc, MenuType _type)   //arg: GameController, typ menu?
    {
        mx = 0;
        my = 0;
        game = _gc;
        type = _type;
        
        buttons = new ArrayList<BasicButton>();
        
        if(type == MenuType.MAIN)
        {
            try {
                logo = ImageIO.read(getClass().getResource("/res/DI_main_logo.png"));
            } catch (IOException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] caption = {"Play", "Load", "Options", "Exit"};
            int n = caption.length;
            for(int i=0; i<n; i++)
                buttons.add(new BasicButton(caption[i], GUIConsts.menuX , GUIConsts.menuY + i*(GUIConsts.buttonHeight+GUIConsts.vOffset)));
        }
        else
            if(type == MenuType.PAUSE)
            {
                try {
                logo = ImageIO.read(getClass().getResource("/res/DI_pause_logo.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }
                String[] caption = {"Resume", "Save", "Quit"};
                int n = caption.length;
                for(int i=0; i<n; i++)
                    buttons.add(new BasicButton(caption[i], GUIConsts.menuX , GUIConsts.menuY + i*(GUIConsts.buttonHeight+GUIConsts.vOffset)));
            }
        else
            if(type == MenuType.END)
            {
                //przycisk powrotu na dole okna
                buttons.add(new BasicButton("Exit", GUIConsts.menuX , GUIConsts.navY));
            }
            /*
        else
            if(type == MenuType.OPTIONS)
            {
                //to będzie bardziej skomplikowane...
            }
            */
        lx = (GlobalVars.gameWidth - logo.getWidth())/2-8;
        ly = (GUIConsts.menuY - logo.getHeight())/2;
    }
    
    private boolean mouseOver(int btnNum)//arg? Numer przycisku?
    {
        BasicButton btn = buttons.get(btnNum);
        
        if((mx > btn.getX()) && (mx < btn.getX() + btn.getWidth()))
        {
            int y1 = btn.getY();
            int y2 = y1 + btn.getHeight();
            if((my > y1) && (my < y2)) return true;
            return false;
        }
                
        return false;
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        
        if((game.getState() == GameState.MENU) && (this.type == MenuType.MAIN))
        {
            if(mouseOver(0))
            {
                game.setState(GameState.GAME);
                game.reset();
            }
            else
            if(mouseOver(1))
            {
                //System.out.println("Menu->Ładuj");   //ładowanie
                //game.setState(GameState.LOAD);
                game.loadGame();
                game.setState(GameState.GAME);
            }
            else
            if(mouseOver(2))
            {
                //System.out.println("Menu->Opcje");   //opcje
                game.setState(GameState.OPTIONS);
            }
            else
            if(mouseOver(3))
                game.setState(GameState.END);
                //System.exit(0);
        }
        
        if((game.getState() == GameState.PAUSE) && (this.type == MenuType.PAUSE))
        {
            if(mouseOver(0))
                game.setState(GameState.GAME);
            else
            if(mouseOver(1))
            {
                //System.out.println("Game save");   //zapis stanu gry - w tym opcje
                game.saveGame();
                game.setState(GameState.GAME);
            }
            else
            if(mouseOver(2))
                game.setState(GameState.MENU);
        }
        //do wykorzystania gdzie indziej...
        if((game.getState() == GameState.DEAD) && (this.type == MenuType.END))
        {
            if(mouseOver(0))
                game.setState(GameState.MENU);
        }
        
        /*
        if((game.getState() == GameState.OPTIONS) && (this.type == MenuType.OPTIONS))
        {
            //to będzie skomplikowane...
            //if(mouseOver(n))    game.setState(GameState.MENU);
        }
        */
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        
    }
    
    public void tick()
    {
        //tu animacje menu?
    }
    
    public void render(Graphics g)
    {
        g.drawImage(logo, lx, ly, null);
        int n = buttons.size();
        for(int i=0; i<n; i++)
        {
            BasicButton btn = buttons.get(i);
            g.setFont(btn.getFont());
            g.setColor(btn.getColor());
            g.drawRect(btn.getX(), btn.getY(), btn.getWidth(), btn.getHeight());
            g.drawString(btn.getText(), btn.getTextX(), btn.getTextY());
        }
    }
   
}
