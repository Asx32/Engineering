/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;

/**
 * Klasa reprezentujaca przycisk menu
 * @author Asx
 */
public class BasicButton {
    protected final String text;
    protected final int x, y, w, h;
    
    protected final Color color;
    protected final Font font;
    
    /**
     * Pełny konstruktor
     * @param text Etylieta przycisku
     * @param x Pozycja w poziomie
     * @param y Pozycja w pionie
     * @param width Szerokość
     * @param height Wysokość
     * @param color Kolor przycisku (ramki?) i tekstu
     * @param font Font tekstu
     */
    public BasicButton(String text, int x, int y, int width, int height, Color color, Font font)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.color = color;
        this.font = font;
    }
    
    /**
     * Skrócony konstruktor.
     * Dane nieprzekazane w argumentach zostają zainicjowane wartościami domyślnymi
     * @param text
     * @param x
     * @param y
     */
    public BasicButton(String text, int x, int y)
    {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = GUIConsts.buttonWidth;
        this.h = GUIConsts.buttonHeight;
        this.color = Color.WHITE;
        this.font = new Font("arial", Font.PLAIN, 24);
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public int getWidth()
    {
        return w;
    }
    
    public int getHeight()
    {
        return h;
    }
    
    public String getText()
    {
        return text;
    }
    
    public int getTextX()
    {
        return x + 8;   //do poprawy!!! ...ale jeszcze nie wiem jak wyśrodkować tekst...
    }
    
    public int getTextY()
    {
        return y + h/2;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public Font getFont()
    {
        return font;
    }
}
