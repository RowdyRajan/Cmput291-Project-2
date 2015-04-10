import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;
import com.sleepycat.db.*;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Hash implements DataBaseType {

	Database database;
	//File name for the table
	private static final String MY_DB_TABLE = "/tmp/egsmith_db/myTable";
	private static final int NUM_RECORDS = 1000;
	
	@Override
	public void populate() {

		int range;
		DatabaseEntry kdbt, ddbt;
		String s;
		
		try {
			DatabaseConfig dbConfig = new DatabaseConfig();
		    dbConfig.setType(DatabaseType.HASH);
		    dbConfig.setAllowCreate(true);
		    database = new Database(MY_DB_TABLE, null, dbConfig);
 
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
				        // System.out.println(s);	
			
						/* to generate a data string */
						range = 64 + random.nextInt( 64 );
						s = "";
						for ( int j = 0; j < range; j++ ) { 
							s+=(new Character((char)(97+random.nextInt(26)))).toString();
					        // to print out the key/data pair
			                // System.out.println(s);	
			                // System.out.println("");
						}
						System.out.println(s);	
			            System.out.println("");
						
						
						/* to create a DBT for data */
						ddbt = new DatabaseEntry(s.getBytes());
						ddbt.setSize(s.length()); 
			
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

	@Override
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
		
		long startTime = System.currentTimeMillis();
		
		try {
			oprStatus = database.get(null, key,data, LockMode.DEFAULT);
		} catch (DatabaseException e) {
			System.out.println("Error");
			return;
		}
		
		if (oprStatus == OperationStatus.NOTFOUND){
			System.out.println("Key not found");
			return;
		}
		long endTime = System.currentTimeMillis();
		System.out.print("Records found: 1\nExecution time: " + (endTime-startTime) +"ms\n\n");
		String getData = new String(data.getData());
		
		try {
			FileWriter fileWriter = new FileWriter("answers.txt",true);
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
		if(database == null){
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
		
		//Incrementor
		int recordsFound = 0;
		
		long startTime = System.currentTimeMillis();
		
		//Iteratatin throught the database 
		try {
			Cursor cursor = database.openCursor(null, null);
			FileWriter fileWriter = new FileWriter("answers.txt",true);
			BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);
			while(cursor.getNext(key, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
				String dataStringt = new String(value.getData());
				bufferedWriter.write(dataStringt + "\n\n");
				System.out.println(dataStringt);
				//Testing if the data is equal
				if(inputString.equals(new String(value.getData()))){
					recordsFound++;
					
					String keyString = new String(key.getData());
					String dataString = new String(value.getData());
					//bufferedWriter.write(keyString + "\n");
					//bufferedWriter.write(dataString + "\n\n");
				}
			}
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			System.out.println("Error");

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error");

		}
		long endTime = System.currentTimeMillis();

		System.out.println("Records found: " + recordsFound);
		System.out.println("Execution time: " + (endTime - startTime) + "ms");
		
	}

	@Override
	public void retrieveByRange() {
		if(database == null){
			System.out.println("Please populate the database before continung");
			return;
		}
		
		// prompt user for range
		System.out.println("Please enter search range.");
		
		System.out.print("Start: ");
		Scanner s = new Scanner(System.in).useDelimiter(" ");
		String start = s.next();
		DatabaseEntry startKey;
		try {
			startKey = new DatabaseEntry(start.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			System.err.println("Encoding Exception:" + e1.toString());
			return;
		}
				
		System.out.print("End: ");
		s = new Scanner(System.in).useDelimiter(" ");
		String end = s.next();
				
		// Create cursor
		long startTime = System.currentTimeMillis();
		
		try {
			Cursor cursor = database.openCursor(null, null);
			DatabaseEntry Key = new DatabaseEntry();
			DatabaseEntry Data = new DatabaseEntry();
			
			OperationStatus retVal = cursor.getSearchKey(startKey, Data, LockMode.DEFAULT);
			
		    if (retVal == OperationStatus.NOTFOUND) {
		        System.out.println(startKey + " not found in" + database.getDatabaseName());
		        return;
		    }
		    
		    String getData = new String(Data.getData());
		    String keyString =  new String(startKey.getData());
		    
		    /* Append initial key | data pair */
		    FileWriter fileWriter = new FileWriter("answers.txt",true);
		    BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);	
		    
		    
			while(getData.compareTo(end) < 1) {
				
				/* Write each key | data pair in range to file */
				bufferedWriter.write(keyString + "\n");
				bufferedWriter.write(getData + "\n\n");
								
				if(cursor.getNext(Key, Data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
					getData = new String(Data.getData());
					keyString = new String(startKey.getData());
				} else {
					break;
				}
			}
			
			long endTime = System.currentTimeMillis();
			System.out.print("Records found: 1\nExecution time: " + (endTime-startTime) +"ms\n\n");
			bufferedWriter.close();	
			cursor.close();
			
		}
		catch (DatabaseException e) {
			System.err.println("Create Database Failed" + e.toString());
			System.exit(1);	
		}
		catch (IOException e) {	
			e.printStackTrace();
			System.out.println("Writing error");
		} 
		
	}

	@Override
	public void destroy() {
		if(database == null){
			return;
		}
		try {
			database.close();
			database.remove(MY_DB_TABLE, null, null);
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
