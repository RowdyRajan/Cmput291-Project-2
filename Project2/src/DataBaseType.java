import com.sleepycat.db.Database;


public interface DataBaseType {

	public void populate();
	public void retrieveByKey();
	public void retrieveByData();
	public void retrieveByRange();
	public void destroy();
}
