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
 * ETL Abstracto. 
 * Extract-Transform-Load significa extraer, transformar y cargar.
 * Esta clase extrae datos de alguna fuente (xml, json), los transforma
 * y los carga en algún output especificado.
 * 
 * @author Avengers
 */
public abstract class AbstractETL {

	XMLInputFactory inputFactory;
	XMLStreamReader reader;
	XMLOutputFactory outputFactory;
	XMLStreamWriter writer;
	ArrayList<XMLStreamWriter> writers;
	
	String base_uri = "http://datos.uchile.cl/recurso/";
	String owlUri = "http://datos.uchile.cl/ontologia/";
	String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	String dcUri = "http://purl.org/dc/elements/1.1/";
	String dctUri = "http://purl.org/dc/terms/";
	String bioUri = "http://vocab.org/bio/0.1/";
	String frbrerUri = "http://iflastandards.info/ns/fr/frbr/frbrer/";
	String schemaUri = "http://schema.org/";

	/**
	 * Constructor ETL abstracto, configura el reader y writer, escribe en varios outputs.
	 * 
	 * @param inputFilename Path al nombre del input.
	 * @param outputFilenames Lista de paths a los nombres de los outputs.
	 * @throws XMLStreamException Excepción lanzada por falla de la librería StAX.
	 * @throws FileNotFoundException Excepción lanzada al no encontrar el archivo.
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamReader.html">XMLStreamReader</a>
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamWriter.html">XMLStreamWriter</a>
	 */
	public AbstractETL(String inputFilename, ArrayList<String> outputFilenames) throws XMLStreamException, FileNotFoundException {
		this.writers = new ArrayList<>();
		this.inputFactory = XMLInputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename), "UTF8");
		this.outputFactory = XMLOutputFactory.newInstance();
		for(String output : outputFilenames) {
			System.out.println(output);
			XMLStreamWriter aux = this.outputFactory.createXMLStreamWriter(new FileOutputStream(output), "UTF8");
			this.writers.add(aux);
		}
	}

	/**
	 * Constructor ETL abstracto, configura el reader y writer, escribe un único output.
	 *
	 * @param inputFilename Path al nombre del input.
	 * @param outputFilename Path al nombre del output.
	 * @throws XMLStreamException Excepción lanzada por falla de la librería StAX.
	 * @throws FileNotFoundException Excepción lanzada al no encontrar el archivo.
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamReader.html">XMLStreamReader</a>
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamWriter.html">XMLStreamWriter</a>
	 */
	public AbstractETL(String inputFilename, String outputFilename) throws XMLStreamException, FileNotFoundException {
		this.inputFactory = XMLInputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename), "UTF8");
		this.outputFactory = XMLOutputFactory.newInstance();
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
	}
	
	/**
	 * Constructor ETL abstracto, configura el writer, define solo el output.
	 * 
	 * @param outputFilename Path al nombre del output.
	 * @throws XMLStreamException Excepción lanzada por falla de la librería StAX.
	 * @throws Exception Excepción lanzada en caso de error.
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamReader.html">XMLStreamReader</a>
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamWriter.html">XMLStreamWriter</a>
	 */
	public AbstractETL(String outputFilename) throws Exception {
		this.outputFactory = XMLOutputFactory.newInstance();
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
	}

	/**
	 * Lee y extrae los datos del input al output especificado.
	 * 
	 * @throws Exception Excepción lanzada en caso de error.
	 */
	public abstract void parseAndWrite() throws Exception;

}
