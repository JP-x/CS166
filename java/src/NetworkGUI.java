//import package.ProfNetwork;
import java.awt.EventQueue;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;

public class NetworkGUI extends JFrame{
    String title = "The Network";
    private BufferedImage image;
    
    NetworkGUI(){
    setTitle(title);
    setSize(640,480);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    FlowLayout experimentLayout = new FlowLayout();
    JButton b1 = new JButton("Create user");
    JButton b2 = new JButton("Log in");
    JButton b3 = new JButton("Exit");
    b1.setBounds(400,100,200,30);
    b2.setBounds(400,200,200,30);
    b3.setBounds(400,300,200,30);
    b3.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(java.awt.event.ActionEvent evt){
        exitAction(evt);
      }
    });
    
    getContentPane().setLayout(new FlowLayout());
    ImageIcon icon = new ImageIcon("../images/networklogothing.png");
    JLabel label = new JLabel("", icon, JLabel.LEADING);
    add(label);
    add(b1);
    add(b2);
    add(b3); 
    pack();
    setVisible(true);
  }
  
  private void exitAction(java.awt.event.ActionEvent evt){
    System.exit(0);
  } 
    
    
    public static void main(String[] args){  
        NetworkGUI main_menu = new NetworkGUI();  
    }  
}
