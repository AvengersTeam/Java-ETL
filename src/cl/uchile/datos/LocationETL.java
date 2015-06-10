package cl.uchile.datos;

/**
 * @author Caro
 * ETL de localidades.
 */
/* Usar json-simple-1.1.1.jar para importar las librerías que siguen */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LocationETL {
	XMLOutputFactory outputFactory;
	XMLStreamWriter writer;
	JSONArray jCities;
	Object[] aCountries;
	
	public LocationETL(String outputFilename) throws XMLStreamException, IOException, ParseException {
		this.outputFactory = XMLOutputFactory.newInstance();
		this.writer = this.outputFactory.createXMLStreamWriter(new FileOutputStream(outputFilename), "UTF8");
		
		JSONParser jparser = new JSONParser();
		Object cities = jparser.parse(new FileReader("localizations/ciudades_chile.json"));
		Object countries = jparser.parse(new FileReader("localizations/paises.json"));
		/* JSONArray de ciudades. */
		jCities = (JSONArray) cities;
		JSONObject jCountries = (JSONObject) countries;
		Collection<Object> cCountries = jCountries.values();
		/* Array de paises. */
		aCountries = cCountries.toArray();
	}
	
	public void parse() throws Exception {
		for(int j = 0; j < jCities.size(); j++){
			/*String aux = unidecode((String)jCities.get(j));
			if(textArray[i].indexOf(aux) != -1){
				location = aux;
				found = true;
				break;
				*/
			}
		}
	}
}
