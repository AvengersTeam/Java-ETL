package cl.uchile.datos;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

/**
 * @author Avengers
 * ETL de Corporativo.
 */
public class CorporateETL extends AbstractETL {
	/**
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * 
	 */
	public CorporateETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void parse() throws Exception {
		String id = ""; String tagname;
		String base_uri = "http://datos.uchile.cl/recurso/";
		
		String owlUri = "http://datos.uchile.cl/ontologia/";
		String foafUri = "http://xmlns.com/foaf/0.1/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String dctUri = "http://purl.org.dc/terms/";
		String orgUri = "http://www.w3.org/TR/vocab-org/";
		String rdfsUri = "http://www.w3.org/TR/rdf-schema/";
		
		this.writer.writeStartDocument();
		this.writer.setPrefix("rdf", rdfUri);
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writer.writeNamespace("owl", owlUri);
		this.writer.writeNamespace("foaf", foafUri);
		this.writer.writeNamespace("rdf", rdfUri);
		this.writer.writeNamespace("dct", dctUri);
		this.writer.writeNamespace("org", orgUri);
		this.writer.writeNamespace("rdfs", rdfsUri);
		
		boolean isFirst = true;
		boolean found = false;
		JSONArray jCities = JsonReader.getCitiesArray();
		Object[] aCountries = JsonReader.getCountriesArray();
		Unidecoder ud = new Unidecoder();
		String location = "";
		
		while(this.reader.hasNext()) {
			
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue;
			tagname = this.reader.getName().toString();
			if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
			String attributeValue = this.reader.getAttributeValue("", "tag");
			// we assume that CHARACTERS event comes next always, which could not be true
			this.reader.next();
			
			if (tagname.equals("authorityID")) {
				id = this.reader.getText();
				// We should not close the parent element
				if (!isFirst) this.writer.writeEndElement();
				isFirst = false;
				// Write buffered element, this can be optimized
				this.writer.flush();
				// New guy starts here
				this.writer.setPrefix("owl", owlUri);
				this.writer.writeStartElement(owlUri, "NamedIndividual");
				this.writer.writeAttribute(rdfUri, "about", base_uri + "corporativo/" + id);
			}
			
			if(attributeValue == null) continue;
			
			if(attributeValue.equals("110") && this.reader.getText().contains("|a")) {
				String text = this.reader.getText();
				String[] textArray = text.split("\\|");
				/* corpName guarda corporation name */
				String corpName = "";
				for (int i = 0; i < textArray.length; i++) {
					if (textArray[i].equals("")) continue;
					/* Subcampo a = nombre de corporativo */
					if (textArray[i].substring(0,1).equals("a")) {
						corpName += textArray[i].substring(1);

						/* Buscar el nombre de la localidad en el nombre */
						found = false;
						location = "";
						/* Quitar caracteres especiales */
						textArray[i] = ud.unidecode(textArray[i]);
						/* Se verifica si el nombre de la localidad coincide con uno del arreglo
						 * de ciudades o de paises */
						for(int j = 0; j < jCities.size(); j++){
							String aux = ud.unidecode((String)jCities.get(j));
							if(textArray[i].indexOf(aux) != -1){
								location = aux;
								found = true;
								break;
							}
						}
						if(!found){
							for(int j = 0; j < aCountries.length; j++){
								String aux = ud.unidecode((String)aCountries[j]);
								if(textArray[i].indexOf(aux) != -1){
									location = aux;
									found = true;
									break;
								}
							}
						}
						if(!found){
							System.out.println("Error, corporativo sin localidad id = " + id + ", nombre = " + corpName);
						}
					}
					/* Subcampo t, p, b */
					if (textArray[i].substring(0,1).equals("t") ||
						textArray[i].substring(0,1).equals("p") ||
						textArray[i].substring(0,1).equals("b")) {
						corpName += " " + textArray[i].substring(1);
					}
				}
				if(!corpName.equals("") && found) {
					/* write type */
					this.writer.setPrefix("rdf", rdfUri);
					this.writer.writeEmptyElement(rdfUri, "type");
					this.writer.writeAttribute(rdfUri, "resource", foafUri + "organization");
					this.writer.setPrefix("rdf", rdfUri);
					this.writer.writeEmptyElement(rdfUri, "type");
					this.writer.writeAttribute(rdfUri, "resource", orgUri + "organization");
					
					/* write foaf:name */
					//System.out.println("Name: " + corpName);
					this.writer.setPrefix("foaf", foafUri);
					this.writer.writeStartElement(foafUri, "name");
					this.writer.writeCharacters(corpName);
					this.writer.writeEndElement();
					/* write foaf:label */
					this.writer.setPrefix("foaf", foafUri);
					this.writer.writeStartElement(foafUri, "label");
					this.writer.writeCharacters(corpName);
					this.writer.writeEndElement();
					
					/* write location */
					location = location.replaceAll(" ", "_");
					String locationURI = base_uri + "localidad/" + location;
					//System.out.println("Localidad: " + location);
					this.writer.setPrefix("dct", dctUri);
					this.writer.writeEmptyElement(dctUri, "spatial");
					this.writer.writeAttribute(rdfUri, "resource", locationURI);
				}
			}		
		}
		// end the last guy
		this.writer.writeEndElement();
		// end the rdf descriptions
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();
	}
}
