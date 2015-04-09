import com.sleepycat.db.*;
import java.io.*;
import java.util.*;

public class myDBtest {

	//File name for the table
	private static final String MY_DB_TABLE = "/tmp/egsmith_db";
	private static final int NUM_RECORDS = 1000;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if( (args[1].equalsIgnoreCase("BTREE")) || (args[1].equalsIgnoreCase("HASH")) || (args[1].equalsIgnoreCase("INDEX"))) {
			String dbType = args[1].toUpperCase();
			createDataBase(dbType);
		}
		else {
			
		}
		
				
	}

	public static void createDataBase(String typeSelection){
		
		if(typeSelection.equals("BTREE")) {
			//Create BTREE type
			try {
				DatabaseConfig dbConfig = new DatabaseConfig();
			    dbConfig.setType(DatabaseType.BTREE);
			    dbConfig.setAllowCreate(true);
			    Database my_table = new Database(MY_DB_TABLE, null, dbConfig);
				
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
