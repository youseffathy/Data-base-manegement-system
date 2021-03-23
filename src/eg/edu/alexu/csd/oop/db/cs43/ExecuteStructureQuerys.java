package eg.edu.alexu.csd.oop.db.cs43;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.transform.sax.*;
import javax.print.Doc;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;



public class ExecuteStructureQuerys {
	private final static String NS_PREFIX = "xs:";
	private File dataBaseFile;
	private String tableName;
	private String[] columnsnames;
	private String[] columnsTypes;

	public void setDataBaseFile(File dataBaseFile) {
		this.dataBaseFile = dataBaseFile;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setColumnsnames(String[] columnsnames) {
		this.columnsnames = columnsnames;
	}

	public void setColumnsTypes(String[] columnsTypes) {
		this.columnsTypes = columnsTypes;
	}

	public boolean createDataBase() {
		boolean b = dataBaseFile.mkdirs();

		if (b == false) {
			dropDataBase();
			return dataBaseFile.mkdirs();

		} else {
			return true;
		}

	}

	public boolean dropDataBase() {
		if (dataBaseFile.exists()) {
			try {
				FileUtils.deleteDirectory(dataBaseFile);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return true;
		}
		return false;
	}

	public boolean createTable() throws SQLException {
		if (columnsnames == null || columnsTypes == null) {
			throw new SQLException();
		}
		File tablefolder = new File(dataBaseFile.getAbsolutePath() + System.getProperty("file.separator") + tableName);
		if (tablefolder.exists()) {
			return false;
		}
		tablefolder.mkdir();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.newDocument();
			Element table = d.createElement(tableName);
			d.appendChild(table);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(d);
			File xmlFile = new File(
					tablefolder.getAbsolutePath() + System.getProperty("file.separator") + tableName + ".xml");
			StreamResult streamResult = new StreamResult(xmlFile);
			transformer.transform(source, streamResult);
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createXmlSchema(tablefolder);

		return true;
	}

	public boolean dropTable() {

		File tableFolder = new File(dataBaseFile.getAbsolutePath() + System.getProperty("file.separator") + tableName);
		if (tableFolder.exists()) {
			try {
				FileUtils.deleteDirectory(tableFolder);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return true;
		}
		return false;
	}

	private void createXmlSchema(File tablefolder) {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element schemaRoot = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, NS_PREFIX + "schema");
			doc.appendChild(schemaRoot);

			NameTypeElementMaker elMaker = new NameTypeElementMaker(NS_PREFIX, doc);

			Element Table = elMaker.createElement("element", tableName);
			schemaRoot.appendChild(Table);

			Element complexType = elMaker.createElement("complexType");
			Table.appendChild(complexType);

			Element sequence = elMaker.createElement("sequence");
			complexType.appendChild(sequence);
			Element row = elMaker.createElement("element", "row", null, "unbounded");
			sequence.appendChild(row);
			Element complexType2 = elMaker.createElement("complexType");
			row.appendChild(complexType2);
			Element sequence2 = elMaker.createElement("sequence");
			complexType2.appendChild(sequence2);

			for (int i = 0; i < columnsnames.length; ++i) {
				if (columnsTypes[i].equalsIgnoreCase("int")) {
					Element col1 = elMaker.createElement("element", columnsnames[i], "xs:integer");
					sequence2.appendChild(col1);
				} else if (columnsTypes[i].equalsIgnoreCase("varchar")) {
					Element col1 = elMaker.createElement("element", columnsnames[i], "xs:string");
					sequence2.appendChild(col1);
				}
			}
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource domSource = new DOMSource(doc);
			// to create a file use something like this:
			transformer.transform(domSource, new StreamResult(new File(
					tablefolder.getAbsolutePath() + System.getProperty("file.separator") + tableName + ".Xsd")));

		} catch (FactoryConfigurationError | ParserConfigurationException | TransformerException e) {
			// handle exception

			e.printStackTrace();
		}
	}

	private static class NameTypeElementMaker {
		private String nsPrefix;
		private Document doc;

		public NameTypeElementMaker(String nsPrefix, Document doc) {
			this.nsPrefix = nsPrefix;
			this.doc = doc;
		}

		public Element createElement(String elementName, String nameAttrVal, String typeAttrVal, String maxoccurs) {
			Element element = doc.createElementNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, nsPrefix + elementName);
			if (nameAttrVal != null)
				element.setAttribute("name", nameAttrVal);
			if (typeAttrVal != null)
				element.setAttribute("type", typeAttrVal);
			if (maxoccurs != null) {
				element.setAttribute("maxOccurs", maxoccurs);
			}
			return element;
		}

		public Element createElement(String elementName, String nameAttrVal, String typeAttrVal) {

			return createElement(elementName, nameAttrVal, typeAttrVal, null);
		}

		public Element createElement(String elementName, String nameAttrVal) {
			return createElement(elementName, nameAttrVal, null);
		}

		public Element createElement(String elementName) {
			return createElement(elementName, null, null);
		}

	}

}
