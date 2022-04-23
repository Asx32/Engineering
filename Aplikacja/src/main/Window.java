/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Klasa reprezentująca okno programu
 * @author Asx
 */
public class Window extends Canvas{
    
    /**
     * Konstruktor
     * @param width Szerokość okna
     * @param height Wysokość okna
     * @param title Tytuł
     * @param game Kontroler gry/główny obiekt gry
     */
    public Window(int width, int height, String title, GameController game)
    {
        JFrame frame = new JFrame(title);
        
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);
        game.start();
    }
}
