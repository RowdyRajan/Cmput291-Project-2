
import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class myDBtest {

	//File name for the table
	private static final String MY_DB_TABLE = "/tmp/egsmith_db";
	private static final int NUM_RECORDS = 1000;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		while (true) {
			System.out.print("Input a number for one of the following\n"
					+ "1. Create and poulate a database\n"
					+ "2. Retrieve records with a given key\n"
					+ "3. Retrieve records with a given data\n"
					+ "4. Retrieve records with a given range of key values\n"
					+ "5. Destroy the database\n" + "6. Quit\n");
			int choice = inputChecker();
			System.out.println(choice);
			switch (choice) {
			case 1: 
				if( (args[1].equalsIgnoreCase("BTREE")) || (args[1].equalsIgnoreCase("HASH")) || (args[1].equalsIgnoreCase("INDEX"))) {
					String dbType = args[1].toUpperCase();
					createDataBase(dbType);
				}
				else {
					
				}
				break;
			
			
			}
			
		}
	}
	
	private static int inputChecker(){
		while(true){
			try{
				Scanner scanner = new Scanner(System.in);
				int choice = scanner.nextInt();
				if(choice <1 || choice > 5){
					System.out.println("Invalid choice please try again");
					continue;
				}
				return choice;
			}catch(InputMismatchException e){
				System.out.println("Invalid choice please try again");
				continue;
			}
		}
	}

	public static void createDataBase(String typeSelection){
		
		if(typeSelection.equals("BTREE")) {
			//Create BTREE type
			try {
				DatabaseConfig dbConfig = new DatabaseConfig();
			    dbConfig.setType(DatabaseType.BTREE);
			    dbConfig.setAllowCreate(true);
			    Database myTable = new Database(MY_DB_TABLE, null, dbConfig);
				System.out.println(MY_DB_TABLE + "successfully created!")
			    
				myTable.close();
				myTable.remove(MY_DB_TABLE,null,null);
			    
			}
			catch (Exception e1) {
				System.err.println("Create Database Failed" + e1.toString());
			}
					
		}
		
		if(typeSelection.equals("HASH")) {
			//Create HASH type
			try {
				DatabaseConfig dbConfig = new DatabaseConfig();
			    dbConfig.setType(DatabaseType.HASH);
			    dbConfig.setAllowCreate(true);
			    Database myTable = new Database(MY_DB_TABLE, null, dbConfig);
			    System.out.println(MY_DB_TABLE + "successfully created!")
			    
				myTable.close();
				myTable.remove(MY_DB_TABLE,null,null);
				
			}
			catch (Exception e1) {
				System.err.println("Create Database Failed" + e1.toString());
			}
			
		}
		
		if(typeSelection.equals("INDEX")) {
			//Create IndexFile type
		}
	}
	
	
}
