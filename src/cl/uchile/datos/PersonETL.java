/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

/**
 * @author Fernando
 *
 */
public class PersonETL extends AbstractETL {
	
	

	/**
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 * 
	 */
	public PersonETL(String filename) throws XMLStreamException, FileNotFoundException {
		super(filename);
		System.out.println("super");
		
		String tagContent;

		while(reader.hasNext()){
			int event = reader.next();
			
			boolean isMarcEntry = false;

			switch(event){
			case XMLStreamConstants.START_ELEMENT: 
				break;

			case XMLStreamConstants.CHARACTERS:
				System.out.println(reader.getText().trim());
				break;

			case XMLStreamConstants.END_ELEMENT:
				break;

			case XMLStreamConstants.START_DOCUMENT:
				break;
			}

		}
	}

}
