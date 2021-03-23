package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import org.xml.sax.XMLReader;

import eg.edu.alexu.csd.oop.db.cs43.concreteclass.ExecuteUpdateQueryCommad;

public class Insert implements ExecuteUpdateQueryCommad {
	private File DataBaseFile;
	private String tableName;
	private String[] columnsNames;
	private String[] columnsValues;

	public Insert(String[] columnsValues, File DataBaseFile, String tableName, String[] columnsnames) {
		this.columnsValues = columnsValues;
		this.DataBaseFile = DataBaseFile;
		this.tableName = tableName;
		this.columnsNames = columnsnames;
	}

	public int execute() throws SQLException{
		if(columnsValues.length == 0 ||DataBaseFile == null) {
			return 0;
		}
		File tablefolder = new File(DataBaseFile.getAbsolutePath() + System.getProperty("file.separator") + tableName);
		if (!tablefolder.exists()) {
			// table isn't exist in database
			return 0;
		}

		XSDReader xsdReader = new XSDReader();
		xsdReader.ReadXSD(DataBaseFile.getAbsolutePath() + System.getProperty("file.separator") + tableName
				+ System.getProperty("file.separator") + tableName + ".xsd");

		String[] tableColumnsNames = xsdReader.getColumns();
		String[] alltypes = xsdReader.getTypes();
		ReadXml readXml = new ReadXml();
		File tableXmlFile = new File(DataBaseFile.getAbsolutePath() + System.getProperty("file.separator") + tableName
				+ System.getProperty("file.separator") + tableName + ".xml");

		Object[][] xmlData = readXml.getArray(tableXmlFile);

		if (xmlData == null) {
			xmlData = new Object[0][0];
		}
		Object[][] newXmlData = CopyOldXmlInNewXml(xmlData, columnsValues.length);

		if (tableColumnsNames.length != columnsValues.length) {
			return 0;
		}
		
		for (int i = 0; i < tableColumnsNames.length; ++i) {
			int index = -2;
			if(columnsNames.length == 0) {
			//	System.out.println("length");
				index = i;
			}else {
				
				 index = getIndex(tableColumnsNames[i], columnsNames);// query has column name does not exist
			}
			
			if (index == -1) {// query has column name does not exist
				return 0;
			}
			
			if (alltypes[i].equalsIgnoreCase("string")) {
				if (columnsValues[index].startsWith("'") && columnsValues[index].endsWith("'")) {
					newXmlData[xmlData.length][i] = columnsValues[index];
					
				} else {
					return 0;
				}
			} else if (alltypes[i].equalsIgnoreCase("integer")) {
				if (!columnsValues[index].startsWith("'") || !columnsValues[index].endsWith("'")) {
					try {
						int val = Integer.valueOf(columnsValues[index]);
						newXmlData[xmlData.length][i] = (Object)val;
					} catch (Exception e) {
						return 0;
					}
				} else {
					return 0;
				}
			}
			
		}
		WriteXml writeXml = new WriteXml();
		writeXml.writeTable(newXmlData, tableColumnsNames, tablefolder);
		XmlValidation validation = new XmlValidation();
		try {
			validation.validateXml(tablefolder);

		} catch (Exception e) {

			writeXml.writeTable(xmlData, tableColumnsNames, tablefolder);
		}

		return 1;
	}

	private Object[][] CopyOldXmlInNewXml(Object[][] arr, int columnsNumber) {
		Object[][] newXml = null;
		if (arr.length != 0) {
			newXml = new Object[arr.length + 1][arr[0].length];
			for (int i = 0; i < arr.length; ++i) {
				for (int j = 0; j < arr[0].length; ++j) {
					newXml[i][j] = arr[i][j];

				}
			}
		} else {

			newXml = new Object[1][columnsNumber];
		}
		return newXml;

	}

	private int getIndex(String name, String columnsNames[]) {

		for (int i = 0; i < columnsNames.length; ++i) {
			if (name.equalsIgnoreCase(columnsNames[i])) {
				return i;
			}
		}
		return -1;
	}
}
