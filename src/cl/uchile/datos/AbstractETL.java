/**
 * 
 */
package cl.uchile.datos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author Avengers
 *
 */
public abstract class AbstractETL {
	
	XMLInputFactory inputFactory;
	XMLOutputFactory outputFactory;
	XMLStreamReader reader;
	XMLStreamWriter writer;
	
	/**
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public AbstractETL(String inputFilename, String outputFilename) throws XMLStreamException, FileNotFoundException {
		this.inputFactory = XMLInputFactory.newInstance();
		this.outputFactory = XMLOutputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename), "UTF8");
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
	}

	
}
