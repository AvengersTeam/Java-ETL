package cl.uchile.datos;

/**
 * @author Avengers
 * ETL de localidades.
 */
/* Usar json-simple-1.1.1.jar para importar las librerías que siguen */
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;

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
		jCities = JsonReader.getCitiesArray();
		aCountries = JsonReader.getCountriesArray();
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
