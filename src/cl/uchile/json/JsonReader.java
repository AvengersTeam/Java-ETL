package cl.uchile.json;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {
	
	static public JSONArray getCitiesArray() throws FileNotFoundException, IOException, ParseException {
		JSONParser jparser = new JSONParser();
		Object cities = jparser.parse(new FileReader("input/localizations/ciudades_chile.json"));
		/* JSONArray de ciudades. */
		JSONArray jCities = (JSONArray) cities;
		return jCities;
	}
	
	static public Object[] getCountriesArray() throws FileNotFoundException, IOException, ParseException {
		JSONParser jparser = new JSONParser();
		Object countries = jparser.parse(new FileReader("input/localizations/paises.json"));		
		JSONObject jCountries = (JSONObject) countries;
		Collection<Object> cCountries = new ArrayList<>(jCountries.size());
	    for(Object o: jCountries.values())
	    	cCountries.add(o);
		/* Array de paises. */
		Object[] aCountries = cCountries.toArray();
		return aCountries;
	}
}
