/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;




import java.awt.Toolkit;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Nitesh
 */
public class Window{
    
    public Window(int width,int height,String title,Game game){

        JFrame frame=new JFrame(title);
        frame.add(game);
        frame.pack();
        
        
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(game);
        frame.setVisible(true);
        frame.setSize(width,height);
        game.start();
    }
    public Window(String title,Game game){

        JFrame frame=new JFrame(title);
        frame.add(game);
     
        
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(game);
        frame.setVisible(true);
        Toolkit tk=Toolkit.getDefaultToolkit();
        int width=(int)tk.getScreenSize().getWidth();
        int height=(int)tk.getScreenSize().getHeight();
        frame.setSize(width,height);
        game.start();
    }
    
}
