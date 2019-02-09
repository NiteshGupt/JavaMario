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
 * @author lapcom
 */
public class Player{
    int x;
    int y;
    int s;
    int ci;
    int delay;
    int jump;
    boolean jumping;
    boolean onGround;
    boolean dead;
    int gid;
    int imagebound;
    int gravity;
    
    Loader[][] pImg;
    Loader pDead;
    DrawImage drawImg;
    public Player(int n,int x,int y,String[][] path,String path2){
         pImg=new Loader[2][n];
        this.x=x;
        this.y=y;
        this.s=0;
        this.ci=0;
        this.delay=2;
        this.jumping=false;
        this.onGround=false;
        this.dead=false;
        this.jump=18;
        this.gid=-1;
        this.gravity=4;
        this.imagebound=-1;
        for(int j=0;j<2;j++)
        {
            for(int i=0;i<n;i++){
                if(path[j][i]==null)continue;
                pImg[j][i]=new Loader(path[j][i]);
                
            }
        }
        pDead=new Loader(path2);
    }
    public Player(int n,int x,int y,int s,int ci,String[][] path){
        this.x=x;
        this.y=y;
        this.s=s;
        this.ci=ci;
        this.imagebound=-1;
        this.delay=2;
        this.dead=false;
        for(int j=0;j<2;j++)
        {
            for(int i=0;i<n;i++){
                if(path[j][i]==null)continue;
                pImg[j][i]=new Loader(path[j][i]);
                
            }
        }
    }
    public void drawPlayer(Graphics g,int x,int y,int width,int height){
        if(pImg[s][ci].image!=null){
            this.x=x;
            this.y=y;
            if(!this.dead){
            drawImg=new DrawImage(pImg[s][ci].image,x,y,width,height,true);
            
            }
            else{
                drawImg=new DrawImage(pDead.image,x,y,width,height,true);

            }
            drawImg.draw(g);
        }
        
    }
    public Rectangle getBoundTop(){
            return new Rectangle((int)this.x+5,(int)this.y+3,(int)this.pImg[s][ci].image.getWidth()-10,(int)-1.5+this.pImg[s][ci].image.getHeight()/3);
    }
    public Rectangle getBoundBottom(){
            return new Rectangle((int)this.x+5,(int)this.y+3+this.pImg[s][ci].image.getHeight()/2,(int)this.pImg[s][ci].image.getWidth()-10,(int)this.pImg[s][ci].image.getHeight()/2);
    }
    public Rectangle getBoundLeft(){
            return new Rectangle((int)this.x-1,(int)this.y+7,5,(int)this.pImg[s][ci].image.getHeight()-10);
    }
    public Rectangle getBoundRight(){
            return new Rectangle((int)this.x+this.pImg[s][ci].image.getWidth()-5,(int)this.y+7,6,(int)this.pImg[s][ci].image.getHeight()-10);
    }
}