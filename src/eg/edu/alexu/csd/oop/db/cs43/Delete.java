package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import eg.edu.alexu.csd.oop.db.cs43.concreteclass.ExecuteUpdateQueryCommad;

public class Delete implements ExecuteUpdateQueryCommad {
	private File database;
	private String[] columns;
	private String[] conditions;

	private ReadXml readXml;
	private Object[][] Storedvalues;
	private XSDReader reader;
	private String[] allcolumns;
	private String[] allTypes;
	private ConditionsManipulation manipulation;
	private WriteXml writeXml;
	private File tablefolder;
	public Delete(File database, String[] columns, String[] conditions, String tablename) {
		this.columns = columns;
		this.conditions = conditions;
		this.database = database;
		tablefolder = new File(database.getAbsolutePath() + System.getProperty("file.separator")
					+ tablename );
		readXml = new ReadXml();
		try {
			Storedvalues = readXml.getArray(new File(database.getAbsolutePath() + System.getProperty("file.separator")
					+ tablename + System.getProperty("file.separator") + tablename + ".xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		reader = new XSDReader();
		reader.ReadXSD(database.getAbsolutePath() + System.getProperty("file.separator") + tablename
				+ System.getProperty("file.separator") + tablename + ".xsd");
		allcolumns = reader.getColumns();
		allTypes = reader.getTypes();

	}
	@Override
	public int execute() {
		if (conditions == null) {
		//	System.out.println("null");

			writeXml = new WriteXml();
			try {
				writeXml.writeTable(null, allcolumns, tablefolder);
			} catch (Exception e) {
				// e.printStackTrace();
				return 0;
			}
			return Storedvalues.length;

		} else if (conditions != null) {

			List<Object[]> newRows = new LinkedList<>();
			manipulation = new ConditionsManipulation(Storedvalues, conditions, allcolumns, allTypes);

			Object[][] RowsTobeAffected = new Object[0][0];
			try {
				RowsTobeAffected = manipulation.getArrayAfterCondiotions();

			} catch (Exception e) {

			}
			int countAffectedRows = 0;

			for (int i = 0; i < Storedvalues.length; i++) {
				int count = 0;
				for (int k = 0; k < RowsTobeAffected.length; k++) {
					count = 0;

					for (int j = 0; j < Storedvalues[0].length; j++) {
						if (RowsTobeAffected[k][j].equals(Storedvalues[i][j])) {
							count++;

						}
					}
					if (count == Storedvalues[0].length) {
						countAffectedRows++;

						break;
					}
				}
				if (count != Storedvalues[0].length) {
					newRows.add(Storedvalues[i]);
				}
			}

			Object[][] returnedRows;
			try {
				returnedRows = new Object[newRows.size()][newRows.get(0).length];
				returnedRows = newRows.toArray(returnedRows);

			} catch (Exception e1) {
				returnedRows = null;
			}
			writeXml = new WriteXml();
			try {
				writeXml.writeTable(returnedRows, allcolumns, tablefolder);
			} catch (Exception e) {
				return 0;
			}
			return countAffectedRows;
		}
		return 0;

	}
}
