/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComponent;
import javax.swing.Timer;
import sun.applet.Main;


/**
 *
 * @author Nitesh
 */
public class Game extends Canvas implements ActionListener, KeyListener, Runnable {

    private boolean running=false;
    private boolean idle=true;
    private static int xPos=200;
    private static int yPos=100;
    private Thread tt;
    private BufferStrategy bs;
    private Graphics g;
    private Window win;
    private final int MAX_ENEMYS=7;
    private int xx=0,ll=pixelx;
    private int ePos[]=new int[MAX_ENEMYS];
    public synchronized void start(){
        if(running)return;
        running=true;
        tt =new Thread(game);
        tt.start();
    }
    public synchronized void stop(){
        if(!running)return;
        running=true;
        try {
            tt.join();;
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
   
    @Override
    public void run() {
        init();
        System.out.println("Id:"+tt.getId());
        long lasttime=System.nanoTime();
        long timer=System.currentTimeMillis();
        final double fps=1000000000.0/60.0;
        double delta=0;
        int frames=0;
        int updates=0;
        while(running){
            long now=System.nanoTime();
            delta=(now-lasttime)/fps;
            lasttime=now;
            if(delta>=1){
                tick();
                updates++;
                delta--;
                try {
                    Thread.sleep(8);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            frames++;
            render();
            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
                System.out.println(frames);
                updates=0;
                frames=0;
            }
            
        }
        game.stop();

    }
    private String nad;
    private String naE[][]=new String[2][2];
    private void init(){
        meStr[0][0]="run1.gif";
        meStr[0][1]="run3.gif";
        meStr[0][2]="run2.gif";
        meStr[0][3]="idle.gif";
        meStr[0][4]="jump.gif";
        
        meStr[1][0]="runback1.gif";
        meStr[1][1]="runback3.gif";
        meStr[1][2]="runback2.gif";
        meStr[1][3]="idleback.gif";
        meStr[1][4]="jumpback.gif";
        
        naE[0][0]="e1.png";
        naE[0][1]="e2.png";
        naE[1][0]="e1b.png";
        naE[1][1]="e2b.png";
        nad="ed.png";
        
        player= new Player(5,xPos,yPos,meStr,"pd.png");
        
        ePos[0]=2560;
        ePos[1]=3328;
        ePos[2]=3936;
        ePos[3]=4832;
        ePos[4]=5120;
        ePos[5]=5664;
        ePos[6]=5984;
        
        for(int i=0;i<MAX_ENEMYS;i++)
        if(naEnemy[i]==null)naEnemy[i]=new Player(2,ePos[i],0,naE,nad);

        blockImg[0]=new Loader("dd.jpg");
        blockImg[1]=new Loader("dl.jpg");
        blockImg[2]=new Loader("c.png");
        blockImg[3]=new Loader("c2.png");
        blockImg[4]=new Loader("bg.png");

        level=new Loader("levell.png");
        
        bg=new Loader("bge.jpg");
        win = new Window("Animation",game); 

        //Timer tt=new Timer(20,game);
        //tt.start();
        
        
    }
    private void tick(){
        animRun();
    }
    private void render(){
        bs=getBufferStrategy();
        if(bs==null){
            createBufferStrategy(3);
            return;
        }
        bs.show();
        g=bs.getDrawGraphics();
       
        paintComponent(g);
    }
    private static final int pixelx=216;
    private static final int pixely=24;
    private int velX=0;
    private int velY=0;
    private boolean move=false;
    private int camX=0;
    private boolean shooting=false;
    private final static int MAX_BULLETS=5;
    private int[] velBX=new int[MAX_BULLETS];
    private int camMov=30;
    private boolean camChange=false;
    private int counts=0;
    private int shootdelay=8;
    private int col=-1;
    
    private void animRun(){
        
        if(player.dead)player.ci=3;
        else{
            if(idle==true && player.onGround==true&&player.jumping==false){
                if(player.ci!=3){
                    player.ci=3;

                }
            }
            else if(player.jumping==true||player.onGround==false){
                if(player.ci!=4)player.ci=4;
            }
            else{
                if(player.delay>0)player.delay--;
                else{

                    if(player.ci<=0){
                        player.imagebound=player.ci;
                        player.ci++;
                    }
                    else if(player.ci>=2){
                        player.imagebound=player.ci;
                        player.ci--;
                    }
                    else if(player.ci>0&&player.ci<2){
                        if(player.imagebound<=0)player.ci++;
                        else if(player.imagebound>=2)player.ci--;
                    }

                    player.delay=2;
                }

            }
        }
        
        //int pos=(xPos+xPos+player.pImg[side][player.ci].image.getWidth())/2;
        for(int i=1;i<pixelx*pixely+1;i++){
            if(di[i]==null)continue;
            if(di[i].block==false)continue;
            if(!player.dead){
                if(player.getBoundBottom().intersects(di[i].getBound())){
                    //if(player.jumping==true)player.jumping=false;
                    player.gid=i;

                }
                else if(player.getBoundTop().intersects(di[i].getBound())){
                    player.jump=18;
                    player.jumping=false;
                    player.onGround=false;
                }

                if(player.getBoundLeft().intersects(di[i].getBound())&&player.s==1||player.getBoundRight().intersects(di[i].getBound())&&player.s==0){
                    col=i;
                }
            }
            if(naEnemy!=null){
                for(int e=0;e<MAX_ENEMYS;e++){
                    if(naEnemy[e]!=null){
                        //Enemy animation
                        if(!(naEnemy[e].x>xx*32&&naEnemy[e].x<ll*32))continue;

                        if(naEnemy[e].getBoundLeft().intersects(di[i].getBound())&&naEnemy[e].s==1){
                            naEnemy[e].s=0;
                        }
                        else if(naEnemy[e].getBoundRight().intersects(di[i].getBound())&&naEnemy[e].s==0){
                            naEnemy[e].s=1;
                        }

                        if(naEnemy[e].getBoundBottom().intersects(di[i].getBound())){
                            //if(player.jumping==true)player.jumping=false;
                            naEnemy[e].gid=i;

                        }



                    }
                }
            }
            /*
            if(pos>di[i].getX()&&pos<di[i].getX()+di[i].getWidth()){
                if(player.onGround==false){

                    if(yPos+player.gravity+player.pImg[player.s][player.ci].image.getHeight()<di[i].getY()){
                        yPos+=player.gravity;
                        player.gravity++;

                    }
                    else if(yPos+player.pImg[side][player.ci].image.getHeight()<di[i].getY()){
                        yPos=di[i].getY()-player.pImg[side][player.ci].image.getHeight();
                        player.gravity=0;
                        player.onGround=true;

                    }
                    else {
                        yPos+=player.gravity;
                        player.gravity++;
                    }

                }
                break;
            }
            if(i<totalground) continue;
            else{
                if(player.onGround==true)player.onGround=false;
                if(player.jumping==false){
                    yPos+=player.gravity;
                    player.gravity++;
                }

            } 
            */
        }
       
        if(player.gid!=-1){
            if(!player.getBoundBottom().intersects(di[player.gid].getBound())||player.dead){
                player.gid=-1;
                if(player.jumping!=true)player.onGround=false;
            }
            else{
                if(player.onGround==false)player.onGround=true;

                player.y=di[player.gid].getBound().y-player.pImg[player.s][player.ci].image.getHeight()+2;
            }

        }
        if(col!=-1){
            if(!player.getBoundLeft().intersects(di[col].getBound())&&player.s==1||!player.getBoundRight().intersects(di[col].getBound())&&player.s==0){
                col=-1;
                if(move!=false){
                    if(player.s ==0){
                        velX=12;
                    }
                    else{
                        velX=-12;
                    }
                }
            }
            else{
                velX=0;
                if(!player.getBoundTop().intersects(di[col].getBound())){
                    if(player.s==0){
                        player.x=di[col].getBound().x-player.pImg[player.s][player.ci].image.getWidth();
                    }
                    else player.x=di[col].getBound().x+di[col].getBound().width;
                }
            }
        }
        if(naEnemy!=null){
            for(int e=0;e<MAX_ENEMYS;e++){
                if(naEnemy[e]==null)continue;
                if(!(naEnemy[e].x>xx*32&&naEnemy[e].x<ll*32))continue;

                //Enemy ANimation
                if(naEnemy[e].dead)naEnemy[e].ci=1;
                else{
                    if(naEnemy[e].delay>0)naEnemy[e].delay--;
                    else{

                        if(naEnemy[e].ci<=0){
                            naEnemy[e].ci++;
                        }
                        else if(naEnemy[e].ci>=1){
                            naEnemy[e].ci--;
                        }


                        naEnemy[e].delay=4;
                    }
                }

                if(naEnemy[e].gid!=-1){
                    if(!naEnemy[e].getBoundBottom().intersects(di[naEnemy[e].gid].getBound())||naEnemy[e].dead){
                        naEnemy[e].gid=-1;
                        if(naEnemy[e].jumping!=true)naEnemy[e].onGround=false;
                    }
                    else{
                        if(naEnemy[e].onGround==false)naEnemy[e].onGround=true;

                        naEnemy[e].y=di[naEnemy[e].gid].getBound().y-naEnemy[e].pImg[naEnemy[e].s][naEnemy[e].ci].image.getHeight()+2;
                    }

                }
                if(!naEnemy[e].dead){
                    if(naEnemy[e].s==0) naEnemy[e].x+=5;
                    else naEnemy[e].x-=5;
                }
                if(naEnemy[e].onGround==false){
                    naEnemy[e].y+=naEnemy[e].gravity;
                    naEnemy[e].gravity++;
                }else{
                    if(naEnemy[e].gravity!=4)naEnemy[e].gravity=4;
                }

                //player-enemy collision
                if(naEnemy[e].getBoundLeft().intersects(player.getBoundRight())&&!naEnemy[e].dead){
                    if(!player.dead){
                        player.dead=true;
                        velX=0;
                        if(player.jumping)player.jumping=false;
                    }

                }
                else if(naEnemy[e].getBoundRight().intersects(player.getBoundLeft())&&!naEnemy[e].dead){
                    if(!player.dead){
                        player.dead=true;
                        velX=0;
                        if(player.jumping)player.jumping=false;
                    }
                }
                else if(naEnemy[e].getBoundTop().intersects(player.getBoundBottom())&&!player.dead){
                    naEnemy[e].dead=true;
                    
                }

                //bullet-enemy collision
                if(shooting){
                    for(int j=0;j<MAX_BULLETS;j++){
                        if(Bullet[j]==null)continue;
                        if(isColliding(Bullet[j],naEnemy[e])){
                            naEnemy[e].dead=true;
                            Bullet[j]=null;
                            counts--;
                            break;
                        }
                    }
                }
                /*if(isColliding(player,naEnemy[e])){
                    if(isTopCollide(player,naEnemy[e]))naEnemy[e].dead=true;
                    else player.dead=true;
                }*/
                if(naEnemy[e].y>750)naEnemy[e]=null;
            }
        }
        if(player.onGround==false){
            player.y+=player.gravity;
            player.gravity++;
        }else{
            if(player.gravity!=4)player.gravity=4;
        }
        if(player.jumping){
            
            player.y-=velY+player.jump;
            player.jump--;
            if(player.jump<0){
                if(!player.dead)player.jumping=false;
                player.onGround=false;
                player.jump=18;
            }
        }
        else player.y+=velY;
        if(player.y>750){
            player.gravity=4;
            respawn();
        }
        if(player.s==0){
            if(camChange==true){
                if(camX>200-player.x){
                    camX-=50;
                }
                else{
                    camX=200-player.x;
                    camChange=false;
                }
            }
            else
            camX=200-player.x;
        }
        else if(player.s==1){
            if(camChange==true){
                if(camX<game.getWidth()-300-player.x){
                    camX+=50;
                }
                else{
                    camX=game.getWidth()-300-player.x;
                    camChange=false;
                }
            }
            else
            camX=game.getWidth()-300-player.x;
           
        }
        
        if(camX>0)camX=0;
        if((pixelx*32)-game.getWidth()<-camX)camX=-(pixelx*32-game.getWidth());
        
        if(shooting=true){
            if(shootdelay>0)
            shootdelay--;
            for(int i=0;i<MAX_BULLETS;i++){
                if(Bullet[i]==null)continue;
                
                Bullet[i].setX(Bullet[i].getX()+velBX[i]);
                if(Bullet[i].getX()>game.getWidth()-camX||Bullet[i].getX()<-camX){
                    Bullet[i]=null;
                    counts--;
                }
                if(counts<0)shooting=false;
            }
        }
        if(!player.dead){
            if(player.x+velX>0&&player.x+player.pImg[player.s][player.ci].image.getWidth()+velX<pixelx*32)player.x+=velX;
        }
        else{
            if(!player.jumping){
                player.jump=18;
                player.jumping=true;
            }
        }
        
    }
    
    private final static int totalground=3;
    static Game game;
    static Loader level;
    static Loader[] blockImg=new Loader[5];
    static Loader bg;
    static Player player;
  
    static Player naEnemy[]=new Player[7];
    static String[][] meStr=new String[2][5];
    //static Loader[][] Player=new Loader[2][5];
    private static final bullet[] Bullet=new bullet[MAX_BULLETS];
    public static void main(String[] args){
        game=new Game();
        
        
        game.start();
    }
   
    private boolean isColliding(bullet p,Player e){
        int px1=p.getX(),px2=px1+p.getSize();
        int py1=p.getY(),py2=py1+p.getSize();
        int ex1=e.x,ex2=ex1+e.pImg[e.s][e.ci].image.getWidth();
        int ey1=e.y,ey2=ey1+e.pImg[e.s][e.ci].image.getHeight();
        if(px1<ex1&&px2>ex1&&py1<=ey1&&py2>=ey1||ex1<px1&&ex2>px1&&ey1<=py1&&ey2>=py1){
            return true; 
        }
        
        return false;
    }
    private boolean isColliding(Player p,Player e){
        int px1=p.x,px2=px1+p.pImg[p.s][p.ci].image.getWidth();
        int py1=p.y,py2=py1+p.pImg[p.s][p.ci].image.getHeight();
        int ex1=e.x,ex2=ex1+e.pImg[e.s][e.ci].image.getWidth();
        int ey1=e.y,ey2=ey1+e.pImg[e.s][e.ci].image.getHeight();
        if(px1<ex1&&px2>ex1&&py1<=ey1&&py2>=ey1||ex1<px1&&ex2>px1&&py1<=ey1&&py2>=ey1){
            return true; 
        }
        
        return false;
    }
    private boolean isTopCollide(Player p,Player e){
        int px1=p.x,px2=px1+p.pImg[p.s][p.ci].image.getWidth();
        int py1=p.y,py2=py1+p.pImg[p.s][p.ci].image.getHeight();
        int ex1=e.x,ex2=ex1+e.pImg[e.s][e.ci].image.getWidth();
        int ey1=e.y,ey2=ey1+e.pImg[e.s][e.ci].image.getHeight();
        int pxhalf=e.pImg[e.s][e.ci].image.getWidth()*7/8;
        int pyhalf=p.pImg[p.s][p.ci].image.getHeight()/2;
        int exhalf=e.pImg[e.s][e.ci].image.getWidth()/8;
        int eyhalf=ey1+e.pImg[e.s][e.ci].image.getHeight()*1/8;
        if((px1<ex1&&px2>=ex1+exhalf||px1<=ex1+pxhalf&&px2>ex2)&&-ey1+py2<eyhalf){
            return true; 
        }
        
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        
    }
    
    private static DrawImage[] di=new DrawImage[pixelx*pixely+1];
    private static DrawImage ug;

    private boolean refresh=true;
    
    protected void paintComponent(Graphics g){
        //Color skyblue=new Color(15,203,202);
        
        Graphics2D g2d=(Graphics2D)g;
        g2d.translate(camX*1/4, 0);
        ug= new DrawImage(bg.image,0,0,pixelx*pixely,game.getHeight(),false);
   
        ug.draw(g);

        g2d.translate(-camX*1/4, 0);

        g2d.translate(camX,0);
/*        
        g2d.setColor(Color.red);
        g2d.draw(player.getBoundTop());
        g2d.draw(player.getBoundBottom());
        g2d.draw(player.getBoundLeft());
        g2d.draw(player.getBoundRight());
  */      
        if(shooting ==true){
            for(int i=0;i<5;i++){
                if(Bullet[i]==null)continue;
                Bullet[i].draw(g);
            }
        }

        
        if(player.s==0){
            if(-camX-500>0)xx=(-camX-500)/32;
            else xx=0;
            if(1500-camX>pixelx*32)ll=pixelx;
            else ll=(1500-camX)/32;
        }
        else{
            if(game.getWidth()-camX-1500>0)xx=(game.getWidth()-camX-1500)/32;
            else xx=0;
            if(game.getWidth()-camX+500>pixelx*32)ll=pixelx;
            else ll=(game.getWidth()-camX+500)/32;
        }
        
        
        
        for(int x=xx,i=0;x<ll;x++){
            for(int yy=0;yy<pixely;yy++,i++){
                int pixel=level.image.getRGB(x, yy);
                int red=(pixel >> 16) & 0xff;
                int green=(pixel >> 8) & 0xff;
                int blue=(pixel) & 0xff;
                
                if(red==255&&green==255&&blue==255){
                     di[i]=new DrawImage(blockImg[0].image,x*32,yy*32,32,32,true);
                    di[i].draw(g);
                }
                else if(red==0&&green==0&&blue==255){
                    di[i]=new DrawImage(blockImg[1].image,x*32,yy*32,32,32,true);

                    di[i].draw(g);
                   
                }
                else if(red==255&&green==0&&blue==255){
                    di[i]=new DrawImage(blockImg[2].image,x*32,yy*32,32,32,false);

                    di[i].draw(g);
                   
                }
                else if(red==0&&green==255&&blue==255){
                    di[i]=new DrawImage(blockImg[3].image,x*32,yy*32,32,32,false);

                    di[i].draw(g);
                   
                }else if(red==0&&green==255&&blue==0){
                    di[i]=new DrawImage(blockImg[4].image,x*32,yy*32,32,32,true);

                    di[i].draw(g);
                   
                }
                
                
            }
            
        }
        player.drawPlayer(g, player.x,player.y, player.pImg[player.s][player.ci].image.getWidth(),player.pImg[player.s][player.ci].image.getHeight());

        for(int e=0;e<MAX_ENEMYS;e++){
            if(naEnemy[e]==null)continue;
            if(naEnemy[e].x>xx*32&&naEnemy[e].x<ll*32){
                naEnemy[e].drawPlayer(g,naEnemy[e].x,naEnemy[e].y,  naEnemy[e].pImg[naEnemy[e].s][naEnemy[e].ci].image.getWidth(),naEnemy[e].pImg[naEnemy[e].s][naEnemy[e].ci].image.getHeight());
            }
            
        }
        
        /*
        if(player.gid!=-1){ 
            g2d.setColor(Color.CYAN);
            g2d.draw(di[player.gid].getBound());
        }
        if(col!=-1){ 
            g2d.setColor(Color.GREEN);
            g2d.draw(di[col].getBound());
        }*/
        g2d.translate(-camX,0);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar()=='w'||e.getKeyChar()=='W'){
            
           if(player.onGround==true)
            player.jumping=true;         
        }
        
    }
   
    @Override    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()=='a'||e.getKeyChar()=='A'){
            idle=false;
            velX=-12;
            if(player.s==0)camChange=true;
            player.s=1;
            move=true;
            
        }
        if(e.getKeyChar()=='d'||e.getKeyChar()=='D'){
            idle=false;
            velX=12;
            if(player.s==1)camChange=true;
            player.s=0;
            move=true;
            
           
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            if(shooting==false)shooting=true;
            if(shootdelay<=0&&counts<MAX_BULLETS){
                for(int i=0;i<5;i++){
                    if(Bullet[i]!=null)continue;
                    Bullet[i]=new bullet(player.x,player.y+player.pImg[player.s][player.ci].image.getHeight()*3/4,10,Color.red);
                    velBX[i]=(player.s==0)?40:-40;
                    break;
                }
                counts++;
                shootdelay=8;
                
                
            }
            
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar()=='a'||e.getKeyChar()=='A'||e.getKeyChar()=='d'||e.getKeyChar()=='D'){
            velX=0;
            move=false;
            player.delay=0;
            idle=true;
        }
    }
    public void respawn(){
        player.x=xPos;
        player.y=yPos;
        player.onGround=false;
        player.jumping=false;
        player.gid=-1;
        player.s=0;
        player.jump=18;
        player.dead=false;
    }
    
}
