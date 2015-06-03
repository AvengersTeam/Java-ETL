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
	public PersonETL( String filename ) throws FileNotFoundException, XMLStreamException {
		super( filename );
	}

	/**
	 * @throws XMLStreamException 
	 * 
	 */
	public void parse() throws XMLStreamException {
		// TODO Auto-generated method stub
		String id = ""; String tagname;
		while( this.reader.hasNext() ) {
			if ( this.reader.next() != XMLStreamConstants.START_ELEMENT ) continue; 
			tagname = this.reader.getName().toString();
			if( ! tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" ) ) continue;
			String attributeValue = this.reader.getAttributeValue( "", "tag" );
			this.reader.next();
			id = tagname.equals( "authorityID" ) ? this.reader.getText() : id;
			
			if( attributeValue == null ) continue;
			
			if( attributeValue.equals( "100" ) && this.reader.getText().contains("|a") ) {
				String text = this.reader.getText();
				String[] textArray = text.split("\\|");
				for (int i = 0; i < textArray.length; i++) {
					if (textArray[i].equals("")) continue;
					if (textArray[i].substring(0,1).equals("a")) System.out.println( "Nombre: " + textArray[i].substring(1) );
					else if (textArray[i].substring(0,1).equals("d")) System.out.println( "Fecha: " + textArray[i].substring(1) );
				}
			}
		}
	}

}
