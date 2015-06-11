package cl.uchile.datos;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import cl.uchile.xml.Element;

/* Usar json-simple-1.1.1.jar para importar las librerías que siguen */
import org.json.simple.JSONArray;

/**
 * ETL Eventos.
 * 
 * @author Avengers
 */
public class EventETL extends AbstractETL {
	
	public EventETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
	}
	
	public void parseAndWrite() throws Exception {
		String id = "";
		String tagname;
		
		Element eventElement = new Element();
		
		this.writer.writeStartDocument();
		this.writer.setPrefix("rdf", rdfUri);
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writer.writeNamespace("owl", owlUri);
		this.writer.writeNamespace("dct", dctUri);
		this.writer.writeNamespace("rdf", rdfUri);
		this.writer.writeNamespace("frbrer", frbrerUri);
		this.writer.writeNamespace("schema", schemaUri);
		
		boolean isFirst = true;
		
		JSONArray jCities = JsonReader.getCitiesArray();
		Object[] aCountries = JsonReader.getCountriesArray();
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
			String attributeValue = this.reader.getAttributeValue("", "tag");
			// we assume that CHARACTERS event comes next always, which could not be true
			this.reader.next();

			if (tagname.equals("authorityID")) {
				id = this.reader.getText();
				System.out.println(id);
				// We should not close the parent element
				if (!isFirst)
					eventElement.write(this.writer);
				isFirst = false;
				// Write buffered element, this can be optimized
				this.writer.flush();
				// New guy starts here
				eventElement = new Element();
				eventElement.setPrefix("owl");
				eventElement.setUri(owlUri);
				eventElement.setElementName("NamedIndividual");
				eventElement.appendAttribute(rdfUri, "about", base_uri + "recurso/evento/" + id);
			}
			
			if(attributeValue == null) continue;
			
			if(attributeValue.equals("111") && this.reader.getText().contains("|a")) {
				String text = this.reader.getText();
				String[] textArray = text.split("\\|");
				int textArrayLength = textArray.length;
				/* Caso nombre */
				String name = "";
				for(int i = 0; i < textArrayLength; i++){
					if(textArray[i].length() == 0)
						continue;
					name = name + textArray[i].substring(1);
				}
				/* Creo elemento nombre como dct:title */
				//System.out.println("Nombre: " + name);
				Element nameElement = new Element();
				nameElement.setPrefix("dct");
				nameElement.setUri(dctUri);
				nameElement.setElementName("title");
				nameElement.setText(name);
				eventElement.appendElement(nameElement);
				/* Caso type */
				Element typeElement = new Element();
				typeElement.setPrefix("rdf");
				typeElement.setUri(rdfUri);
				typeElement.setElementName("type");
				typeElement.appendAttribute(rdfUri, "resource", frbrerUri+"C1009");
				eventElement.appendElement(typeElement);
				/* Según la letra luego de los pipes, creo los siguientes elementos */
				for(int i = 0; i < textArrayLength; i++){
					if (textArray[i].equals("")) continue;
					/* Caso fechas */
					if (textArray[i].substring(0,1).equals("d")) {
						String fecha = textArray[i].substring(1);
						fecha = fecha.replaceAll("[^0-9]", "");
						//System.out.println("Fecha: " + fecha);
						Element dateElement = new Element();
						dateElement.setPrefix("schema");
						dateElement.setUri(schemaUri);
						dateElement.setElementName("startDate");
						dateElement.appendAttribute(rdfUri, "datatype", "http://www.w3.org/2001/XMLSchema#dateTime");
						dateElement.setText(fecha);
						eventElement.appendElement(dateElement);
					}
					/* Caso localidad */
					if (textArray[i].substring(0,1).equals("c")) {
						Unidecoder ud = new Unidecoder();
						boolean found = false;
						String location = "";
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
						/*  */
						if(found){
							location = location.replaceAll(" ", "_");
							String locationURI = base_uri + "localidad/" + location;
							//System.out.println("Localidad: " + location);
							Element spatialElement = new Element();
							spatialElement.setPrefix("dct");
							spatialElement.setUri(dctUri);
							spatialElement.setElementName("spatial");
							spatialElement.appendAttribute(rdfUri, "resource", locationURI);
							eventElement.appendElement(spatialElement);
						}
					}
				}
			}
			if(attributeValue.equals("670") && this.reader.getText().contains("|a")) {
				String alternate = this.reader.getText().substring(2);
				//System.out.println("Alternativo: " + alternate);
				Element altNameElement = new Element();
				altNameElement.setPrefix("dct");
				altNameElement.setUri(dctUri);
				altNameElement.setElementName("alternative");
				altNameElement.setText(alternate);
				eventElement.appendElement(altNameElement);
			}
		}
		
		/* end the last event */
		eventElement.write(this.writer);
		/* end the rdf descriptions */
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();
	}
}
