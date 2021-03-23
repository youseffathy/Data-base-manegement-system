package eg.edu.alexu.csd.oop.db.cs43;

import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.cs43.commandConcreteClasses.ClientCommand;
import eg.edu.alexu.csd.oop.db.cs43.concreteclass.CommandFactory;
import eg.edu.alexu.csd.oop.db.cs43.concreteclass.MyDatabase;

public class Testing {

	public static void main(String[] args) {

		Database database = MyDatabase.getInstance();
		database.createDatabase("sample" + System.getProperty("file.separator") + "testDB"
				, false);
		
		try {
			database.executeStructureQuery("CREATE TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)");
		
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String s = "UPDATE table_namE1 SET column_name1='11111111', COLUMN_NAME2=22222222, column_name3='333333333'";
		try {
			System.out.println(database.executeUpdateQuery(s));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
