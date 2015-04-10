import com.sleepycat.db.Database;


public class Btree implements DataBaseType {
	
	Database database;
	
	@Override
	public void populate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveByKey() {
		if(database == null){
			System.out.println("Please populate the database before continung");
		}
		
	}

	@Override
	public void retrieveByData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveByRange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
