package main.java.cl.uchile.datos;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import main.java.cl.uchile.elasticsearch.Elastic;
import main.java.cl.uchile.xml.Element;
import main.java.utils.NameParser;
import java.util.logging.Logger;
import main.java.utils.ETLLogger;


/**
 * ETL Personas.
 * 
 * @author Avengers
 */
public class PersonETL extends AbstractETL {
	Logger log;
	/**
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * 
	 */
	public PersonETL(String inputFilename, ArrayList<String> outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
		log = ETLLogger.getLog("log/person.txt");
	}

	/**
	 * {@inheritDoc}
	 * @throws XMLStreamException 
	 */
	public void parseAndWrite() throws XMLStreamException {
		String id = ""; String tagname;
		String base_uri = "http://datos.uchile.cl/recurso/";
		
		String owlUri = "http://datos.uchile.cl/ontologia/";
		String foafUri = "http://xmlns.com/foaf/0.1/";
		String bioUri = "http://vocab.org/bio/0.1/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

		Element personElement = new Element();
		Element eventElement = new Element();
		
		XMLStreamWriter personWriter = this.writers.get(0);
		XMLStreamWriter dateWriter = this.writers.get(1);

		personWriter.writeStartDocument();
		personWriter.setPrefix("rdf", rdfUri);
		personWriter.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		personWriter.writeNamespace("owl", owlUri);
		personWriter.writeNamespace("foaf", foafUri);
		personWriter.writeNamespace("bio", bioUri);
		personWriter.writeNamespace("rdf", rdfUri);
		personWriter.writeNamespace("rdfs", rdfsUri);
		
		dateWriter.writeStartDocument();
		dateWriter.setPrefix("rdf", rdfUri);
		dateWriter.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		dateWriter.writeNamespace("owl", owlUri);
		dateWriter.writeNamespace("bio", bioUri);
		dateWriter.writeNamespace("rdf", rdfUri);
		
		boolean personHasName = false;
		Elastic elastic = new Elastic();
		NameParser parser = new NameParser();
		
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
			
			String attributeValue = this.reader.getAttributeValue("", "tag");
			// we assume that CHARACTERS event comes next always, which could not be true
			this.reader.next();

			if (tagname.equals("authorityID")) {
				//write previous person if it atleast got a name
				if( personHasName ){
					personElement.write(personWriter);
					//elastic.index( personElement, "autoridad" );
					personWriter.flush();
					dateWriter.flush();
				}
				id = this.reader.getText();
				// New guy starts here
				personElement = new Element();
				personElement.setPrefix("owl");
				personElement.setUri(owlUri);
				personElement.setElementName("NamedIndividual");
				personElement.appendAttribute(rdfUri, "about", base_uri + "autoridad/" + id);
				personHasName = false;
			}
			
			if(attributeValue == null) continue;
			
			//alternative names
			if(attributeValue.equals("400")) {
				String text = this.reader.getText();
				text = parser.labelNameParse(text);
				Element alternativeNameElement = new Element();
				alternativeNameElement.setPrefix("rdfs");
				alternativeNameElement.setUri(rdfsUri);
				alternativeNameElement.setElementName("label");
				alternativeNameElement.setText(text);
				personElement.appendElement(alternativeNameElement);
			}

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
						personElement.appendElement(nameElement);
					}
					else if (textArray[i].substring(0,1).equals("d")) {
						Element typeElement, dateElement;
						String[] birthArray = parseDate( textArray[i].substring(1) );
						if( birthArray[0] != "" ) {
							Element birthElement = new Element();
							birthElement.setPrefix("bio");
							birthElement.setUri(bioUri);
							birthElement.setElementName("event");
							birthElement.appendAttribute(rdfUri, "resource",  base_uri + "nacimiento/" + birthArray[0]);
							personElement.appendElement(birthElement);
							
							eventElement = new Element();
							eventElement.setPrefix("owl");
							eventElement.setUri(owlUri);
							eventElement.setElementName("NamedIndividual");
							eventElement.appendAttribute(rdfUri, "about", base_uri + "nacimiento/" + birthArray[0]);
							typeElement = new Element();
							typeElement.setPrefix("rdf");
							typeElement.setUri(rdfUri);
							typeElement.setElementName("type");
							typeElement.appendAttribute(rdfUri, "resource",  bioUri + "birth");
							eventElement.appendElement(typeElement);
							dateElement = new Element();
							dateElement.setPrefix("bio");
							dateElement.setUri(bioUri);
							dateElement.setElementName("date");
							dateElement.setText(birthArray[0]);
							eventElement.appendElement(dateElement);
							eventElement.write(dateWriter);
							}
						if( birthArray[1] != "" ) {
							Element deathElement = new Element();
							deathElement.setPrefix("bio");
							deathElement.setUri(bioUri);
							deathElement.setElementName("event");
							deathElement.appendAttribute(rdfUri, "resource", base_uri + "muerte/" + birthArray[1]);
							personElement.appendElement(deathElement);
							
							eventElement = new Element();
							eventElement.setPrefix("owl");
							eventElement.setUri(owlUri);
							eventElement.setElementName("NamedIndividual");
							eventElement.appendAttribute(rdfUri, "about", base_uri + "muerte/" + birthArray[1]);
							typeElement = new Element();
							typeElement.setPrefix("rdf");
							typeElement.setUri(rdfUri);
							typeElement.setElementName("type");
							typeElement.appendAttribute(rdfUri, "resource",  bioUri + "death");
							eventElement.appendElement(typeElement);
							dateElement = new Element();
							dateElement.setPrefix("bio");
							dateElement.setUri(bioUri);
							dateElement.setElementName("date");
							dateElement.setText(birthArray[1]);
							eventElement.appendElement(dateElement);
							eventElement.write(dateWriter);
						}				
					}
					log.warning("Problema con autoridad:" + id + "," + text);
				}
				personHasName = true;
			}
		}
		//write the last person
		if( personHasName ){
			personElement.write(personWriter);
			//elastic.index( personElement, "autoridad" );
			personWriter.flush();
			dateWriter.flush();
			personHasName = false;
		}

		elastic.close();
		// end the rdf descriptions
		personWriter.writeEndElement();
		personWriter.writeEndDocument();
		personWriter.close();
		
		dateWriter.writeEndElement();
		dateWriter.writeEndDocument();
		dateWriter.close();
	}
	
	
	private String[] parseDate( String str ) {
		String[] res = new String[2]; res[0] = res[1] = "";
		String[] aux = str.split("\\-");
		if( aux.length == 1 ) {
			if( aux[0].startsWith( "n" ) ) {
				res[0] = getNumbers( aux[0] );
			}
			else if( aux[0].startsWith( "m" ) ) {
				res[1] = getNumbers( aux[0] );
			}
		}
		else if( aux.length > 1 ) {
			//supuesto de que son 2 elementos (o tomo los 2 primeros)
			res[0] = getNumbers( aux[0] );
			res[1] = getNumbers( aux[1] );
		}
		return res;
	}
	
	private String getNumbers( String str ) {
		String num = ""; boolean ini = false; boolean isDigit;
		for( Character c : str.toCharArray() ) {
			isDigit = Character.isDigit( c );
			if( ! isDigit && ini ) break;
			if( ! isDigit && ! ini ) continue;
			ini = true;
			num += c;
		}
		return num;
	}
}
