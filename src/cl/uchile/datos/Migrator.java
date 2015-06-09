/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;

/**
 * @author Avengers
 *
 */
public class Migrator {

	/**
	 * @param args
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
		String inputFilename = "input/autoridades.xml";
		String outputFilename = "output/autoridades-personas.rdf";
		PersonETL p = new PersonETL(inputFilename, outputFilename);
		p.parse();
		inputFilename = "input/autoridades-eventos.xml";
		outputFilename = "output/autoridades-eventos.rdf";
		EventETL e = new EventETL(inputFilename, outputFilename);
		e.parse();
	}

}
