package main.java.cl.uchile.datos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.json.simple.JSONArray;

import main.java.utils.CorporateMemberInfo;
import main.java.utils.CorporateMemberList;
import main.java.utils.Unidecoder;
import main.java.cl.uchile.json.JsonReader;
import main.java.cl.uchile.xml.Element;
import main.java.utils.ETLLogger;
/**
 * @author Avengers
 * ETL de Corporativo.
 */
public class CorporateETL extends AbstractETL {
	Logger log;
	String inputFN = "";
	
	/**
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * 
	 */
	
	public CorporateETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
		inputFN = inputFilename;
		log = ETLLogger.getLog("log/corporate.txt");
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
		
		Element corporateElement = new Element();
		
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
		boolean corpListLinked = false;
		
		boolean corporateFinished = false;
		
		/* Dos iteraciones, una para llenar el HashMap, otra para crear el rdf */
		for (int k = 0; k < 3; k++) {
			
			if(k == 2 || k == 1) this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFN), "UTF8");
			
			while(this.reader.hasNext()) {
				
				if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue;
				tagname = this.reader.getName().toString();
				if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
				String attributeValue = this.reader.getAttributeValue("", "tag");
				// we assume that CHARACTERS event comes next always, which could not be true
				this.reader.next();
				
				if (tagname.equals("authorityID")) {
					id = this.reader.getText();
					if(k == 2){
						//faltara escribir el ultimo elemento
						if( corporateFinished ){
							corporateElement.write(this.writer);
							this.writer.flush();
						}
						corporateFinished = true;
						corporateElement = new Element();
						// New guy starts here
						corporateElement.setPrefix("owl");
						corporateElement.setUri(owlUri);
						corporateElement.setElementName("NamedIndividual");
						corporateElement.appendAttribute(rdfUri, "about", base_uri + "corporativo/" + id);
						
					}
				}
				
				if(attributeValue == null) continue;
				
				if(attributeValue.equals("510")) {
					
					CorporateMemberInfo thisCorp = corpList.getMember(id);
					/**lee y agrega los predecesores y sucesores al hashmap*/
					if (k == 0) {
						
					}
					
					/** Agrega al elemento los predecesores y sucesores */
					else if(k == 1){
						
						
						if (this.reader.getText().contains("|wa") || this.reader.getText().contains("|wb")) { 
							String referenceText = this.reader.getText();
							String referenceType = referenceText.substring(0, 3); 
							String referenceTextNormalized = CorporateMemberList.normalizeFullNameString(referenceText);
							try {
								corpList.setMemberReference(id, referenceType, referenceTextNormalized);
							}
							catch(Exception e) {
								log.info(e.toString());
							}
						}
					}
				}
				
				if(attributeValue.equals("110") && this.reader.getText().contains("|a")) {
					String nameText = this.reader.getText();
					String[] nameTextArray = nameText.split("\\|");
					/* corpName guarda corporation name */
					String corpName = "";
					/* Buscar el nombre de la localidad en el nombre */
					locationFound = false;
					location = "";
					
					for (int i = 0; i < nameTextArray.length; i++) {
						if (nameTextArray [i].equals("")) continue;
						
						
						
						if (nameTextArray [i].substring(0,1).equals("a") ||
								nameTextArray [i].substring(0,1).equals("t") ||
								nameTextArray [i].substring(0,1).equals("p") ||
								nameTextArray [i].substring(0,1).equals("v") ||
								nameTextArray [i].substring(0,1).equals("b")) {
							corpName += nameTextArray [i].substring(1);

							/* Quitar caracteres especiales */
							nameTextArray [i] = ud.unidecode(nameTextArray [i]);
							/* Se verifica si el nombre de la localidad coincide con uno del arreglo
							 * de ciudades o de paises */
							if(!locationFound) {
								for(int j = 0; j < jCities.size(); j++){
									String aux = ud.unidecode((String)jCities.get(j));
									if(nameTextArray [i].indexOf(aux) != -1){
										location = aux;
										locationFound = true;
										break;
									}
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
						}
						if(!locationFound){
							log.info("Advertencia corporativo ID='" + id + "': sin localidad en el nombre (campo 110)");
						}
					}
					
					if(!corpName.equals("")) {
						/* k == 0 => es la primera iteracion */
						if (k == 0) {
							/* Incorporar el corporativo al HashMap */
							String corpFullName = "";
							corpFullName = nameText;
							corpList.addMember(id, corpFullName);
						}
						/* k != 0 => es la segunda iteracion */
						else if(k == 2){
							if(! corpListLinked ){
								corpListLinked = true;
								try {
									corpList.linkMembers();
								}
								catch(Exception e) {
									log.info("ERROR FATAL en función CorporateMemberList.linkMembers(): " + e.toString() + "Las referencias de padres e hijos no han sido establecidas correctamente");
									
								}
							}
							//corporateElement
							/* write type */
							Element typeElement = new Element();
							typeElement.setPrefix("rdf");
							typeElement.setUri(rdfUri);
							typeElement.setElementName("type");
							typeElement.appendAttribute(rdfUri, "resource", foafUri + "organization");
							corporateElement.appendElement(typeElement);
							Element typeElement2 = new Element();
							typeElement2.setPrefix("rdf");
							typeElement2.setUri(rdfUri);
							typeElement2.setElementName("type");
							typeElement2.appendAttribute(rdfUri, "resource", orgUri + "organization");
							corporateElement.appendElement(typeElement2);
							
							/* write foaf:name */
							Element nameElement = new Element();
							nameElement.setPrefix("foaf");
							nameElement.setUri(foafUri);
							nameElement.setElementName("name");
							nameElement.setText(corpName);
							corporateElement.appendElement(nameElement);

							/* write foaf:label */
							Element labelElement = new Element();
							labelElement.setPrefix("foaf");
							labelElement.setUri(foafUri);
							labelElement.setElementName("label");
							labelElement.setText(corpName);
							corporateElement.appendElement(labelElement);

							if (locationFound) {
								/* write location */
								location = location.replaceAll(" ", "_");
								String locationURI = base_uri + "localidad/" + location;
								Element locationElement = new Element();
								locationElement.setPrefix("dct");
								locationElement.setUri(dctUri);
								locationElement.setElementName("spatial");
								locationElement.appendAttribute(rdfUri, "resource", locationURI);
								corporateElement.appendElement(locationElement);
							}
							CorporateMemberInfo thisCorp = corpList.getMember(id);
							//write org:subOrganizationOf
							String fatherID = thisCorp.getParentID();
							if (!fatherID.equals("")) {
								Element fatherElement = new Element();
								fatherElement.setPrefix("org");
								fatherElement.setUri(orgUri);
								fatherElement.setElementName("subOrganizationOf");
								fatherElement.appendAttribute(rdfUri, "resource", base_uri + "corporativo/" + fatherID);
								corporateElement.appendElement(fatherElement);
							}
							//write org:hasSubOrganization
							ArrayList<String> childrenIDs = thisCorp.getChildrenIDs();
							for (int l = 0; l < childrenIDs.size(); l++) {
								Element childElement = new Element();
								childElement.setPrefix("org");
								childElement.setUri(orgUri);
								childElement.setElementName("hasSubOrganization");
								childElement.appendAttribute(rdfUri, "resource", base_uri + "corporativo/" + childrenIDs.get(l));
								corporateElement.appendElement(childElement);
							}
							
							//write predecessors
							ArrayList<String> predecessorIDs = thisCorp.getPrevious_id();
							if (predecessorIDs != null) {
								for (int l = 0; l < predecessorIDs.size(); l++) {
									Element predElement = new Element();
									predElement.setPrefix("org");
									predElement.setUri(orgUri);
									predElement.setElementName("Antecesor");
									predElement.appendAttribute(rdfUri, "resource", base_uri + "corporativo/" + predecessorIDs.get(l));
									corporateElement.appendElement(predElement);
								}
							}
						
							//write sucessors
							ArrayList<String> succesorIDs = thisCorp.getNext_id();
							if (succesorIDs != null) {
								for (int l = 0; l < succesorIDs.size(); l++) {
									Element succElement = new Element();
									succElement.setPrefix("org");
									succElement.setUri(orgUri);
									succElement.setElementName("Sucesor");
									succElement.appendAttribute(rdfUri, "resource", base_uri + "corporativo/" + succesorIDs.get(l));
									corporateElement.appendElement(succElement);
								}
							}
							
							//corporateFinished = true;
							//this.writer.writeEndElement();
						}
					}
				}
			}
		}
		
		if( corporateFinished ){
			corporateElement.write(this.writer);
			this.writer.flush();
			corporateFinished = false;
		}
		
		// end the last guy
		//this.writer.writeEndElement();
		// end the rdf descriptions
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();
	}

}
