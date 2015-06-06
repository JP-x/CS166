package CS166;

import CS166.ProfNetwork;
import java.awt.EventQueue;
import java.awt.BorderLayout;
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
    ProfNetwork esql = null;
    
    NetworkGUI(String[] args){
      try{
        esql = new ProfNetwork(args[0], args[1], args[2], args[3]);
      } catch(Exception e){
      
      }
      setTitle(title);
      setSize(640,480);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      FlowLayout experimentLayout = new FlowLayout();
      
      JLabel userLabel = new JLabel("Username: ");
      JLabel passLabel = new JLabel("Password: ");
      final JTextField userField = new JTextField(20);
      final JTextField passField = new JTextField(20);
      userLabel.setBounds(400,400,100,30);
      passLabel.setBounds(400,500, 100, 30);
      userField.setBounds(400, 400, 100, 30);
      passField.setBounds(600, 600, 200, 30);
      
      JButton b1 = new JButton("Create user");
      JButton b2 = new JButton("Log in");
      JButton b3 = new JButton("Exit");
      b1.setBounds(400,100,200,30);
      b2.setBounds(400,200,200,30);
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          loginAction(evt, userField.getText(), passField.getText());
        }
      });
      b3.setBounds(400,300,200,30);
      b3.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          exitAction(evt);
        }
      });
      
      ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("", icon, JLabel.LEFT);
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      panel.add(label);
      getContentPane().add(panel, BorderLayout.NORTH);
      
      panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(userLabel);
      panel.add(userField);
      panel.add(passLabel);
      panel.add(passField);
      getContentPane().add(panel, BorderLayout.CENTER); 
      
      panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      panel.add(b1);
      panel.add(b2);
      panel.add(b3);
      getContentPane().add(panel, BorderLayout.SOUTH);
      
      pack();
      setVisible(true);
  }
  
  private void loginAction(java.awt.event.ActionEvent evt, String user, String pass){
  
  }
  
  private void exitAction(java.awt.event.ActionEvent evt){
    System.exit(0);
  }
    
    
    public static void main(String[] args){  
        NetworkGUI main_menu = new NetworkGUI(args);  
    }  
}
