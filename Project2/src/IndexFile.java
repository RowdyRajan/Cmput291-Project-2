import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;
import com.sleepycat.db.SecondaryConfig;
import com.sleepycat.db.SecondaryCursor;
import com.sleepycat.db.SecondaryDatabase;
import com.sleepycat.db.SecondaryKeyCreator;


public abstract class IndexFile implements DataBaseType {

	protected Database database;
	protected SecondaryDatabase secondaryDatabase;
	
	//File name for the table
	protected static final String MY_DB_TABLE = "/tmp/egsmith_db/myTable";
	protected static final String MY_SDB_TABLE = "/tmp/egsmith_db/mySTable";
	protected static final int NUM_RECORDS = 1000;
	
	@Override
	public void populate() {

		int range;
		DatabaseEntry kdbt, ddbt;
		String s;
		
		try {
			DatabaseConfig dbConfig = new DatabaseConfig();
			SecondaryConfig sdbConfig = new SecondaryConfig();
			
		    dbConfig.setType(DatabaseType.BTREE);
		    dbConfig.setAllowCreate(true);
		    database = new Database(MY_DB_TABLE, null, dbConfig);
		    
		    sdbConfig.setAllowCreate(true);
		    sdbConfig.setType(DatabaseType.BTREE);
		    sdbConfig.setSortedDuplicates(true);
		   
			sdbConfig.setKeyCreator(new SecondaryDatabaseKeyCreator());	
			secondaryDatabase = new SecondaryDatabase("SData", null, database, sdbConfig);	
			System.out.println(MY_DB_TABLE + " successfully created!");

			/*  Generate a random string with the length between 64 and 127,
			 *  inclusive.
			 *
			 *  Seeded once only.
			 *  
			 *  Population maintains nearly the same code as the sample population code
			 */
			Random random = new Random(1000000);

		        try {
		            for (int i = 0; i < NUM_RECORDS; i++) {
		
						/* to generate a key string */
						range = 64 + random.nextInt( 64 );
		            	//range = 2;
						s = "";
						for ( int j = 0; j < range; j++ ) {
						  s+=(new Character((char)(97+random.nextInt(26)))).toString();
						}
						System.out.println(s);	
			            System.out.println("");
			
						/* to create a DBT for key */
						kdbt = new DatabaseEntry(s.getBytes());
						kdbt.setSize(s.length()); 
			
				        // to print out the key/data pair
				        //System.out.println(s);	
		
						/* to generate a data string */
						range = 64 + random.nextInt( 64 );
						//range = 1;
						s = "";
						for ( int j = 0; j < range; j++ ) { 
							s+=(new Character((char)(97+random.nextInt(26)))).toString();
						}
						//System.out.println(s);	
			            //System.out.println("");
						
						/* to create a DBT for data */
						ddbt = new DatabaseEntry(s.getBytes());
						ddbt.setSize(s.length()); 
						System.out.println(new String(ddbt.getData()));
						System.out.println("");
						/* to insert the key/data pair into the database */
			            database.putNoOverwrite(null, kdbt, ddbt);
		            }
		            System.out.println(MY_DB_TABLE + " Populated!");
		        }
		        catch (DatabaseException dbe) {
		            System.err.println("Populate the table: "+dbe.toString());
		            System.exit(1);
		        }		
	    
		}
		catch (Exception e1) {
			System.err.println("Create Database Failed" + e1.toString());
			System.exit(1);
		}	
		
	}
	
	public void retrieveByKey() {
		//If no data base is populated
		if(database == null){
			System.out.println("Please populate the database before continung");
			return;
		}
		System.out.println("Please enter the key you are searching for");
		Scanner scanner = new Scanner(System.in);
		String keyString = scanner.nextLine();
		
		OperationStatus oprStatus;
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		key.setData(keyString.getBytes());
		
		long start = System.currentTimeMillis();

		try {
			oprStatus = database.get(null, key,data, LockMode.DEFAULT);
		} catch (DatabaseException e) {
			System.out.println("Error");
			return;
		}
		
		if (oprStatus == OperationStatus.NOTFOUND){
			System.out.println("Key not found");
			long end = System.currentTimeMillis();
			System.out.print("Execution time: " + (end-start) +"ms\n\n");
			return;
		}
		long end = System.currentTimeMillis();
		System.out.print("Records found: 1\nExecution time: " + (end-start) +"ms\n\n");
		String getData = new String(data.getData());
		
		
		try {
			FileWriter fileWriter = new FileWriter("answers.txt", true);
			BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
			bufferedWriter.write(keyString + "\n");
			bufferedWriter.write(getData + "\n\n");
			bufferedWriter.close();	
		} catch (IOException e) {
			
			e.printStackTrace();
			System.out.println("Writing error");
		}
	}
	
	@Override
	public void retrieveByData() {
		//If no data base is populated
		if(database == null || secondaryDatabase == null){
			System.out.println("Please populate the database before continung");
			return;
		}
		
		//Get the input
		System.out.println("Please enter the data you are searching for");
		Scanner scanner = new Scanner(System.in);
		String inputString = scanner.nextLine();
		
		//Values to look through
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		DatabaseEntry pkey = new DatabaseEntry();
		
		//Incrementing counter
		int recordsFound = 0;
		
		long startTime = System.currentTimeMillis();
		
		//Iterating through the database 
		try {
			//DatabaseEntry databaseEntry = new DatabaseEntry("a".getBytes());
			SecondaryCursor cursor = secondaryDatabase.openSecondaryCursor(null, null);
			key.setData(inputString.getBytes());
			
			//cursor.getSearchKeyRange(databaseEntry, value, LockMode.DEFAULT);
			FileWriter fileWriter = new FileWriter("answers.txt",true);
			BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
			
			OperationStatus oprStatus = cursor.getSearchKey(key,pkey,value, LockMode.DEFAULT);
			if (oprStatus == OperationStatus.NOTFOUND){
				System.out.println("Key not found");
				long endTime = System.currentTimeMillis();
				System.out.println("Execution time: " + (endTime - startTime) + "ms");
				return;
			}
			
			recordsFound++;
			bufferedWriter.write(new String(pkey.getData()) + "\n");
			bufferedWriter.write(new String(value.getData()) + "\n\n");
			pkey = new DatabaseEntry();
			value = new DatabaseEntry();
			key.setData(inputString.getBytes());
			while(cursor.getNextDup(key,pkey,value,LockMode.DEFAULT)==OperationStatus.SUCCESS){
				recordsFound++;
				bufferedWriter.write(new String(pkey.getData()) + "\n");
				bufferedWriter.write(new String(value.getData()) + "\n\n");
				pkey = new DatabaseEntry();
				value = new DatabaseEntry();
				key.setData(inputString.getBytes());
				
			}
			bufferedWriter.close();
			cursor.close();
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			System.out.println("Error");

		} catch (IOException e) {	
			e.printStackTrace();
			System.out.println("Writing error");
		} 		
		
		long endTime = System.currentTimeMillis();

		System.out.println("Records found: " + recordsFound);
		System.out.println("Execution time: " + (endTime - startTime) + "ms");
			
		
	}
	
	@Override
	public void destroy() {
		if(database == null || secondaryDatabase == null){
			return;
		}
		try {
			secondaryDatabase.close();
			database.close();
			database.remove(MY_DB_TABLE, null, null);
			
			File table = new File("/tmp/egsmith_db/myTable");
			table.delete();
			
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

	

