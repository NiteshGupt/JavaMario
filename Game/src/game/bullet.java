/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author lapcom
 */
public class bullet {
    private int x;
    private int y;
    private int size;
    private Color col;
    bullet(int x,int y,int size,Color col){
        this.x=x;
        this.y=y;
        this.size=size;
        this.col=col;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getSize(){
        return this.size;
    }
    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public void draw(Graphics g){
        g.setColor(col);
        g.fillOval(x, y, size+10, size);
    }
    public Rectangle getBound(){
        return new Rectangle(x,y,size+15,size+5);
    }
}
