package cl.uchile.datos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.json.simple.JSONArray;
import utils.Unidecoder;
import cl.uchile.json.JsonReader;

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
	String inputFN = "";
	public CorporateETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
		inputFN = inputFilename;
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	public void parseAndWrite() throws Exception {
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
		boolean locationFound = false;
		/* Arreglos de localidades leidas desde los archivos json */
		JSONArray jCities = JsonReader.getCitiesArray();
		Object[] aCountries = JsonReader.getCountriesArray();
		Unidecoder ud = new Unidecoder();
		String location = "";
		HashMap<String, String> nameWithId = new HashMap<String, String>();
		/* Dos iteraciones, una para llenar el diccionario, otra para crear el rdf */
		for (int k = 0; k < 2; k++) {
			if(k == 1) this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFN), "UTF8");
			while(this.reader.hasNext()) {
				if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue;
				tagname = this.reader.getName().toString();
				if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
				String attributeValue = this.reader.getAttributeValue("", "tag");
				// we assume that CHARACTERS event comes next always, which could not be true
				this.reader.next();
				
				if (tagname.equals("authorityID")) {
					id = this.reader.getText();
					if(k == 1){
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
							locationFound = false;
							location = "";
							/* Quitar caracteres especiales */
							textArray[i] = ud.unidecode(textArray[i]);
							/* Se verifica si el nombre de la localidad coincide con uno del arreglo
							 * de ciudades o de paises */
							for(int j = 0; j < jCities.size(); j++){
								String aux = ud.unidecode((String)jCities.get(j));
								if(textArray[i].indexOf(aux) != -1){
									location = aux;
									locationFound = true;
									break;
								}
							}
							if(!locationFound){
								for(int j = 0; j < aCountries.length; j++){
									String aux = ud.unidecode((String)aCountries[j]);
									if(textArray[i].indexOf(aux) != -1){
										location = aux;
										locationFound = true;
										break;
									}
								}
							}
							if(!locationFound){
								//System.out.println("Error, corporativo sin localidad id = " + id + ", nombre = " + corpName);
							}
						}
						/* Subcampo t, p, b */
						if (textArray[i].substring(0,1).equals("t") ||
							textArray[i].substring(0,1).equals("p") ||
							textArray[i].substring(0,1).equals("b")) {
							corpName += " " + textArray[i].substring(1);
						}
					}
					if(!corpName.equals("")) {
						if (k == 1) {
							//System.out.println("entraaaaaaaaaaaaaa");
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
							if (locationFound) {
								/* write location */
								location = location.replaceAll(" ", "_");
								String locationURI = base_uri + "localidad/" + location;
								//System.out.println("Localidad: " + location);
								this.writer.setPrefix("dct", dctUri);
								this.writer.writeEmptyElement(dctUri, "spatial");
								this.writer.writeAttribute(rdfUri, "resource", locationURI);
							}
							//this.writer.writeEndElement();
						}
						else {
							nameWithId.put(ud.unidecode(corpName).replace(' ','_'), id);	
						}
					}
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
