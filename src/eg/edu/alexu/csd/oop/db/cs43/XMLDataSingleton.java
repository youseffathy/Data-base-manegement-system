package eg.edu.alexu.csd.oop.db.cs43;

import eg.edu.alexu.csd.oop.db.Database;

public class XMLDataSingleton {
	private static Object[][] xmlData = null;
	private String databasename ;
	private String tableName ;
	private XMLDataSingleton() {
		
	}

	public static Object[][] getInstance() {
		if(xmlData == null) {
		ReadXml  readXml = new ReadXml();
		
		}
		return xmlData;
	}
	public void  setDatabaseName() {
		
	}
	public void setTableName() {
		
	}
	public void Destroy() {
		xmlData = null;
	}

}
