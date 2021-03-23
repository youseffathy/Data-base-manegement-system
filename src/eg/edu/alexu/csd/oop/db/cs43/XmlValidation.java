package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlValidation {
	/*XmlValidation validation = new XmlValidation();
	validation.validateXml(new File("sample"+System.getProperty("file.separator")+"testDB"	+System.getProperty("file.separator")+"table_name1"));
*/ private boolean b = false;
	
	public void validateXml(File tablefolder) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

		try {
			factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(
					tablefolder.getAbsolutePath() + System.getProperty("file.separator") + tablefolder.getName() + ".Xsd") }));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DocumentBuilder builder;
		try {
			
			builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ErrorHandler() {

				@Override
				public void warning(SAXParseException exception) throws SAXException {

				b = true;
					
				}

				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					b = true;

				}

				@Override
				public void error(SAXParseException exception) throws SAXException {
					b = true;

				}
			});
			Document document = builder.parse(new InputSource(
					tablefolder.getAbsolutePath() + System.getProperty("file.separator") +tablefolder.getName() + ".Xml"));
			if(b) {
				throw new SQLException();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
}
