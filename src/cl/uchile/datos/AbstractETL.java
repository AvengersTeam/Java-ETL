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
	
	XMLInputFactory inputFactory;
	XMLOutputFactory outputFactory;
	XMLStreamReader reader;
	XMLStreamWriter writer;
	
	/**
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public AbstractETL(String filename) throws XMLStreamException, FileNotFoundException {
		this.inputFactory = XMLInputFactory.newInstance();
		this.outputFactory = XMLOutputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename));
		this.writer = this.outputFactory.createXMLStreamWriter(new FileInputStream(outputFilename));
	}

}
