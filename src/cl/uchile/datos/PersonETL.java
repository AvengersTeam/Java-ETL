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
	public PersonETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
	}

	/**
	 * @throws XMLStreamException 
	 * 
	 */
	public void parse() throws XMLStreamException {
		String id = ""; String tagname;
		String base_uri = "http://datos.uchile.cl/recurso/";
		
		String owlUri = "http://datos.uchile.cl/ontologia/";
		String foafUri = "http://xmlns.com/foaf/0.1/";
		String bioUri = "http://vocab.org/bio/0.1/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

		this.writer.writeStartDocument();
		this.writer.setPrefix("rdf", rdfUri);
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writer.writeNamespace("owl", owlUri);
		this.writer.writeNamespace("foaf", foafUri);
		this.writer.writeNamespace("bio", bioUri);
		this.writer.writeNamespace("rdf", rdfUri);
	
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
			String attributeValue = this.reader.getAttributeValue("", "tag");
			// we assume that CHARACTERS event comes next always, which could not be true
			this.reader.next();

			if (tagname.equals("authorityID")) {
				id = this.reader.getText()
				// Write buffered element, this can be optimized
				this.writer.writeEndElement();
				this.writer.flush();
				// New guy starts here
				this.writer.setPrefix("owl", owlUri);
				this.writer.writeStartElement(owlUri, "NamedIndividual");
				this.writer.writeAttribute(rdfUri, "about", base_uri + id);
			}
			
			if(attributeValue == null) continue;
			
			if(attributeValue.equals("100") && this.reader.getText().contains("|a")) {
				String text = this.reader.getText();
				String[] textArray = text.split("\\|");
				for (int i = 0; i < textArray.length; i++) {
					if (textArray[i].equals("")) continue;
					if (textArray[i].substring(0,1).equals("a")) {
						System.out.println("Nombre: " + textArray[i].substring(1));
						this.writer.setPrefix("foaf", foafUri);
						this.writer.writeStartElement(foafUri, "name");
						this.writer.writeCharacters(textArray[i].substring(1));
						this.writer.writeEndElement();
					}
					else if (textArray[i].substring(0,1).equals("d")) {
						System.out.println("Fecha: " + textArray[i].substring(1));
					}
				}
			}
		}
		
		writer.writeEndElement();
		writer.writeEndDocument();

	}

}
