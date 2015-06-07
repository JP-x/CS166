package CS166;

import CS166.*;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.FlowLayout;
import java.io.File;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.io.IOException;

public class NetworkGUI extends JFrame{
    String title = "The Network";
    private BufferedImage image;
    ProfNetwork esql = null;
    
    NetworkGUI(String[] args){
      try{
        // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
        esql = new ProfNetwork(args[0], args[1], args[2], "");
        setTitle(title);
        setSize(640,480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        viewStart();
      }catch(Exception e){
        System.err.println("Error: "+ e.getMessage());
      }
  }
  
  private boolean loginAction(java.awt.event.ActionEvent evt, String user, String pass){
    String status = ProfNetwork.LogIn(esql, user, pass);
    if(status.equals("login")){
      //Login Passed
      System.out.println("Go to User Profile.");
      return true;
    }
    else{
      //Login Failed
      System.out.println("Login Failed");
      return false;
    }
  }
  
  private void exitAction(java.awt.event.ActionEvent evt){
    System.exit(0);
  }
  
  private void viewStart(){
      
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

      b1.setLocation(400,100);
      b1.setSize(200,30);

      b2.setLocation(400,200);
      b2.setSize(200,30);

      b3.setLocation(400,300);
      b3.setSize(200,30);
      
      b1.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          viewCreateUser();
        }
      });
      
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          boolean success = false;
          success = loginAction(evt, userField.getText(), passField.getText());
          if(loginAction(evt, userField.getText(), passField.getText())){
            getContentPane().removeAll();
            getContentPane().repaint();
            viewProfile(userField.getText());
          }
        }
      });
      
      b3.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          exitAction(evt);
        }
      });
  
      
      ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("", icon, JLabel.LEFT);
      JPanel logo_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      logo_panel.add(label);
      getContentPane().add(logo_panel, BorderLayout.NORTH);
      
      JPanel loginpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      loginpanel.add(userLabel);
      loginpanel.add(userField);
      loginpanel.add(passLabel);
      loginpanel.add(passField);
      getContentPane().add(loginpanel, BorderLayout.CENTER); 
      
      JPanel buttons_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      buttons_panel.add(b2);
      buttons_panel.add(b1);
      buttons_panel.add(b3);
      getContentPane().add(buttons_panel, BorderLayout.SOUTH);
      
      pack();
      setVisible(true);
  }
  
  private void viewMenu(final String user){
      JButton friends = new JButton("Friends List");
      JButton profile = new JButton("Update Profile");
      JButton sendMsg = new JButton("Send A Message");
      JButton viewMsg = new JButton("View Messages");
      JButton search = new JButton("Search Profiles");
      JButton passwd = new JButton("Change Password");
      JButton logout = new JButton("Logout");

      friends.setLocation(100,100);
      friends.setSize(200,30);

      profile.setLocation(100,200);
      profile.setSize(200,30);
      
      sendMsg.setLocation(100,300);
      sendMsg.setSize(200,30);
      
      viewMsg.setLocation(100,400);
      viewMsg.setSize(200,30);
      
      search.setLocation(100,500);
      search.setSize(200,30);
      
      passwd.setLocation(100,600);
      passwd.setSize(200,30);
      
      logout.setLocation(100,700);
      logout.setSize(200,30);
      
      friends.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewFriends(user);
        }
      });
      
      profile.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewProfile(user);
        }
      });
      
      sendMsg.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewMsgSend(user);
        }
      });
      
      viewMsg.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewMsgView(user);
        }
      });
      
      search.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewSearch(user);
        }
      });
      
      passwd.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewPasswd(user);
        }
      });
      
      logout.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewStart();
        }
      });
      
      JPanel buttons_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      buttons_panel.add(friends);
      buttons_panel.add(profile);
      buttons_panel.add(sendMsg);
      buttons_panel.add(viewMsg);
      buttons_panel.add(search);
      buttons_panel.add(passwd);
      buttons_panel.add(logout);
      getContentPane().add(buttons_panel, BorderLayout.SOUTH);
      
      //pack();
      //setVisible(true);
  }
  
  private void viewCreateUser(){
  
  }
  
  private void viewFriends(String user){
    viewMenu(user);
    pack();
    setVisible(true);
  }
  
  private void viewProfile(String user){
    viewMenu(user);
    pack();
    setVisible(true);
  }
  
  private void viewMsgSend(String user){
    viewMenu(user);
    pack();
    setVisible(true);
  }
  
  private void viewMsgView(String user){
    viewMenu(user);
    pack();
    setVisible(true);
  }
  
  private void viewSearch(String user){
    viewMenu(user);
    pack();
    setVisible(true);
  }
  
  private void viewPasswd(String user){
    viewMenu(user);
    pack();
    setVisible(true);
  }
    
    
  public static void main(String[] args){  
    NetworkGUI main_menu = new NetworkGUI(args);  
  }  
}
