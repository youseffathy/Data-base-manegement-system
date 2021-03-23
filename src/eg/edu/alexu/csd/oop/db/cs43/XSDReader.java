package eg.edu.alexu.csd.oop.db.cs43;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.xml.sax.SAXException;

import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.xs.XSImplementationImpl;
import com.sun.org.apache.xerces.internal.xs.XSConstants;
import com.sun.org.apache.xerces.internal.xs.XSLoader;
import com.sun.org.apache.xerces.internal.xs.XSModel;

public class XSDReader {
	private List<String> columns;
	private List<String> types;

	public XSDReader() {
		types = new LinkedList<>();
		columns = new LinkedList<>();
	}

	public String[] getTypes() {
		String [] temp = new String[types.size()];
		temp = types.toArray(temp);
		return temp;
	}

	public String[] getColumns() {
		String [] temp = new String[types.size()];
		temp = columns.toArray(temp);
		return temp;
	}

	public void ReadXSD(String path) {
		File xsd = new File(path);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(xsd);
			NodeList list = document.getElementsByTagName("xs:element");
			for (int i = 2; i < list.getLength(); i++) {
				NamedNodeMap map = list.item(i).getAttributes();
				String column = map.getNamedItem("name").getNodeValue();
				String type = map.getNamedItem("type").getNodeValue();
				type = type.substring(3, type.length());
				columns.add(column);
				types.add(type);
							}

		} catch (Exception e) {

		}

	}

}
