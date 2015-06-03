/**
 * 
 */
package cl.uchile.datos;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Fernando
 *
 */
public class PersonETL extends AbstractETL {

	/**
	 * 
	 */
	public PersonETL() {
		super();
		
		// Anonymous Sub-classing 
		this.handler = new DefaultHandler() {

			boolean bmentry = false;
			
			// Here we make our buffer, somehow we need to write this into an xml file
			// We cant just build the entire tree in memory
			// TODO: investigate how to write large XML file
			
			// Maybe we use this?
			// StringBuffer elementContent = new StringBuffer();
			

			public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

				//Here we say when to read a value
				if (qName.equalsIgnoreCase("MARCENTRY") && attributes.getValue("tag").equals("100")) {
					bmentry = true;
					System.out.println(qName);
				}

			}

			public void endElement(String uri, String localName, String qName) throws SAXException {

			}

			public void characters(char ch[], int start, int length) throws SAXException {

				// TODO: filter this shit
				if (bmentry) {
					System.out.println("A marc entry : " + new String(ch, start, length));
					bmentry = false;
					// here this content should be writen in an XML well formed document
					//
				}

			}

		};
	}

}
