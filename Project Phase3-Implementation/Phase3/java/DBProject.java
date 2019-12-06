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


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.management.RuntimeErrorException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

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
   }//end DBProject

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
   public int executeQuery (String query) throws SQLException {
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
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;
				   case 5: bookRoom(esql); break;
				   case 6: assignHouseCleaningToRoom(esql); break;
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
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
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

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

   enum Gender {
      Male,
      Female,
      Other
   }

   public static void addCustomer(DBProject esql){
	  // Given customer details add the customer in the DB 
      // Your code goes here.
      int customerID;
      String fname;
      String lname;
      String address;
      int phoneNum;
      Date dob;
      String gender;

      //get customerID
      while(true) {
      System.out.print("Input Customer ID: ");
         try {
            customerID = Integer.parseInt(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid Customer ID");
            System.out.println(e);
            continue;
         }
      }
      //get first name
      while(true) {
      System.out.print("Input First Name: ");
         try {
            fname = in.readLine();
            if (fname.length() <= 0 || fname.length() > 30) {
               throw new RuntimeException("First name cannot be 0 letters or longer than 30");
            }
            break;
         }
         catch(Exception e) {
            System.out.println(e);
            continue;
         }
      }
      //get last name
      while(true) {
      System.out.print("Input Last Name: ");
         try {
            lname = in.readLine();
            if (lname.length() <= 0 || lname.length() > 30) {
               throw new RuntimeException("Last name cannot be 0 letters or longer than 30");
            }
            break;
         }
         catch(Exception e) {
            System.out.println(e);
            continue;
         }
      }
      //get address
      while(true) {
      System.out.print("Input Address: ");
         try {
            address = in.readLine();
            if (address.length() <= 0 || address.length() > 30) {
               throw new RuntimeException("Address cannot be 0 letters or longer than 30");
            }
            break;
         }
         catch(Exception e) {
            System.out.println(e);
            continue;
         }
      }
      //get phone number
      while(true) {
      System.out.print("Input Phone Number: ");
         try {
            phoneNum = Integer.parseInt(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid Phone number");
            System.out.println(e);
            continue;
         }
      }
      //get DOB
      while(true) {
         System.out.print("Input DOB: ");
            try {
               SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
               dob = dateFormat.parse(in.readLine());
               break;
            }
            catch(Exception e) {
               System.out.println("Not a valid DOB");
               System.out.println(e);
               continue;
            }
         }
      //get gender
      while(true) {
      System.out.print("Input Gender: ");
         try {
            gender = in.readLine();
            if (!gender.equals("Male") && !gender.equals("Female") && !gender.equals("Other")) {
               throw new RuntimeException("Gender can only be Male, Female, or Other");
            }
            break;
         }
         catch(Exception e) {
            System.out.println(e);
            continue;
         }
      }
      //we have all the inputs ... need to insert into query now   
      try {
         String esqlQuery = "INSERT INTO Customer(customerID, fname, lName, Address, phNo, DOB, gender) VALUES (" + customerID + ", \'"  + fname + "\', \'"  + lname + "\', \'" + address + "\', \'" + phoneNum + "\', \'" + dob + "\', \'" + gender + "\' );";
         esql.executeUpdate(esqlQuery);
      }   
      catch(Exception e) {
         System.err.println(e.getMessage());  
      }
   }//end addCustomer

   public static void addRoom(DBProject esql){
	   	// Given room details add the room in the DB
      // Your code goes here.
      int hotelID;
      int roomNo;
      String roomType;
      
      // get hotelID
      while(true) {
         System.out.print("Input Hotel ID: ");
	 		try {
	 			hotelID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid hotel ID");
				System.out.println(e);
				continue;
			}
		}

		// get roomNo
		while(true) {
			System.out.print("Input room number: ");
			try {
				roomNo = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid room number");
				System.out.println(e);
				continue;
			}
		}

		// get roomType
		while(true) {
			System.out.print("Input room type: ");
			try {
				roomType = in.readLine();
				if (roomType.length() <= 0 || roomType.length() > 10) {
					throw new RuntimeException("Room Type cannot be 0 letters or longer than 10");
				}
				break;
			}
			catch(Exception e) {
				System.out.println(e);
				continue;
			}
		}

		// insert query
		try {
			String esqlQuery = "INSERT INTO Room (hotelID, roomNo, roomType) VALUES (" + hotelID + ", \'" + roomNo + "\', \'" + roomType + "\')";
			esql.executeUpdate(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){

      // Given maintenance Company details add the maintenance company in the DB
      int cmpID;
      String name;
      String address;
      String isCertified;

      //get company id
      while(true) {
         System.out.print("Input Company ID: ");
            try {
               cmpID = Integer.parseInt(in.readLine());
               break;
            }
            catch(Exception e) {
               System.out.println("Not a valid Company ID");
               System.out.println(e);
               continue;
            }
      }
      //get company name
      while(true) {
         System.out.print("Input Company Name: ");
            try {
               name = in.readLine();
               if (name.length() <= 0 || name.length() > 30) {
                  throw new RuntimeException("Company Name cannot be 0 letters or longer than 30");
               }
               break;
            }
            catch(Exception e) {
               System.out.println(e);
               continue;
            }
      }
      //get address
      while(true) {
         System.out.print("Input Address: ");
            try {
               address = in.readLine();
               if (address.length() <= 0 || address.length() > 30) {
                  throw new RuntimeException("Address cannot be 0 letters or longer than 30");
               }
               break;
            }
            catch(Exception e) {
               System.out.println(e);
               continue;
            }
      }
      //get isCertified
      while(true) {
         System.out.print("Is company certified?");
         try {
            isCertified = in.readLine();
            if(!isCertified.equals("TRUE") && !isCertified.equals("FALSE")) {
               throw new RuntimeException("Either TRUE or FALSE");
            }
            break;
         }
         catch(Exception e) {
            System.out.println(e);
            continue;
         }
      }
      try {
         String esqlQuery = "INSERT INTO MaintenanceCompany(cmpID, name, address, isCertified) VALUES (" + cmpID + " , \' " + name + " \' , \' " + address + " \', " + isCertified + " );";
         esql.executeUpdate(esqlQuery);
      }   
      catch(Exception e) {
         System.err.println(e.getMessage());  
      }

      

      	// Given maintenance Company details add the maintenance company in the DB
      // ...
      // .
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
	   	// Given repair details add repair in the DB
      // Your code goes here.
      int rID;
      int hotelID;
      int roomNo;
      int mCompany;
      Date repairDate;
      String description;
      String repairType;
		
		// get rID
		while(true) {
			System.out.print("Input repair ID: ");
			try {
				rID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid repair ID");
				System.out.println(e);
				continue;
			}
		}
		
		// get hotelID
		while(true) {
			System.out.print("Input hotel ID: ");
			try {
				hotelID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid hotel ID");
				System.out.println(e);
				continue;
			}
		}

		// get roomNo
		while(true) {
			System.out.print("Input room number: ");
			try {
				roomNo = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid room number");
				System.out.println(e);
				continue;
			}
		}

		// get mCompany
		while(true) {
			System.out.print("Input maintenance company ID: ");
			try {
				mCompany = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid maintenance company ID");
				System.out.println(e);
				continue;
			}
		}

		// get repairDate
		while(true) {
			System.out.print("Input repair date: ");
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
				repairDate = dateFormat.parse(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid repair date");
				System.out.println(e);
				continue;
			}
		}

		// get description
		while(true) {
			System.out.print("Input repair description: ");
			try {
				description = in.readLine();
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid description");
				System.out.println(e);
				continue;
			}
		}

		// get repairType
		while(true) {
			System.out.print("Input repair type: ");
			try {
				repairType = in.readLine();
				if (repairType.length() <= 0 || repairType.length() > 10) {
					throw new RuntimeException("Repair type cannot be 0 letters or longer than 10");
				}
				break;
			}
			catch(Exception e) {
				System.out.println(e);
				continue;
			}
		}

		// insert query
		try {
			String esqlQuery = "INSERT INTO Repair(rID, hotelID, roomNo, mCompany, repairDate, description, repairType) VALUES (" + rID + ", \'" + hotelID + "\', \'" + roomNo + "\', \'" + mCompany + "\', \'" + repairDate + "\', \'" + description + "\', \'" + repairType + "\');";
			esql.executeUpdate(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}

   }//end addRepair

   public static void bookRoom(DBProject esql){
	   	// Given hotelID, roomNo and customer Name create a booking in the DB 
      int hotelID;
      int roomNo;
      int customerID;
      Date bookingDate;

      //get hotelID
      while(true) {
			System.out.print("Input Hotel ID: ");
			try {
				hotelID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid Hotel ID");
				System.out.println(e);
				continue;
			}
      }
      //get roomNo
      while(true) {
			System.out.print("Input Room Number: ");
			try {
				roomNo = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid Room Number");
				System.out.println(e);
				continue;
			}
      }
      //get customer name
      while(true) {
			System.out.print("Input Customer ID: ");
			try {
				customerID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid Customer ID");
				System.out.println(e);
				continue;
			}
      }
      //get booking Date
      while(true) {
			System.out.print("Input Date of Booking: ");
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
				bookingDate = dateFormat.parse(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid date");
				System.out.println(e);
				continue;
			}
		}
      //book room ... should check if room is open first ... and then if it is open then book
      try {
         String esqlQuery = "SELECT bookingDate, roomNo, hotelID FROM Booking WHERE hotelID =  " + hotelID + "  AND roomNo = " + roomNo + " AND bookingDate = \'" + bookingDate + "\'\n ;";
         String input;
         if (esql.executeQuery(esqlQuery) == 0) {
            while(true) {
               System.out.println("Room is open, would you like to reserve?");
               input = in.readLine();
               if (input.equals("Y")) {
                  //need new info to put in... bID, etc.
                  //bookingID
                  int bID;
                  int noOfPeople;
                  int price;
                  //get bID
                  while(true) {
                     System.out.print("Input Booking ID: ");
                     try {
                        bID = Integer.parseInt(in.readLine());
                        break;
                     }
                     catch(Exception e) {
                        System.out.println("Not a valid Booking ID");
                        System.out.println(e);
                        continue;
                     }
                  }
                  //get noOfPeople
                  while(true) {
                     System.out.print("Input Number of People: ");
                     try {
                        noOfPeople = Integer.parseInt(in.readLine());
                        break;
                     }
                     catch(Exception e) {
                        System.out.println("Not a valid Number of People");
                        System.out.println(e);
                        continue;
                     }
                  }
                  //get price? not sure why it is user inputted...lmao
                  while(true) {
                     System.out.print("Input Price: ");
                     try {
                        price = Integer.parseInt(in.readLine());
                        break;
                     }
                     catch(Exception e) {
                        System.out.println("Not a valid Price");
                        System.out.println(e);
                        continue;
                     }
                  }
                  //insert into Booking
                  try {
                     esqlQuery = "INSERT INTO Booking(bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price) VALUES (" + bID + " , " + customerID + " , " + hotelID + ", " + roomNo + ", \' " + bookingDate + " \' , " + noOfPeople + ", " + price + ");";
                     esql.executeUpdate(esqlQuery);
                  }
                  catch(Exception e) {
                     System.err.println(e.getMessage());
                  }
               }
               else if (input.equals("N")) {
                  System.out.println("Room booking canceled");
                  break;
               }
               else {
                  System.out.println("Invalid input");
               }    
            }
         } 
      }
      catch(Exception e) {
         System.out.println(e);
      }  
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
	  		// Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      // Your code goes here.
      // ...
      // ...
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
      // Your code goes here.
      // ...
      // ...
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms available 
      // Your code goes here.
      // ...
      // ...
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms booked
      // Your code goes here.
      // ...
      // ...
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      // Your code goes here.
      // ...
      // ...
   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      // Your code goes here.
      // ...
      // ...
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
      // ...
      // ...
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      // Your code goes here.
      // ...
      // ...
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
      // ...
      // ...
   }//end listRepairsMade

}//end DBProject
