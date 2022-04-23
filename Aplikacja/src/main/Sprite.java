/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Asx
 */
public class Sprite {
    private BufferedImage sheet;
    private int cell_w;
    private int cell_h;
    
    public Sprite(String imgpath, int width, int height)
    {
        cell_w = width;
        cell_h = height;
        try {
            sheet = ImageIO.read(getClass().getResource(imgpath));
        } catch (IOException ex) {
            Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getImage(int no)
    {
        BufferedImage img = sheet.getSubimage(no*cell_w, 0, cell_w, cell_h);
        return img;
    }
}
