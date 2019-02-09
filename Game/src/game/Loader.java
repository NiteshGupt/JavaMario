/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author lapcom
 */
public class Loader {
    BufferedImage image;
    public Loader(String path){
        
        try {
            
            image=ImageIO.read(new File(path));
            System.out.println(path+" LOADED.");
        } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}