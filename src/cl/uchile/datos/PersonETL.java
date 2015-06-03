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
	public PersonETL( String filename ) throws XMLStreamException, FileNotFoundException {
		super( filename );
		String id = ""; String tagname;
		while( reader.hasNext() ) {
			if( reader.next() != XMLStreamConstants.START_ELEMENT ) continue;
			tagname = reader.getName().toString();
			if( ! tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" ) ) continue;
			String str = reader.getAttributeValue( "", "tag" );
			reader.next();
			id = tagname.equals( "authorityID" ) ? reader.getText() : id;
			if( str == null ) continue;
			
			if( str.equals( "111" ) ) {
				System.out.println( reader.getText() );
			}
			else if( str.equals( "005" ) ) {
				
				
			}
			
			

		}
		System.out.println(  "termine"  );
	}

}
