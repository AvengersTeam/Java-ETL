/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

/**
 * @author SISIB
 *
 */
public class Migrator {

	/**
	 * @param args
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
		String filename = "C:/Users/SISIB/Downloads/Autoridades-personales-hasta2005-con-subcampos_2015-04-17.xml";
		PersonETL p = new PersonETL(filename);
		p.parse();
	}

}
