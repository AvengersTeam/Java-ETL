package cl.uchile.datos;

import java.io.FileReader;
import java.util.Collection;

import javax.xml.stream.XMLStreamException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * ETL de localidades
 * 
 * @author Avengers
 */
public class LocationETL extends AbstractETL{

	JSONArray jCities;
	Object[] aCountries;
	
	public LocationETL(String outputFilename) throws Exception {
		super(outputFilename);
		
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
