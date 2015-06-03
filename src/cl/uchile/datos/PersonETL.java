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
			String str = this.reader.getAttributeValue( "", "tag" );
			this.reader.next();
			id = tagname.equals( "authorityID" ) ? this.reader.getText() : id;
			
			if( str == null ) continue;
			
			if( str.equals( "100" ) ) {
				System.out.println( this.reader.getText() );
			}
			else if( str.equals( "005" ) ) {}
		}
		System.out.println(  "termine"  );
	}

}
