package main.java.cl.uchile.datos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.json.simple.JSONArray;

import main.java.utils.CorporateMemberInfo;
import main.java.utils.CorporateMemberList;
import main.java.utils.Unidecoder;
import main.java.cl.uchile.json.JsonReader;

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
		CorporateMemberList corpList = new CorporateMemberList();
		boolean corpListFull = false;
		
		/* Dos iteraciones, una para llenar el HashMap, otra para crear el rdf */
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
					String nameText = this.reader.getText();
					String[] nameTextArray = nameText.split("\\|");
					/* corpName guarda corporation name */
					String corpName = "";
					for (int i = 0; i < nameTextArray.length; i++) {
						if (nameTextArray [i].equals("")) continue;
						
						/* Subcampo a = nombre de corporativo */
						if (nameTextArray [i].substring(0,1).equals("a")) {
							corpName += nameTextArray [i].substring(1);

							/* Buscar el nombre de la localidad en el nombre */
							locationFound = false;
							location = "";
							/* Quitar caracteres especiales */
							nameTextArray [i] = ud.unidecode(nameTextArray [i]);
							/* Se verifica si el nombre de la localidad coincide con uno del arreglo
							 * de ciudades o de paises */
							for(int j = 0; j < jCities.size(); j++){
								String aux = ud.unidecode((String)jCities.get(j));
								if(nameTextArray [i].indexOf(aux) != -1){
									location = aux;
									locationFound = true;
									break;
								}
							}
							if(!locationFound){
								for(int j = 0; j < aCountries.length; j++){
									String aux = ud.unidecode((String)aCountries[j]);
									if(nameTextArray [i].indexOf(aux) != -1){
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
						if (nameTextArray [i].substring(0,1).equals("t") ||
							nameTextArray [i].substring(0,1).equals("p") ||
							nameTextArray [i].substring(0,1).equals("b")) {
							corpName += " " + nameTextArray [i].substring(1);
						}
					}
					
					if(!corpName.equals("")) {
						/* k == 0 => es la primera iteracion */
						if (k == 0) {
							/* Incorporar el corporativo al HashMap */
							String corpFullName = "";
							corpFullName = nameText;
							//System.out.println("FULL\t" + corpFullName);
							//System.out.println("PARS\t" + parseCorporationName(corpFullName));
							//System.out.println("FATH\t'" + getFathersParsedName(corpFullName)+"'");
							//System.out.println("ME\t" + getRealName(corpFullName));
							corpList.addMember(id, corpFullName);
						}
						/* k != 0 => es la segunda iteracion */
						else {
							if(! corpListFull ){
								corpListFull = true;
								try {
									corpList.linkMembers();
								}
								catch(Exception e) {
									System.out.println("Excepcion de linkMembers!! " + e.toString());
								}
							}
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
							CorporateMemberInfo thisCorp = corpList.getMember(id);
							//write org:subOrganizationOf
							String fatherID = thisCorp.getParentID();
							if (!fatherID.equals("")) {
								this.writer.setPrefix("org", orgUri);
								this.writer.writeEmptyElement(orgUri, "subOrganizationOf");
								this.writer.writeAttribute(rdfUri, "resource", base_uri + "corporativo/" + fatherID);
							}
							//write org:hasSubOrganization
							ArrayList<String> childrenIDs = thisCorp.getChildrenIDs();
							for (int l = 0; l < childrenIDs.size(); l++) {
								
								this.writer.setPrefix("org", orgUri);
								this.writer.writeEmptyElement(orgUri, "hasSubOrganization");
								this.writer.writeAttribute(rdfUri, "resource", base_uri + "corporativo/" + childrenIDs.get(l));
							}
							//this.writer.writeEndElement();
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
	
	private String parseCorporationName(String originalName) throws Exception {
		
		String[] nameTextArray = originalName.split("\\|");
		/* corpName guarda corporation name */
		String corpName, newName = "";
		Unidecoder ud = new Unidecoder();
		
		for (int i = 0; i < nameTextArray.length; i++) {
			if (nameTextArray [i].equals("")) continue;
			String subFieldChar = nameTextArray[i].substring(0, 1);
			if (!subFieldChar.equals("a") && !subFieldChar.equals("b") && !subFieldChar.equals("p") && !subFieldChar.equals("t") && !subFieldChar.equals("v")) continue;
			String namePart = nameTextArray [i].substring(1);
			namePart = namePart.trim();
			if(namePart.substring(namePart.length() - 1).equals(".")) {
				namePart = namePart.substring(0, namePart.length() - 1);
			}
			namePart = namePart.trim();
		  newName += "|" + nameTextArray[i].substring(0, 1) + ud.unidecode(namePart);
		}
		return newName;
	}
	
	private String getFathersParsedName(String corporationFullName) throws Exception {
	  corporationFullName = parseCorporationName(corporationFullName);
		String [] corpNameArray = corporationFullName.split("\\|");
		return corporationFullName.substring(0, corporationFullName.length() - corpNameArray[corpNameArray.length - 1].length() - 1);
	}
	
}
