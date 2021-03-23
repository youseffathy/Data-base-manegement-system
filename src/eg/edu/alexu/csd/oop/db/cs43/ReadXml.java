package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class ReadXml {
	public ReadXml() {
	
	}

	public Object[][] getArray(File table) throws SQLException {
		File tableFolder = table.getParentFile();
		
		XSDReader reader = new XSDReader();
	
		reader.ReadXSD(tableFolder.getAbsolutePath() +File.separator+ tableFolder.getName() + ".xsd");
	//	System.out.println(tableFolder.getAbsolutePath() +File.separator+ tableFolder.getName() + ".xsd");
		String[]allTypes = reader.getTypes();
		Object[][] values = null;
		int noOfRows = 0;
		int noOfcolumns = 0;
		
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document ;
			
			document = documentBuilder.parse(table);
		
		

		NodeList nodes = document.getElementsByTagName(table.getName().substring(0, table.getName().length() - 4));
		Node node = nodes.item(0);
		int cout = 0;
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			NodeList rows = document.getElementsByTagName("row");
			if (values == null) {
				noOfRows = rows.getLength();
			}
			if(rows.getLength() == 0) {
				return new Object[0][0];
			}
			for (int i = 0; i < noOfRows; i++) {
				Node row = rows.item(i);
				Node column = row.getFirstChild();
				List<Object> columnList = new LinkedList<>();
				int count = 0;
				while (column != null) {
					
					if(allTypes[count].equals("string")) {
						columnList.add(column.getTextContent());
					}else if(allTypes[count].equals("integer")) {
						columnList.add(Integer.valueOf(column.getTextContent()));
					}
					count++;
					column = column.getNextSibling();
				}
				Object[] columnArray = new Object[columnList.size()];
				columnArray = columnList.toArray(columnArray);
				if(values == null) {
					values = new Object[noOfRows][columnList.size()];
				}
				values[i] = columnArray;
			}
		}
		}catch (Exception e) {
			return null;
		}
		return values;
	}
}
