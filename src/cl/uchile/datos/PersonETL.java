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

		boolean bool = false;

		while(reader.hasNext()){
			int event = reader.next();

			switch(event){
			case XMLStreamConstants.START_ELEMENT:
				String str = reader.getAttributeValue("", "tag");
				if(str != null && str.equals("100")) {
					bool = true;
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				if (bool) {
					System.out.println(reader.getText().trim());
					bool = false;
				}
				break;

			case XMLStreamConstants.END_ELEMENT:
				break;

			case XMLStreamConstants.START_DOCUMENT:
				break;

			case XMLStreamConstants.ATTRIBUTE:
				break;
			}
		}
	}

}
