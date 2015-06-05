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
    //setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    //add("Center", esql);
    FlowLayout experimentLayout = new FlowLayout();
    JButton b1 = new JButton("Create user");
    JButton b2 = new JButton("Log in");
    JButton b3 = new JButton("Exit");
    //setBounds(x,y,width,height
    b1.setBounds(400,100,200,30);
    b2.setBounds(400,200,200,30);
    b3.setBounds(400,300,200,30);
    
        
    getContentPane().setLayout(new FlowLayout());
    ImageIcon icon = new ImageIcon("../images/networklogothing.png");
    JLabel label = new JLabel("", icon, JLabel.LEADING);
    add(label);
    add(b1);
    add(b2);
    add(b3);
    //JPasswordField passwordField = new JPasswordField(10);
    //passwordField.setActionCommand(OK);
    //passwordField.addActionListener(passwordField);
    
    //JPanel picpanel = new JPanel();
    //ImageIcon logo = new ImageIcon("networklogothing.png");
    //JLabel labeel = new JLabel(logo);
    //add(labeel);
    //picpanel.add(new JLabel(logo));
    //add(picpanel);
    
    //ImageIcon logo = new ImageIcon("..\\images\\networklogothing.png");
    
    //setLayout(null);//no layout manager  
    pack();
    setVisible(true);//now frame will be visible, by default not visible  
    }
    
    
    public static void main(String[] args){  
        NetworkGUI main_menu=new NetworkGUI();  
    }  
}
