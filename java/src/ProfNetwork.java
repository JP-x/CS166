/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */
 
package CS166;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class ProfNetwork {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of ProfNetwork
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public ProfNetwork (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end ProfNetwork

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
          List<String> record = new ArrayList<String>();
         for (int i=1; i<=numCol; ++i)
            record.add(rs.getString (i));
         result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       if(rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            ProfNetwork.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      ProfNetwork esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the ProfNetwork object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new ProfNetwork (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            
            String authorisedUser = null;
            
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
              boolean usermenu = true;
              int retstate = -1;
              while(usermenu) {
                System.out.println("MAIN MENU");
                System.out.println("---------");
                System.out.println("1. Go to Friends List"); //done
                System.out.println("2. Update Profile"); //done
                System.out.println("3. Send a Message"); //done
                System.out.println("4. Send Friend Request"); //?
                System.out.println("5. Search Profiles"); //done
                System.out.println("6. View Messages"); //done
                System.out.println(".........................");
                System.out.println("8. Change Password"); //done
                System.out.println("9. Log out");
                if(retstate == 9){System.out.println("Password changed!");}
                if(retstate == 10){System.out.println("Password change failed.");}
                switch (readChoice()){
                   case 1: FriendList(esql, authorisedUser); break;
                   case 2: UpdateProfile(esql, authorisedUser); break;
                   case 3: NewMessage(esql, authorisedUser); break;
                   case 4: SendRequest(esql, authorisedUser); break;
                   case 5: SearchProfiles(esql, authorisedUser); break;
                   case 6: ViewMessage(esql, authorisedUser); break;
                   case 8: 
                        retstate = ChangePassword(authorisedUser, esql); 
                    break;
                   case 9: usermenu = false; break;
                   default : System.out.println("Unrecognized choice!"); break;
                }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              Welcome to the Network    	               \n" +
         "*******************************************************\n");
   }//end Greeting

	public static void placeHeader(String header){
			System.out.println(
         "\n\n*******************************************************\n" +
         "              "+ header + "      	               \n" +
         "*******************************************************\n");
	}//end placeHeader
	public static void placeFooter(String footer, int num){
		System.out.println(
				"\n....................................\n"+
				num + ". " + footer);
	}//end placeFooter

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user with privided login, passowrd and phoneNum
    * An empty block and contact list would be generated and associated with a user
    **/
   public static void CreateUser(ProfNetwork esql){
      try{
         System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         System.out.print("\tEnter user email: ");
         String email = in.readLine();

		//Creating empty contact\block lists for a user
		String query = String.format("INSERT INTO USR (userId, password, email) VALUES ('%s','%s','%s')", login, password, email);
         esql.executeUpdate(query);
         System.out.println ("User successfully created!");
      }catch(Exception e){
         System.err.println (e.getMessage ());
      }
   }//end

   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(ProfNetwork esql){
      try{
		 System.out.print("\tEnter user login: ");
         String login = in.readLine();
         System.out.print("\tEnter user password: ");
         String password = in.readLine();
         String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
         int userNum = esql.executeQuery(query);
         if (userNum > 0)
            return login;
         return null;
      }catch(Exception e){
         System.err.println (e.getMessage ());
         return null;
      }
   }//end
   public static String LogIn(ProfNetwork esql, String login, String password){
     try{
       String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", login, password);
       int userNum = esql.executeQuery(query);
       if(userNum > 0){
        return "login";
       }
      return "";
     }catch(Exception e){
        System.err.println(e.getMessage());
        return "";
     }
   }

   public static void FriendList(ProfNetwork esql, String currentUser){
    try{
      placeHeader("Friends List");
      String query = String.format("SELECT * FROM CONNECTION_USR WHERE userId = '%s'", currentUser);
      placeFooter("Go Back",9);
    }catch(Exception e){
      System.err.println(e.getMessage());
    }
   }//end

   public static void SearchProfiles(ProfNetwork esql, String currentUser){
      try{
        List<List<String>> profileResults = new ArrayList<List<String>>();
        placeHeader("Search Profiles");
        while(profileResults.size() <= 0){
          System.out.print("\tSearch ('quit' to return): ");
          String q = in.readLine();
          if(q.equals("quit")){
            return;
          }
          else{
            String query = String.format("SELECT * FROM USR WHERE userId = '%s'", q);
            profileResults = esql.executeQueryAndReturnResult(query);
            if(profileResults.size() <= 0){
              System.out.println("\tNo Results.");            
            }
          }        
        }
        DisplayResults(esql, profileResults);
        ChooseOption(esql, profileResults, profileResults.size(), currentUser);    
      }catch(Exception e){
        System.err.println(e.getMessage());
      }
      return;
   }//end

   public static void DisplayResults(ProfNetwork esql, List<List<String>> results){
      for(int i = 0; i < results.size(); ++i){
		//order: name, password, email
        System.out.println((i+1) + ". " + results.get(i).get(0));
      }
      placeFooter("Go Back", results.size()+1);
   }//end
   
   public static void ChooseOption(ProfNetwork esql, List<List<String>> results, int sentinel, String currentUser){
		while(true){
			int choice = readChoice();
			--choice;
			if(choice >= 0 && choice < results.size()){
				goToProfile(esql, results.get(choice), currentUser);
				break;
			}
			else if(choice == sentinel){
				break;
			}
			else{
				System.out.println("Invalid choice");
			}
		}
	}
	
	public static void goToProfile(ProfNetwork esql, List<String> profileData, String currentUser){
		try{
			placeHeader(profileData.get(0) + "'s Profile");
			System.out.println("\tUsername: "+ profileData.get(0));
			System.out.println("\tEmail: "+ profileData.get(2));
			System.out.println("\tName: "+ profileData.get(3));
			System.out.println("\tBirthday: "+ profileData.get(4));
			//Profile options: add friend, remove friend, send message
			//1. Check if currently friends, within 3 degree rule
			System.out.println("\t1. Add Friend");
			System.out.println("\t2. Remove Friend");
			System.out.println("\t3. Send Message");
			placeFooter("Go Back", 9);
			boolean stay = true;
			while(stay){
				switch(readChoice()){
					case 1: AddFriend(esql, currentUser, profileData.get(0));break;
					case 2: RemoveFriend(esql, currentUser, profileData.get(0));break;
					case 3: SendMessage(esql, currentUser, profileData.get(0)); break;
					case 9: stay = false; break;
				}
			}
			
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
  
   public static void ViewMessage(ProfNetwork esql, String currentUser){
    placeHeader("View Messages");
    System.out.println("\t1. View Sent Messages.");
    System.out.println("\t2. View Received Messages.");
    placeFooter("Go Back", 9);
    boolean stay = true;
    while(stay){
      switch(readChoice()){
        case 1: ViewSent(esql, currentUser); break;
        case 2: ViewReceived(esql, currentUser); break;
        case 9: stay = false; break;
      }
      System.out.println("\t1. View Sent Messages.");
      System.out.println("\t2. View Received Messages.");
      placeFooter("Go Back", 9);
    }
   }
   public static void ViewSent(ProfNetwork esql, String currentUser){
    try{
			String query = String.format("SELECT receiverId, senderId, sendTime, contents  FROM MESSAGE WHERE senderId = '%s' AND (deleteStatus = '0' OR deleteStatus = '2')", currentUser);
			List<List<String>> results = new ArrayList<List<String>>();
      results = esql.executeQueryAndReturnResult(query);
      placeHeader("Sent Messages");
			if(results.size() <= 0){
				System.out.println("You have no sent messages.");
			}
      else{
        for(int i = 0; i < results.size(); ++i){
          System.out.println("To: "+results.get(i).get(0));
          System.out.println("From: "+results.get(i).get(1));
          System.out.println(results.get(i).get(2));
          System.out.println(results.get(i).get(3)); 
        }
      }
		} catch (Exception e){
			System.err.println(e.getMessage());
		}
   }
   public static void ViewReceived(ProfNetwork esql, String currentUser){
    try{
			String query = String.format("SELECT receiverId, senderId, sendTime, contents  FROM MESSAGE WHERE receiverId = '%s' AND (deleteStatus = '0' OR deleteStatus = '2')", currentUser);
			List<List<String>> results = new ArrayList<List<String>>();
      results = esql.executeQueryAndReturnResult(query);
      placeHeader("Received Messages");
			if(results.size() <= 0){
				System.out.println("You have no received messages.");
			}
      else{
        for(int i = 0; i < results.size(); ++i){
          System.out.println("To: "+results.get(i).get(0));
          System.out.println("From: "+results.get(i).get(1));
          System.out.println(results.get(i).get(2));
          System.out.println(results.get(i).get(3)); 
        }
      }
		} catch (Exception e){
			System.err.println(e.getMessage());
		}
   }
   public static void AddFriend(ProfNetwork esql, String sender, String receiver){
   }
   public static void RemoveFriend(ProfNetwork esql, String sender, String receiver){
   }
   public static void UpdateProfile(ProfNetwork esql, String currentUser){
     
   }
   public static void NewMessage(ProfNetwork esql, String currentUser){
     try{
       placeHeader("Send a Message");
       System.out.println("\tWho do you wish to message? ");
       String receiver = in.readLine();
       System.out.println("\tEnter your message: ");
       String msg = in.readLine();
       java.util.Date date = new java.util.Date();
       Timestamp curr = new Timestamp(date.getTime());
       String msgid = msg + currentUser + receiver;
       int msgHash = msgid.hashCode();
       String query =  String.format("INSERT INTO MESSAGE (msgId, senderId, receiverId, contents, sendTime, deleteStatus, status) " + "VALUES("+ msgHash+", '" + currentUser+"', '"+receiver+"', '"+msg+"', '" + curr+ "', 0, 'sent')");
       System.out.println(query);
       esql.executeUpdate(query);
     }catch(Exception e){
      System.err.println(e.getMessage());
     }
   }
   public static void SendMessage(ProfNetwork esql, String sender, String receiver){
   }
   public static void SendRequest(ProfNetwork esql, String currentUser){
   }


   public static int ChangePassword(String current_user, ProfNetwork esql){
       try{
           System.out.print("\tEnter current password: ");
           String old_pwd = in.readLine();
           System.out.print("\tEnter new password: ");
           String new_pwd = in.readLine();
           
           String query = String.format("SELECT * FROM USR WHERE userId = '%s' AND password = '%s'", current_user, old_pwd);
           int valid = esql.executeQuery(query);
           if(valid > 0){
               query = String.format("UPDATE USR SET password = '%s' WHERE userId = '%s' ", new_pwd, current_user);
               System.out.println(valid); 
                esql.executeUpdate(query);
                return 9;
            }
            return 10;
            
       }catch(Exception e){
            System.err.println(e.getMessage() );
            return 10;
       }
   
   }

}//end ProfNetwork
