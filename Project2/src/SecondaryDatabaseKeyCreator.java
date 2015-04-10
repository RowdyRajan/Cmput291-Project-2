import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.SecondaryDatabase;
import com.sleepycat.db.SecondaryKeyCreator;


class SecondaryDatabaseKeyCreator implements SecondaryKeyCreator{

	@Override
	public boolean createSecondaryKey(SecondaryDatabase sdb,
			DatabaseEntry key, DatabaseEntry value, DatabaseEntry skey)
			throws DatabaseException {
		
		skey.setData(value.getData());
		
		return true;
	}
	
}
