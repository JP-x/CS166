package CS166;

import CS166.*;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.JComponent;
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
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

public class NetworkGUI extends JFrame{
    String title = "The Network";
    String currentUser = null;
    private BufferedImage image;
    ProfNetwork esql = null;
    
    NetworkGUI(String[] args){
      try{
        // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
        esql = new ProfNetwork(args[0], args[1], args[2], "");
        setTitle(title);
        setSize(640,768);
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
      currentUser = user;
      return true;
    }
    else{
      //Login Failed
      System.out.println("Login Failed");
      return false;
    }
  }
  
  private boolean canAddFriend(String user, String check){
     try{
       List<List<String>> options = new ArrayList<List<String>>();
		   List<List<String>> friends = ProfNetwork.getFriends(esql, user);
		   //options.addAll(friends);
       for(int i = 0; i < friends.size(); ++i){
        if(friends.get(i).get(0).equals(check)){
          return false;
        }
       }
       if(friends.size() < 5){
        return true;
       }

		   for(int i = 0; i < friends.size(); ++i){
			   List<List<String>> one = ProfNetwork.getFriends(esql, friends.get(i).get(0));
			   options.addAll(one);
			   for(int j = 0; j < one.size(); ++j){
				   List<List<String>> two = ProfNetwork.getFriends(esql, one.get(j).get(0));
				   options.addAll(two);
           for(int k = 0; k < two.size(); ++k){
              List<List<String>> three = ProfNetwork.getFriends(esql, two.get(k).get(0));
              options.addAll(three);
           }
			   }
		   }
       
		   for(int i = 0; i < options.size(); ++i){
          if(options.get(i).get(0).equals(check)){
            return true;
          }
       }
       return false;
	   }catch(Exception e){
		   System.err.println(e.getMessage() );
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
  
  private boolean deleteAction(String msgId, String user){
    ProfNetwork.deleteMsg(esql, msgId, user);
    return true;
  }
  
  private boolean createAction(String user, String pass,String email, String name, String dob){
    return ProfNetwork.createUser(esql, user, pass, email, name, dob);
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
            viewMenu(userField.getText());
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
      JButton profile = new JButton("View Profile");
      JButton requests = new JButton("View Friend Requests");
      JButton sendMsg = new JButton("Send A Message");
      JButton viewMsg = new JButton("View Messages");
      JButton search = new JButton("Search Profiles");
      JButton passwd = new JButton("Change Password");
      JButton logout = new JButton("Logout");

      friends.setLocation(400,100);
      friends.setSize(200,30);
      
      requests.setLocation(400,100);
      requests.setSize(200,30);

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
      
      requests.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewRequests(user);
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
          viewMsgSend(user,-1);
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
          viewSearch(user,0, "");
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
      buttons_panel.add(requests);
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
      
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            getContentPane().removeAll();
            getContentPane().repaint();
            boolean created = createAction(userField.getText(), passField.getText(), emailField.getText(), nameField.getText(), bdayField.getText());
            if(created){
              getContentPane().removeAll();
              getContentPane().repaint();
              currentUser = userField.getText();
              viewMenu(currentUser);
            }
            else{
              getContentPane().removeAll();
              getContentPane().repaint();
              viewCreateUser(3);
            }
            //value passed in determines what label is shown
            // 0  - Create New account
            // 1  - Account created
            // 2  - User already exists
            // 3  - User error
            
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
      else if (user_created_state == 3)
      {
          label = new JLabel("Error", icon, JLabel.LEFT);
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
    System.out.println("Go to Friends List");
    List<List<String>>results = new ArrayList<List<String>>();
    results = ProfNetwork.getFriends(esql, user);
    if(results == null){return;}
    JButton friends[];
    friends = new JButton[results.size()];
    
    ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("", icon, JLabel.LEFT);
      JPanel logo_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      logo_panel.add(label);
      getContentPane().add(logo_panel, BorderLayout.NORTH);
      
    
    JPanel friends_panel = new JPanel();
    for(int i = 0; i < results.size(); ++i){
      friends[i] = new JButton(results.get(i).get(0));
      friends[i].setSize(200,30);
      final String temp = results.get(i).get(0);
      friends[i].addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewProfile(temp);
        }
      });
      friends_panel.add(friends[i]);
    }
    getContentPane().add(friends_panel, BorderLayout.CENTER);
    JButton cancel = new JButton("Go Back");
    cancel.setSize(200,30);
    
    //cancel button b1
      cancel.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            //go back to menu
            getContentPane().removeAll();
            getContentPane().repaint();
            viewMenu(currentUser);
        }
      });
    JPanel end = new JPanel();
    end.add(cancel);
    getContentPane().add(end, BorderLayout.SOUTH);
    
    pack();
    setVisible(true);
  }
  
  private void viewProfile(final String user){
    String welcome_mes = user + "'s Profile";
    System.out.println(welcome_mes);
    ImageIcon user_icon = new ImageIcon("../images/usericon.png");
    JLabel label = new JLabel(welcome_mes, user_icon, JLabel.LEFT);
    JPanel user_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    user_panel.add(label);
    getContentPane().add(user_panel, BorderLayout.NORTH);
    
    List<String> info = ProfNetwork.getUserInfo(esql, user);
    JLabel username;
    JLabel email;
    JLabel name;
    JLabel dob;
    if(info != null){
      username = new JLabel("Username: " + info.get(0));
      email = new JLabel("Email: " + info.get(2));
      name = new JLabel("Name: " + info.get(3));
      dob = new JLabel("Date of Birth: " + info.get(4));
    }
    else{
      username = new JLabel("Username: ");
      email = new JLabel("Email: ");
      name = new JLabel("Name: ");
      dob = new JLabel("Date of Birth: ");
    }
    JButton cancel = new JButton("Go Back");
    cancel.setSize(200,30);
    //Check if can add friend
    JButton addFriend = new JButton("Add Friend");
    addFriend.setSize(200,30);
    
    addFriend.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(java.awt.event.ActionEvent evt){
        getContentPane().removeAll();
        getContentPane().repaint();
        ProfNetwork.addFriend(esql, currentUser, user);
        final String reload = user;
        viewProfile(reload);
      }
    });
    
    JButton removeFriend = new JButton("Remove Friend");
    removeFriend.setSize(200,30);
    
    removeFriend.addActionListener(new java.awt.event.ActionListener(){
      public void actionPerformed(java.awt.event.ActionEvent evt){
        getContentPane().removeAll();
        getContentPane().repaint();
       // ProfNetwork.removeFriend(esql, currentUser, user);
        viewProfile(user);
      }
    });
    
    //cancel button b1
      cancel.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            //go back to menu
            getContentPane().removeAll();
            getContentPane().repaint();
            viewMenu(currentUser);
        }
      });
      
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    infoPanel.add(username);
    infoPanel.add(email);
    infoPanel.add(name);
    infoPanel.add(dob);
    getContentPane().add(infoPanel, BorderLayout.CENTER);
    
    JPanel optionsPanel = new JPanel();
    optionsPanel.add(cancel);
    if(!currentUser.equals(user) && canAddFriend(currentUser, user)){
      optionsPanel.add(addFriend);
    }
    getContentPane().add(optionsPanel, BorderLayout.SOUTH);
    pack();
    setVisible(true);
  }
  
  
  private void viewMsgSend(final String user, int msg_state){
      JLabel userLabel = new JLabel("To:");
      final JTextField userField = new JTextField(20);
      final JTextField messField = new JTextField(100);
      userLabel.setBounds(400,400,100,30);
      userField.setBounds(400, 400, 100, 30);
      messField.setBounds(400, 400, 100, 100);
      messField.setPreferredSize(new Dimension(100,100));
      
      JButton b2 = new JButton("Send Message");
      JButton b3 = new JButton("Cancel");
      
      b2.setLocation(400,200);
      b2.setSize(200,30);

      b3.setLocation(400,300);
      b3.setSize(200,30);
      
      //send button
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          List<String> info = ProfNetwork.getUserInfo(esql, userField.getText());
          if(info == null){
            getContentPane().removeAll();
          getContentPane().repaint();
          
            viewMsgSend(user, 0);
          }
          else{
            boolean sent = ProfNetwork.sendMessage(esql, currentUser, userField.getText(), messField.getText());
            if(sent){
              getContentPane().removeAll();
          getContentPane().repaint();
          
              viewMsgSend(user,1);
            }
          }
        }
      });
      //cancel search button
      b3.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewMenu(user);
        }
      });
  
      ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("Send Message", icon, JLabel.LEFT);
      JPanel logo_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      logo_panel.add(label);
      getContentPane().add(logo_panel, BorderLayout.NORTH);
      
      JPanel searchpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      searchpanel.add(userLabel);
      searchpanel.add(userField);
      searchpanel.add(b2);
      searchpanel.add(b3);
      getContentPane().add(searchpanel, BorderLayout.CENTER); 
      
      JPanel message_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      //user results here
      if(msg_state == 0)
      {
        JLabel resultsLabel = new JLabel("Cannot find user");
        message_panel.add(resultsLabel);
      }
      else if(msg_state ==1)
      {
        JLabel resultsLabel = new JLabel("Message Sent.");
        //display that users information
        message_panel.add(resultsLabel);
      }
      message_panel.add(messField);
      getContentPane().add(message_panel, BorderLayout.SOUTH);
      pack();
      setVisible(true);
  }
  
  private void viewRequests(final String user){
    List<List<String>> requests = ProfNetwork.getRequests(esql, currentUser);
    for(int i = 0; i < requests.size(); ++i){
      JPanel reqPanel = new JPanel();
      final String req = requests.get(i).get(0);
      String message = "<html>" + req +"<br></html>";
      JLabel l = new JLabel(message);
      JButton bt = new JButton("Accept");
      bt.setSize(200,30);
      JButton dt = new JButton("Reject");
      dt.setSize(200,30);
      bt.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            //go back to menu
            getContentPane().removeAll();
            getContentPane().repaint();
            ProfNetwork.confirmFriend(esql, currentUser, req);
            viewRequests(currentUser);
        }
      });
      dt.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          ProfNetwork.rejectFriend(esql, currentUser, req);
          viewRequests(currentUser);
        }
      });
      reqPanel.add(l);
      reqPanel.add(bt);
      reqPanel.add(dt);
      getContentPane().add(reqPanel, BorderLayout.CENTER);
    }
    JButton cancel = new JButton("Go Back");
    cancel.setSize(200,30);
    
    //cancel button b1
      cancel.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            //go back to menu
            getContentPane().removeAll();
            getContentPane().repaint();
            viewMenu(currentUser);
        }
      });
    JPanel end = new JPanel();
    end.add(cancel);
    getContentPane().add(end, BorderLayout.SOUTH);
    pack();
    setVisible(true);
  }
  private void viewMsgView(final String user){
    List<List<String>> messages = ProfNetwork.getMessages(esql, currentUser);
    System.out.println(messages.size() + " messages");
    JPanel msgPanel = new JPanel();
    for(int i = 0; i < messages.size(); ++i){
      String receiver = messages.get(i).get(0);
      String sender = messages.get(i).get(1);
      String time = messages.get(i).get(2);
      String content = messages.get(i).get(3);
      String all = "<html><body style='width: 500px;'>To: " + receiver +"<br>"
                   + "From: " + sender + "<br>"
                   + time + "<br>"
                   + content + "<br></body></html>";
      final String id = messages.get(i).get(4);
      JLabel tLabel = new JLabel(all);
      msgPanel.add(tLabel);
      JButton delete = new JButton("Delete");
      delete.setSize(200,30);
      delete.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          deleteAction(id, currentUser);
          getContentPane().removeAll();
          getContentPane().repaint();
          viewMsgView(currentUser);
        }
      });
      msgPanel.add(delete);
    }
    getContentPane().add(msgPanel, BorderLayout.CENTER);
    JButton cancel = new JButton("Go Back");
    cancel.setSize(200,30);
    
    //cancel button b1
      cancel.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
            //go back to menu
            getContentPane().removeAll();
            getContentPane().repaint();
            viewMenu(currentUser);
        }
      });
    JPanel end = new JPanel();
    end.add(cancel);
    getContentPane().add(end, BorderLayout.SOUTH);
    pack();
    setVisible(true);
  }
  
  private void viewSearch(final String user, int search_state, final String search){

      JLabel userLabel = new JLabel("User Search");
      final JTextField userField = new JTextField(20);
      userLabel.setBounds(400,400,100,30);
      userField.setBounds(400, 400, 100, 30);
      
      JButton b2 = new JButton("Search");
      JButton b3 = new JButton("Cancel");
      
      b2.setLocation(400,200);
      b2.setSize(200,30);

      b3.setLocation(400,300);
      b3.setSize(200,30);
      
      //search button
      b2.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          List<String> result = ProfNetwork.getUserInfo(esql, userField.getText());
          if(result != null){
            viewSearch(currentUser, 1, result.get(0));
          }
        }
      });
      //cancel search button
      b3.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewMenu(user);
        }
      });
  
      
      ImageIcon icon = new ImageIcon("../images/networklogothing.png");
      JLabel label = new JLabel("User Search", icon, JLabel.LEFT);
      JPanel logo_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      logo_panel.add(label);
      getContentPane().add(logo_panel, BorderLayout.NORTH);
      
      JPanel searchpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      searchpanel.add(userLabel);
      searchpanel.add(userField);
      searchpanel.add(b2);
      searchpanel.add(b3);
      getContentPane().add(searchpanel, BorderLayout.CENTER); 
      
      JPanel results_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      //user results here
      if(search_state == 0)
      {
        JLabel resultsLabel = new JLabel("No user found.");
        //results_panel.add(resultsLabel);
      }
      else if(search_state ==1)
      {
        JLabel resultsLabel = new JLabel("User found.");
        JButton uButton = new JButton(search);
        uButton.setSize(200,300);
        uButton.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){
          getContentPane().removeAll();
          getContentPane().repaint();
          viewProfile(search);
        }
        });
        //display that users information
        results_panel.add(uButton);
      } 
      getContentPane().add(results_panel, BorderLayout.SOUTH);
      
      pack();
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
