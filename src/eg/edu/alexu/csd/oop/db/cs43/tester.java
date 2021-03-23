package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.XMLConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.cs43.concreteclass.MyDatabase;

public class tester {




	public static void main(String[] args) throws SQLException {
		Database database = MyDatabase.getInstance();
		database.createDatabase("sample" + System.getProperty("file.separator") + "testDB"
				+ System.getProperty("file.separator") + "table_name1", false);


		int s=database.executeUpdateQuery("delete from table_name1 where column_name1 = 'zw' or column_name2 =  88" );

		System.out.println(s);

	}

}