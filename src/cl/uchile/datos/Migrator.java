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
		String inputFilename = "C:/Users/Fernando/Desktop/Autoridades-personales-2.xml";
		String outputFilename = "C:/Users/Fernando/Desktop/asd.xml";
		PersonETL p = new PersonETL(inputFilename, outputFilename);
		p.parse();
	}

}
