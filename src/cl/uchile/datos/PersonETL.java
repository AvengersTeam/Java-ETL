package cl.uchile.datos;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import cl.uchile.xml.Element;

/**
 * ETL Personas.
 * 
 * @author Avengers
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
	 * {@inheritDoc} asdf
	 * @throws XMLStreamException 
	 */
	public void parse() throws XMLStreamException {
		String id = ""; String tagname;
		String base_uri = "http://datos.uchile.cl/recurso/";
		
		String owlUri = "http://datos.uchile.cl/ontologia/";
		String foafUri = "http://xmlns.com/foaf/0.1/";
		String bioUri = "http://vocab.org/bio/0.1/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		
		Element el = new Element();

		this.writer.writeStartDocument();
		this.writer.setPrefix("rdf", rdfUri);
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writer.writeNamespace("owl", owlUri);
		this.writer.writeNamespace("foaf", foafUri);
		this.writer.writeNamespace("bio", bioUri);
		this.writer.writeNamespace("rdf", rdfUri);
		
		boolean isFirst = true;
	
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
			String attributeValue = this.reader.getAttributeValue("", "tag");
			// we assume that CHARACTERS event comes next always, which could not be true
			this.reader.next();

			if (tagname.equals("authorityID")) {
				id = this.reader.getText();
				this.writer.flush();
				if (!isFirst) el.write(writer);
				isFirst = false;
				// New guy starts here
				el = new Element();
				el.setPrefix("owl");
				el.setUri(owlUri);
				el.setElementName("NamedIndividual");
				el.appendAttribute(rdfUri, "about", base_uri + "autoridad/" + id);
			}
			
			if(attributeValue == null) continue;
			
			if(attributeValue.equals("100") && this.reader.getText().contains("|a")) {
				String text = this.reader.getText();
				String[] textArray = text.split("\\|");
				for (int i = 0; i < textArray.length; i++) {
					if (textArray[i].equals("")) continue;
					if (textArray[i].substring(0,1).equals("a")) {
						Element nameElement = new Element();
						nameElement.setPrefix("foaf");
						nameElement.setUri(foafUri);
						nameElement.setElementName("name");
						nameElement.setText(textArray[i].substring(1));
						el.appendElement(nameElement);
					}
					else if (textArray[i].substring(0,1).equals("d")) {
						Element birthElement = new Element();
						String[] birthArray = textArray[i].substring(1).split("\\-");
						birthElement.setPrefix("bio");
						birthElement.setUri(bioUri);
						birthElement.setElementName("event");
						birthElement.appendAttribute(rdfUri, "resource",  base_uri + "nacimiento/" + birthArray[0]);
						el.appendElement(birthElement);
						if (birthArray.length == 2) {
							Element deathElement = new Element();
							deathElement.setPrefix("bio");
							deathElement.setUri(bioUri);
							deathElement.setElementName("event");
							deathElement.appendAttribute(rdfUri, "resource", base_uri + "muerte/" + birthArray[1]);
							el.appendElement(deathElement);
						}
						
					}
				}
			}
		}
		// end the rdf descriptions
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();
	}

}
