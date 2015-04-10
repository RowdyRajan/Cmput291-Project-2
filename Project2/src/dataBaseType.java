import com.sleepycat.db.Database;


public abstract class dataBaseType {
	private Database database;

	public dataBaseType(Database database) {
		super();
		this.database = database;
	}
	
	public abstract void retrieveByKey();
	public abstract void retrieveByData();
	public abstract void retrieveByRange();
	
	
}
