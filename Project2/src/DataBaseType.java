import com.sleepycat.db.*;

public interface DataBaseType {

	public void populate();
	public void retrieveByKey();
	public void retrieveByData();
	public void retrieveByRange();
	public void destroy();
}
