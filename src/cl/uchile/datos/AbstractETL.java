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
 * ETL (Extract-Transform-Load) significa extraer, transformar y cargar.
 * Esta clase extrae datos de alguna fuente (xml, json), los transforma
 * y los carga en algún output especificado.
 * 
 * @author Avengers
 */
public abstract class AbstractETL {

	XMLInputFactory inputFactory;
	XMLOutputFactory outputFactory;
	XMLStreamReader reader;
	XMLStreamWriter writer;

	/**
	 * Constructor ETL abstracto, configura el reader y writer.
	 * 
	 * @throws XMLStreamException Excepción lanzada por falla de la librería StAX.
	 * @throws FileNotFoundException Excepción lanzada al no encontrar el archivo.
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamReader.html">XMLStreamReader</a>
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/javax/xml/stream/XMLStreamWriter.html">XMLStreamWriter</a>
	 */
	public AbstractETL(String inputFilename, String outputFilename) throws XMLStreamException, FileNotFoundException {
		this.inputFactory = XMLInputFactory.newInstance();
		this.outputFactory = XMLOutputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename), "UTF8");
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
	}

	/**
	 * Lee y extrae los datos del input al output especificado.
	 * 
	 * @throws Exception Excepción lanzada en caso de error.
	 */
	public abstract void parse() throws Exception;

}
