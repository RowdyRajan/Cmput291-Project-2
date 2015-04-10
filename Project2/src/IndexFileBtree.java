import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

import com.sleepycat.db.Cursor;
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


public class IndexFileBtree extends IndexFile implements DataBaseType {

	
	

	@Override
	public void retrieveByRange() {
		if(database == null){
			System.out.println("Please populate the database before continung");
			return;
		}
		
		// prompt user for range
		System.out.println("Please enter search range.");
		
		System.out.print("Start: ");
		Scanner s = new Scanner(System.in);
		String start = s.nextLine();
		System.out.println("You Entered: " + start);
		DatabaseEntry startKey;
		try {
			startKey = new DatabaseEntry(start.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			System.err.println("Encoding Exception:" + e1.toString());
			return;
		}
			
		System.out.print("End: ");
		s = new Scanner(System.in);
		String end = s.nextLine();
		System.out.println("You Entered: " + end);
		
		
		long startTime = System.currentTimeMillis();
		
		try {
			Cursor cursor = database.openCursor(null, null);
			DatabaseEntry Key = new DatabaseEntry();
			DatabaseEntry Data = new DatabaseEntry();
			
			OperationStatus retVal = cursor.getSearchKeyRange(startKey, Data, LockMode.DEFAULT);
			
		    if (retVal == OperationStatus.NOTFOUND) {
		        System.out.println(startKey + " not found in" + database.getDatabaseName());
		        return;
		    }
		    
		    String getData = new String(Data.getData());
		    String keyString =  new String(startKey.getData());
		    
		    /* Append initial key | data pair */
		    FileWriter fileWriter = new FileWriter("answers.txt",true);
		    BufferedWriter bufferedWriter= new BufferedWriter(fileWriter);	
		    int num;
		    
		    if((start.compareTo(end) >= 1) || (keyString.compareTo(end) >= 1)){
		    	num = 0;
		    	keyString = end + "z";
		    } else {
		    	num = 1;
		    }
		    
			while(keyString.compareTo(end) < 1) {

				/* Write each key | data pair in range to file */
				bufferedWriter.write(keyString + "\n");
				bufferedWriter.write(getData + "\n\n");
				
				if(cursor.getNext(Key, Data, LockMode.DEFAULT) != OperationStatus.SUCCESS) {
					break;
				}
				
				keyString = new String(Key.getData());
				getData = new String(Data.getData());
				
				Key = new DatabaseEntry();
				Data = new DatabaseEntry();
				
				num++;	
			}
						
			long endTime = System.currentTimeMillis();
			System.out.print("Records found: " + num + "\nExecution time: " + (endTime-startTime) +"ms\n\n");
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

	
}

