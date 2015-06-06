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
    //private JPanel login_screen;
    //private JPanel main_menu;
    //private login_panel log;
    
    NetworkGUI(String[] args){
      try{
        // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
        esql = new ProfNetwork(args[0], args[1], args[2], "");
        System.out.println("Argument " + args[0]);
      } catch(Exception e){
        System.err.println("Error: "+ e.getMessage());
      }
      setTitle(title);
      setSize(640,480);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      //FlowLayout experimentLayout = new FlowLayout();
      
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
      //b1.setBounds(400,100,200,30);
      b1.setLocation(400,100);
      b1.setSize(200,30);
      //LOGIN BUTTON
      //b2.setBounds(400,200,200,30);
      b2.setLocation(400,200);
      b2.setSize(200,30);
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          int success = 0;
          success = loginAction(evt, userField.getText(), passField.getText());
          if(success == 1)
          {
              //clear out all components for new window
              
              //TODO: implement new panel to then add to the stupid frame
              //EVERY BUTTON WILL HAVE ITS OWN ACTION PERFORMED
              //WHICH WILL THEN CLEAR BASED ON SUCCESS VALUE
              getContentPane().removeAll();
              getContentPane().repaint();
            JButton b4 = new JButton("Exit");
            b4.setLocation(400,300);
            b4.setSize(200,30);
            b4.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
            exitAction(evt);
            }
                });
            
             getContentPane().add(b4, BorderLayout.SOUTH);
             setVisible(true);
          }
        }
      });
      //EXIT BUTTON
      //b3.setBounds(400,300,200,30);
      b3.setLocation(400,300);
      b3.setSize(200,30);
      b3.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          exitAction(evt);
        }
      });
      
      
     // JPanel main_menu = new JPanel();
    //  main_menu.setLayout(new CardLayout());
      
      ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("", icon, JLabel.LEFT);
      JPanel logo_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      logo_panel.add(label);
      getContentPane().add(logo_panel, BorderLayout.NORTH);
      
      JPanel all_login = new JPanel(new CardLayout());
      
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
      //main_menu.add(panel, BorderLayout.NORTH);
      getContentPane().add(buttons_panel, BorderLayout.SOUTH);
      
      //getContentPane().add(main_menu,FlowLayout.CENTER);
      
      pack();
      setVisible(true);
  }
  
  private int loginAction(java.awt.event.ActionEvent evt, String user, String pass){
    String status = ProfNetwork.LogIn(esql, user, pass);
    System.out.println("Username: " + user);
    System.out.println("Password: " + pass);
    if(status.equals("login")){
      //Login Passed
      System.out.println("Go to User Profile.");
      return 1;
    }
    else{
      //Login Failed
      System.out.println("Login Failed");
      return -1;
    }
  }
  
  private void exitAction(java.awt.event.ActionEvent evt){
    System.exit(0);
  }
    
    
    public static void main(String[] args){  
        NetworkGUI main_menu = new NetworkGUI(args);  
    }  
}

/*
class login_panel extends JPanel{
    private JButton jcomp1;
    private JButton jcomp2;
    private JButton jcomp3;
    private JTextField jcomp4;

    public login_panel() {
        //construct components
        jcomp1 = new JButton ("test1");
        jcomp2 = new JButton ("test2");
        jcomp3 = new JButton ("test3");
        jcomp4 = new JTextField (5);

        //adjust size and set layout
        setSize (640, 480);
        setLayout (null);

        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (20, 45, 100, 25);
        jcomp2.setBounds (135, 60, 100, 25);
        jcomp3.setBounds (260, 35, 100, 25);
        jcomp4.setBounds (105, 115, 100, 25);

        //add components
        add (jcomp1);
        add (jcomp2);
        add (jcomp3);
        add (jcomp4);       
    }
}
*/
