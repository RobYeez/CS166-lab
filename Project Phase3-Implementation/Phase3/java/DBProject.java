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
import java.util.Calendar;

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

   public static void addCustomer(DBProject esql){ //1
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

   public static void addRoom(DBProject esql){ //2
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

   public static void addMaintenanceCompany(DBProject esql){ //3

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

   public static void addRepair(DBProject esql){ //4
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

   public static void bookRoom(DBProject esql){ //5
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
                     esqlQuery = "INSERT INTO Booking(bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price) VALUES (" + bID + " , " + customerID + " , " + hotelID + ", " + roomNo + ", \' " + bookingDate + " \' , " + noOfPeople + ", " + price + ") "; //FROM FROM Customer C, Room R WHERE " + hotelID + " = " + R.hotelID + " AND " + roomNo + " = " + R.roomNo + " AND " + customerID + " = " + C.customerID + ";
                     esql.executeUpdate(esqlQuery);
                     break;
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

   public static void assignHouseCleaningToRoom(DBProject esql){ //6
	  		// Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      // Your code goes here.
      int asgID;
		int staffID;
		int hotelID;
		int roomNo;
		
		// get asgID
		while(true) {
			System.out.print("Enter assignment ID: ");
			try {
				asgID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid assignment ID");
				System.out.println(e);
				continue;
			}
		}

		// get staffID
		while(true) {
			System.out.print("Enter staff ID: ");
			try {
				staffID = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid staff ID");
				System.out.println(e);
				continue;
			}
		}
		// get hotelID
		while(true) {
			System.out.print("Enter hotel ID: ");
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
			System.out.print("Enter room number: ");
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

		// insert query
		try {
			String esqlQuery = "INSERT INTO Assigned(asgID, staffID, hotelID, roomNo) VALUES (" + asgID + ", \'" + staffID + "\', \'" + hotelID + "\', \'" + roomNo + "\');";
			esql.executeUpdate(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}

   }//end assignHouseCleaningToRoom
   
   //there is an addRequest ... would you request
   //does repair mean things that have been repaired and then request mean ask to repair?
   public static void repairRequest(DBProject esql){ //7
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
   int reqID;
   int SSN;
   int repairID;
   Date requestDate;
   String description;

   //get reqID
   while(true) {
      System.out.print("Input RequestID: ");
      try {
         reqID = Integer.parseInt(in.readLine());
         break;
      }
      catch(Exception e) {
         System.out.println("Not a valid ReqID");
         System.out.println(e);
         continue;
      }
   }
   //get SSN
   while(true) {
      System.out.print("Input manger ID: ");
      try {
         SSN = Integer.parseInt(in.readLine());
         break;
      }
      catch(Exception e) {
         System.out.println("Not a valid SSN");
         System.out.println(e);
         continue;
      }
   }   
   //get repairID
   while(true) {
      System.out.print("Input RepairID: ");
      try {
         repairID = Integer.parseInt(in.readLine());
         break;
      }
      catch(Exception e) {
         System.out.println("Not a valid RepairID");
         System.out.println(e);
         continue;
      }
   }
   //get date
   while(true) {
      System.out.print("Input Date of Repair: ");
      try {
         SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
         requestDate = dateFormat.parse(in.readLine());
         break;
      }
      catch(Exception e) {
         System.out.println("Not a valid date");
         System.out.println(e);
         continue;
      }
   }
   //get Description
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

  try {
      String esqlQuery = "INSERT INTO Request(reqID, managerID, repairID, requestDate, description) VALUES( " + reqID + ", " + SSN + ", " + repairID + ",\' " + requestDate + " \', \' " + description + " \');";
      esql.executeUpdate(esqlQuery);
   }
   catch(Exception e) {
      System.out.println(e);
   }

   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){ //8
	  // Given a hotelID, get the count of rooms available 
      // Your code goes here.
      int hotelID;

		// get hotelID
		while(true) {
			System.out.print("Enter hotel ID: ");
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
		
		// insert query
		try {
			String esqlQuery = "SELECT COUNT(*) FROM Room r WHERE r.hotelID = " + hotelID + " AND r.roomNo NOT IN (SELECT b.roomNo FROM Booking b WHERE b.hotelID = " + hotelID + ");";
			esql.executeQuery(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
      
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){ //9
	  // Given a hotelID, get the count of rooms booked

      // Your code goes here.
      int hotelID;

		// get hotelID
		while(true) {
			System.out.print("Enter hotel ID: ");
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
		
		// insert query
		try {
			String esqlQuery = "SELECT COUNT(*) FROM Room r WHERE r.hotelID = " + hotelID + " AND r.roomNo IN (SELECT b.roomNo FROM Booking b WHERE b.hotelID = " + hotelID + ");";
			esql.executeQuery(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
      
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){ //10
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      // Your code goes here.
      int hotelID;
		Date inputDate;
		String endDate;		

		// get hotelID
		while(true) {
			System.out.print("Enter hotel ID: ");
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

		// get inputDate and endDate     
      while(true) {
			System.out.print("Enter input date: ");
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
				inputDate = dateFormat.parse(in.readLine());
				
				Calendar c = Calendar.getInstance();
				c.setTime(inputDate);
				c.add(Calendar.DATE, 7);
				endDate = dateFormat.format(c.getTime());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid input date");
				System.out.println(e);
				continue;
			}
		}

		// insert query
		try {
			String esqlQuery = "SELECT r.hotelID, r.roomNo, r.roomType FROM Room r WHERE r.hotelID = '" + hotelID + "' AND r.roomNo NOT IN (SELECT b.roomNo FROM Booking b WHERE b.hotelID = '" + hotelID + "' AND b.bookingDate >= '" + inputDate + "' AND b.bookingDate <= '" + endDate + "');";
			esql.executeQuery(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}

   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){ //11
	  // List Top K Rooms with the highest price for a given date range
      Date date1;
      Date date2;
      int num;

      //date1
      while(true) {
         System.out.print("Input Start Serch Date: ");
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
            date1 = dateFormat.parse(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid date");
            System.out.println(e);
            continue;
         }
      }

      //date2
      while(true) { 
         System.out.print("Input End Search Date: ");
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
            date2 = dateFormat.parse(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid date");
            System.out.println(e);
            continue;
         }
      }
      //top num
      while(true) {
         System.out.print("Input Top Number: ");
         try {
            num = Integer.parseInt(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid RepairID");
            System.out.println(e);
            continue;
         }
      }
      try {
         String esqlQuery = "SELECT B.price, B.roomNo, B.hotelID FROM Booking B WHERE B.bookingDate >= '" + date1 + "' AND B.bookingDate <= '" + date2 + "' ORDER BY B.price DESC LIMIT '" + num + "';";
         esql.executeQuery(esqlQuery);
      }
      catch(Exception e) {
         System.out.println(e);
      }


   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){ //12
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
      String fName;
		String lName;
		int K;

		// get fName
		while(true) {
			System.out.print("Enter customer's first name: ");
			try {
				fName = in.readLine();
				if (fName.length() <= 0 || fName.length() > 30) {
					throw new RuntimeException("First name cannot be 0 letters or longer than 30");
				}
				break;
			}
			catch(Exception e) {
				System.out.println(e);
				continue;
			}
		}

		// get lName
		while(true) {
			System.out.print("Enter customer's last name: ");
			try {
				lName = in.readLine();
				if (lName.length() <= 0 || lName.length() > 30) {
					throw new RuntimeException("Last name cannot be 0 letters or longer than 30");
				}
				break;
			}
			catch(Exception e) {
				System.out.println(e);
				continue;
			}
		}
		
		// get K
		while(true) {
			System.out.print("Enter a K value: ");
			try {
				K = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e) {
				System.out.println("Not a valid K value");
				System.out.println(e);
				continue;
			}
		}

		// insert query
		try {
			String esqlQuery = "SELECT b.price FROM Customer c, Booking b WHERE c.fName = '" + fName + "' AND c.lName = '" + lName + "' AND c.customerID = b.customer ORDER BY b.price DESC LIMIT '" + K + "';";
			esql.executeQuery(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}

   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){ //13
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      //each time customerId pops up, take price and add to total price
      int customerID;
      Date date1;
      Date date2;

      //customerID
      while(true) {
         System.out.print("Input CustomerID: ");
         try {
            customerID = Integer.parseInt(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid RepairID");
            System.out.println(e);
            continue;
         }
      }      

      //date1
      while(true) {
         System.out.print("Input Start Serch Date: ");
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
            date1 = dateFormat.parse(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid date");
            System.out.println(e);
            continue;
         }
      }

      //date2
      while(true) { 
         System.out.print("Input End Search Date: ");
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
            date2 = dateFormat.parse(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid date");
            System.out.println(e);
            continue;
         }
      }

      try{
         String esqlQuery = "SELECT SUM(B.price) FROM Booking B WHERE B.bookingDate >= '" + date1 + "' AND B.bookingDate <= '" + date2 + "' AND B.customer = '" + customerID + "';";
         esql.executeQuery(esqlQuery);
      }
      catch(Exception e) {
         System.out.println(e);
      }

   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){ //14
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
      String name;

		// get maintenance company name
		while(true) {
			System.out.print("Enter maintenance company name: ");
			try {
				name = in.readLine();
				if (name.length() <= 0 || name.length() > 30) {
					throw new RuntimeException("Name cannot be 0 letters or longer than 30");
				}
				break;
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}

		// insert query
		try {
			String esqlQuery = "SELECT rp.rID, rp.hotelID, rp.roomNo, rp.repairType FROM Repair rp, MaintenanceCompany m, Room r WHERE m.name = '" + name + "' AND rp.hotelID = r.hotelID AND rp.roomNo = r.roomNo;";
			esql.executeQuery(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
      
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){ //15
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      int num;
      while(true) {
         System.out.print("Input Top Number: ");
         try {
            num = Integer.parseInt(in.readLine());
            break;
         }
         catch(Exception e) {
            System.out.println("Not a valid RepairID");
            System.out.println(e);
            continue;
         }
      }
// "SELECT B.price FROM Booking B WHERE B.bookingDate >= '" + date1 + "' AND B.bookingDate <= '" + date2 + "' ORDER BY B.price DESC LIMIT '" + num + "';";

      try {
         // String esqlQuery = "SELECT M.name FROM MaintenanceCompany M, Repair R WHERE R.mCompany = M.cmpID ORDER BY (SELECT R1.mCompany, COUNT(*) as c FROM Repair R1, MaintenanceCompany M2 WHERE M2.cmpID = R1.mCompany) DESC LIMIT '" + num + "';";
         String esqlQuery = "SELECT M.name, COUNT(*) FROM MaintenanceCompany M, Repair R WHERE M.cmpID = R.mCompany GROUP BY M.name ORDER BY COUNT(*) DESC LIMIT '" + num + "';";
         esql.executeQuery(esqlQuery);
      }
      catch(Exception e) {
         System.out.println(e);
      }
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){ //16
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
      int hotelID;
		int roomNo;

		// get hotelID
		while(true) {
			System.out.print("Enter hotel ID: ");
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

		// get room No
		while(true) {
			System.out.print("Enter room number: ");
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

		// insert query
		try {
			String esqlQuery = "SELECT EXTRACT (year from r.repairDate) as \"Year\", COUNT(r.rID) FROM Repair r GROUP BY \"Year\" ORDER BY COUNT ASC;";
			esql.executeQuery(esqlQuery);
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}

   }//end listRepairsMade

}//end DBProject
