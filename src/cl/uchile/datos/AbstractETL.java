/**
 * 
 */
package cl.uchile.datos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author SISIB
 *
 */
public abstract class AbstractETL {
	
	XMLInputFactory factory;
	XMLStreamReader reader;
	
	/**
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public AbstractETL(String filename) throws XMLStreamException, FileNotFoundException {
		this.factory = XMLInputFactory.newInstance();
		this.reader = factory.createXMLStreamReader(new FileInputStream(filename));
	}

}
