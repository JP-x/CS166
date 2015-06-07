package CS166;

import CS166.*;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
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
  
  private boolean passAction(java.awt.event.ActionEvent evt, String user, String pass, String newPass){
    String status = ProfNetwork.ChangePassword(user, esql, pass, newPass);
    if(status.equals("changed")){
        //Password changed
        return true;
    }
    else{
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
          //value passed in determines what label is shown
          getContentPane().removeAll();
          getContentPane().repaint();
          viewCreateUser(0);
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

      friends.setLocation(400,100);
      friends.setSize(200,30);

      profile.setLocation(600,100);
      profile.setSize(200,30);
      
      sendMsg.setLocation(400,300);
      sendMsg.setSize(200,30);
      
      viewMsg.setLocation(600,400);
      viewMsg.setSize(200,30);
      
      search.setLocation(400,500);
      search.setSize(200,30);
      
      passwd.setLocation(600,600);
      passwd.setSize(200,30);
      
      logout.setLocation(400,700);
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
          viewPasswd(user,0);
        }
      });
      
      logout.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewStart();
        }
      });
      
      String welcome_mes = "Welcome to The Network " + user + "!\n";
      ImageIcon user_icon = new ImageIcon("../images/usericon.png");
      JLabel label = new JLabel(welcome_mes, user_icon, JLabel.LEFT);
      JPanel user_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      user_panel.add(label);
      getContentPane().add(user_panel, BorderLayout.NORTH);
      
      JPanel buttons_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttons_panel.add(friends);
      buttons_panel.add(profile);
      buttons_panel.add(sendMsg);
      buttons_panel.add(viewMsg);
      buttons_panel.add(search);
      buttons_panel.add(passwd);
      buttons_panel.add(logout);
      getContentPane().add(buttons_panel, BorderLayout.CENTER);
      
      pack();
      setVisible(true);
  }
  
  
  //value passed in determines what label is shown
  // 0  - Create New account
  // 1  - Account created
  // 2  - User already exists
  private void viewCreateUser(int user_created_state){
      JLabel userLabel = new JLabel("New Username: \n");
      JLabel passLabel = new JLabel("New Password: \n");
      JLabel emailLabel = new JLabel("email: \n");
      JLabel nameLabel = new JLabel("name: \n");
      JLabel bdayLabel = new JLabel("bday: \n");
      
      final JTextField userField = new JTextField(20);
      final JTextField passField = new JTextField(20);
      final JTextField emailField = new JTextField(20);
      final JTextField nameField = new JTextField(20);
      final JTextField bdayField = new JTextField(20);
      
      //x,y,width,height
      userLabel.setBounds(100,400,100,30);
      passLabel.setBounds(100,500, 100, 30);
      emailLabel.setBounds(100,600,100,30);
      nameLabel.setBounds(100,700, 100, 30);
      bdayLabel.setBounds(100,800, 100, 30);
      
      userField.setBounds(100, 400, 200, 30);
      passField.setBounds(100, 500, 200, 30);
      emailField.setBounds(100, 600, 200, 30);
      nameField.setBounds(100, 700, 200, 30);
      bdayField.setBounds(100, 800, 200, 30);
      
      
      JButton b2 = new JButton("Create User");
      JButton b3 = new JButton("Cancel");

      b2.setLocation(400,200);
      b2.setSize(200,30);

      b3.setLocation(400,300);
      b3.setSize(200,30);
      //
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          /*
          boolean success = false;
          success = loginAction(evt, userField.getText(), passField.getText());
          
          if(loginAction(evt, userField.getText(), passField.getText())){
            getContentPane().removeAll();
            getContentPane().repaint();
            viewProfile(userField.getText());
          }
          */
        }
      });
      
      //hit cancel go back to start menu 
      b3.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();          
          viewStart();
        }
      });
  
      ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("", icon, JLabel.LEFT);
      if(user_created_state == 0) //initial run through
      {
        label = new JLabel("Create New Account", icon, JLabel.LEFT);
      }
      else if ( user_created_state == 1) // successful addition
      {
          label = new JLabel("Account Created", icon, JLabel.LEFT);
      }
      else if (user_created_state == 2) //  user already exists
      {
          label = new JLabel("User exists", icon, JLabel.LEFT);
      }
 
      JPanel logo_panel = new JPanel();
      logo_panel.add(label);
      getContentPane().add(logo_panel, BorderLayout.NORTH);
      
      JPanel loginpanel = new JPanel();
      loginpanel.add(userLabel);
      loginpanel.add(userField);
      loginpanel.add(passLabel);
      loginpanel.add(passField);
      loginpanel.add(emailLabel);
      loginpanel.add(emailField);
      //set to west
      getContentPane().add(loginpanel, BorderLayout.CENTER); 
      
      JPanel loginpanel2 = new JPanel();
      loginpanel2.add(nameLabel);
      loginpanel2.add(nameField);
      loginpanel2.add(bdayLabel);
      loginpanel2.add(bdayField);
      loginpanel2.add(b2);
      loginpanel2.add(b3);
      getContentPane().add(loginpanel2, BorderLayout.PAGE_END); 
      
      pack();
      setVisible(true);
  
  
  
  
  }
  
  private void viewFriends(String user){
    viewMenu(user);
    
   // pack();
    setVisible(true);
  }
  
  private void viewProfile(String user){
    viewMenu(user);
   // pack();
    setVisible(true);
  }
  
  private void viewMsgSend(String user){
    viewMenu(user);
    //pack();
    setVisible(true);
  }
  
  private void viewMsgView(String user){
    viewMenu(user);
   // pack();
    setVisible(true);
  }
  
  private void viewSearch(String user){
    viewMenu(user);
   // pack();
    setVisible(true);
  }
  
  private void viewPasswd(final String user,int changed_pwd_success){
    //viewMenu(user);
    
      JLabel oldpassLabel = new JLabel("Enter Old Password: ");
      JLabel newpassLabel = new JLabel("Enter New Password: ");
      final JTextField oldpassField = new JTextField(20);
      final JTextField newpassField = new JTextField(20);
      oldpassLabel.setBounds(400,400,100,30);
      newpassLabel.setBounds(400,500, 100, 30);
      oldpassField.setBounds(400, 400, 100, 30);
      newpassField.setBounds(600, 600, 200, 30);
      
      JButton b1 = new JButton("Cancel");
      JButton b2 = new JButton("Change Password");

      b1.setLocation(400,100);
      b1.setSize(200,30);

      b2.setLocation(400,200);
      b2.setSize(200,30);
      
      //cancel button b1
      b1.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            //go back to menu
            getContentPane().removeAll();
            getContentPane().repaint();
            viewMenu(user);
        }
      });
      
      //change password button b2
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          boolean success = false;
          //test password
          success = passAction(evt, user, oldpassField.getText(), newpassField.getText());
          
          if(success){
            getContentPane().removeAll();
            getContentPane().repaint();
            //viewProfile(userField.getText());
            viewPasswd(user,1);
          }
          else
          {
            viewPasswd(user,2);
          }
        }
      });
  
      
      ImageIcon icon = new ImageIcon("../images/usericon.png");
      
      JLabel label = new JLabel("", icon, JLabel.LEFT);
      if(changed_pwd_success == 1)
      {
          label = new JLabel("Password changed", icon, JLabel.LEFT);
      }
      else if (changed_pwd_success == 2)
      {
          label = new JLabel("Invalid Password", icon, JLabel.LEFT);   
      }
      JPanel icon_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      icon_panel.add(label);
      getContentPane().add(icon_panel, BorderLayout.NORTH);
      
      JPanel loginpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      loginpanel.add(oldpassLabel);
      loginpanel.add(oldpassField);
      loginpanel.add(newpassLabel);
      loginpanel.add(newpassField);
      getContentPane().add(loginpanel, BorderLayout.CENTER); 
      
      JPanel buttons_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      buttons_panel.add(b2);
      buttons_panel.add(b1);
      getContentPane().add(buttons_panel, BorderLayout.SOUTH);
      
      pack();
      setVisible(true);
  }
    
    
  public static void main(String[] args){  
    NetworkGUI main_menu = new NetworkGUI(args);  
  }  
}
