
import com.sleepycat.db.*;
import java.io.*;
import java.util.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class mydbtest {

	//File name for the table
	private static final String MY_DB_TABLE = "/tmp/egsmith_db";
	private static final int NUM_RECORDS = 1000;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String dbType = args[0].toUpperCase();
		DataBaseType myData;
		if(dbType.equals("BTREE")) {
			myData = new Btree();
		}
		else if(dbType.equals("HASH")) {
			myData = new Hash();
		}
		else if(dbType.equals("HASH") || dbType.equals("INDEXFILE") || dbType.equals("INDEX_FILE")) {
			myData = new IndexFileBtree();
		}
		else {
			System.out.println("Incorrect database type");
			return;
		}
		
	

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
					myData.populate();				
					break;
				case 2:
					myData.retrieveByKey();
					break;
				case 3:
					myData.retrieveByData();
					break;
				case 4:
					myData.retrieveByRange();
					break;
				case 5:
					myData.destroy();
					break;
				case 6:
					myData.destroy();
					System.out.println("Goodbye!");
					return;
			}
			
		}
	}
	
	private static int inputChecker(){
		while(true){
			try{
				Scanner scanner = new Scanner(System.in);
				int choice = scanner.nextInt();
				if(choice <1 || choice > 6){
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
	
	private static void retrieveKey(Database database){
		if (database == null){
			System.out.println("DataBase not created please");
		}
	}
	
}
