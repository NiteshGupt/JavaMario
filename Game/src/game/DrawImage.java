/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Nitesh
 */
public class DrawImage {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final BufferedImage img;
    public boolean block;
    public DrawImage(BufferedImage img,int x,int y,boolean block){
        this.img=img;
        this.x=x;
        this.y=y;
        this.width=img.getWidth();
        this.height=img.getHeight();
        this.block=block;
    }
    public DrawImage(BufferedImage img,int x,int y,int width,int height,boolean block){
        this.img=img;
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.block=block;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    protected void draw(Graphics g){
        g.drawImage(img,x,y,width,height,null);
    }
    protected Rectangle getBound(){
        return new Rectangle((int)x,(int)y,(int)width,(int)height);
    }
}
