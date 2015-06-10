package cl.uchile.datos;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* Usar json-simple-1.1.1.jar para importar las librer�as que siguen */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * ETL de localidades
 * 
 * @author Avengers
 */
public class LocationETL {
	XMLOutputFactory outputFactory;
	XMLStreamWriter writer;
	JSONArray jCities;
	Object[] aCountries;
	
	String base_uri = "http://datos.uchile.cl/recurso/";
	String owlUri = "http://datos.uchile.cl/ontologia/";
	String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";	
	String dctUri = "http://purl.org/dc/terms/";
	
	public LocationETL(String outputFilename) throws XMLStreamException, IOException, ParseException {
		this.outputFactory = XMLOutputFactory.newInstance();
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
		
		JSONParser jparser = new JSONParser();
		Object cities = jparser.parse(new FileReader("input/localizations/ciudades_chile.json"));
		Object countries = jparser.parse(new FileReader("input/localizations/paises.json"));
		/* JSONArray de ciudades. */
		jCities = (JSONArray) cities;
		JSONObject jCountries = (JSONObject) countries;
		Collection<Object> cCountries = jCountries.values();
		/* Array de paises. */
		aCountries = cCountries.toArray();
	}
	
	public void parse() throws Exception {
		
		this.writer.writeStartDocument();
		this.writer.setPrefix("rdf", rdfUri);
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writer.writeNamespace("rdf", rdfUri);
		this.writer.writeNamespace("dct", dctUri);
		
		Unidecoder ud = new Unidecoder();
		
		/* Recorrer JSONArray de ciudades */
		for(int j = 0; j < jCities.size(); j++){
			String city = ud.unidecode((String)jCities.get(j));
			writeRDFTag(city);			
		}
		/* Recorrer Array de paises */
		for(int j = 0; j < aCountries.length; j++){
			String country = ud.unidecode((String)aCountries[j]);
			writeRDFTag(country);
		}
		
		/* end the rdf descriptions */
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();
	}
	
	private void writeRDFTag(String location_name) throws XMLStreamException {
		this.writer.setPrefix("owl", owlUri);
		this.writer.writeStartElement(owlUri, "NamedIndividual");
		this.writer.writeAttribute(rdfUri, "about", base_uri + "localidad/" + location_name.replace(' ', '_'));
		
		/* type */
		this.writer.writeEmptyElement(rdfUri, "type");
		this.writer.writeAttribute(rdfUri, "resource", dctUri + "location");
		/* nombre */
		this.writer.setPrefix("dct", dctUri);
		this.writer.writeStartElement(dctUri, "title");
		this.writer.writeCharacters(location_name);
		this.writer.writeEndElement();
		this.writer.writeEndElement();
	}
	
}
