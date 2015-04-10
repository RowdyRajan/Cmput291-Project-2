import java.util.InputMismatchException;
import java.util.Scanner;
import com.sleepycat.db.*;

public class mydbtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Database database;
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

}
