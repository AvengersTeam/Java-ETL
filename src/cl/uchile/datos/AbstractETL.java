/**
 * 
 */
package cl.uchile.datos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
	XMLStreamReader reader;
	XMLOutputFactory outputFactory;
	XMLStreamWriter writer;
	ArrayList<XMLStreamWriter> writers;
	
	/**
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 * 
	 */
	/* Caso varios outputs */
	public AbstractETL(String inputFilename, ArrayList<String> outputFilenames) throws XMLStreamException, FileNotFoundException {
		this.inputFactory = XMLInputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename), "UTF8");
		this.outputFactory = XMLOutputFactory.newInstance();
		for(String output : outputFilenames) {
			XMLStreamWriter aux = this.outputFactory.createXMLStreamWriter(new FileOutputStream(output), "UTF8");
			this.writers.add(aux);
		}
	}
	/* Caso un solo output */
	public AbstractETL(String inputFilename, String outputFilename) throws XMLStreamException, FileNotFoundException {
		this.inputFactory = XMLInputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename), "UTF8");
		this.outputFactory = XMLOutputFactory.newInstance();
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
	}


	
}
