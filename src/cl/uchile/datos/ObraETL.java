/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import cl.uchile.xml.Element;


/**
 * @author Avengers
 *
 */
public class ObraETL extends AbstractETL {

	/**
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 * 
	 */
	//ArrayList<String> outputFilenames=new ArrayList<String>(){{add(outputFilename);add(outputFilename2);add(outputFilename3);}};
	public ObraETL(String inputFilename, ArrayList<String> outputFilenames) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilenames);
	}

	/**
	 * @throws XMLStreamException 
	 * 
	 */
	public void parseAndWrite() throws XMLStreamException {
		String id = ""; String tagname;
		
		XMLStreamWriter obraWriter = this.writers.get(0);
		XMLStreamWriter expWriter = this.writers.get(1);
		XMLStreamWriter manifWriter = this.writers.get(2);
		
		Element obraElement = new Element();
		Element expElement = new Element();
		Element manifElement = new Element();
		
		obraWriter.writeStartDocument();
		obraWriter.setPrefix("rdf", rdfUri);
		obraWriter.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		obraWriter.writeNamespace("dc",dcUri);
		obraWriter.writeNamespace("dct",dctUri);
		obraWriter.writeNamespace("frbrer",frbrerUri);
		obraWriter.writeNamespace("owl", owlUri);		
		obraWriter.writeNamespace("rdf", rdfUri);
		
		expWriter.writeStartDocument();
		expWriter.setPrefix("rdf", rdfUri);
		expWriter.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		expWriter.writeNamespace("dc",dcUri);
		expWriter.writeNamespace("dct",dctUri);
		expWriter.writeNamespace("frbrer",frbrerUri);
		expWriter.writeNamespace("owl", owlUri);		
		expWriter.writeNamespace("rdf", rdfUri);
		
		manifWriter.writeStartDocument();
		manifWriter.setPrefix("rdf", rdfUri);
		manifWriter.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		manifWriter.writeNamespace("dc",dcUri);
		manifWriter.writeNamespace("dct",dctUri);
		manifWriter.writeNamespace("frbrer",frbrerUri);
		manifWriter.writeNamespace("owl", owlUri);		
		manifWriter.writeNamespace("rdf", rdfUri);
		boolean isFirst = true;
		boolean named = false;
		boolean isFirstAsset = false;
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();	
			String attributeValueType = this.reader.getAttributeValue("", "type");
			String attributeValueName = this.reader.getAttributeValue("", "name");
//			System.out.println(tagname);
			if (!tagname.equals( "asset" ) &&  !(isFirst)) continue;
<<<<<<< HEAD
			if (attributeValueType == null) continue;
			if (attributeValueType.equals("FOLDER")) continue;
			w = this.reader.getAttributeCount();
=======
			try {if (attributeValueType.equals("FOLDER")) continue;}
			catch(Exception e){}
			//System.out.println(tagname);
			//System.out.println(w);
>>>>>>> 3490e939d22650b39bb5c32c9ece25d3ec7b25bd
			
			// we assume that CHARACTERS event comes next always, which could not be true
			System.out.println(attributeValueType);
			this.reader.next();
			
			if (attributeValueType.equals("ASSET")) {	
				isFirstAsset=true;
				if (!isFirst){
					System.out.println("ENTRA");
					obraElement.write(obraWriter);
					expElement.write(expWriter);
					manifElement.write(manifWriter);
				}
			}
			isFirst = false;				
			if(!isFirstAsset) continue;
			if (tagname.equals("id")){
				/*if(named){
					obraWriter.writeEndElement();
					expWriter.writeEndElement();
					manifWriter.writeEndElement();
					System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				}*/
				named = true;
				System.out.println("el id es: ");
				id = this.reader.getText();
				System.out.println(id);
				//if (!isFirst) this.writer.writeEndElement();
				
				//crear Obra ***********************************************************************
				obraWriter.flush();
				obraElement = new Element();
				obraElement.setPrefix("owl");
				obraElement.setUri(owlUri);
				obraElement.setElementName("NamedIndividual");
				obraElement.appendAttribute(rdfUri, "about", base_uri + "obra/" + id);
				
				Element typeObraElement = new Element();
				typeObraElement.setPrefix("rdf");
				typeObraElement.setUri(rdfUri);
				typeObraElement.setElementName("type");
				typeObraElement.appendAttribute(rdfUri, "resource", frbrerUri + "C1001");
				obraElement.appendElement(typeObraElement);
				
				// Agregar link a expresion
				Element realizedElement = new Element();
				realizedElement.setPrefix("frbrer");
				realizedElement.setUri(frbrerUri);
				realizedElement.setElementName("isRealizedThrough");
				realizedElement.appendAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
				obraElement.appendElement(realizedElement);
				
				//crear Expresion ******************************************************************
				expWriter.flush();
				expElement = new Element();
				expElement.setPrefix("owl");
				expElement.setUri(owlUri);
				expElement.setElementName("NamedIndividual");
				expElement.appendAttribute(rdfUri, "about", base_uri + "expresion/" + id);
				
				Element typeExpElement = new Element();
				typeExpElement.setPrefix("rdf");
				typeExpElement.setUri(rdfUri);
				typeExpElement.setElementName("type");
				typeExpElement.appendAttribute(rdfUri, "resource", frbrerUri + "C1002");
				expElement.appendElement(typeExpElement);
				
				// Agregar link a obra y manifestacion
				Element realizationElement = new Element();
				realizationElement.setPrefix("frbrer");
				realizationElement.setUri(frbrerUri);
				realizationElement.setElementName("isRealizationOf");
				realizationElement.appendAttribute(rdfUri, "resource",base_uri + "obra/" + id);
				expElement.appendElement(realizationElement);
				
				Element embodiedElement = new Element();
				embodiedElement.setPrefix("frbrer");
				embodiedElement.setUri(frbrerUri);
				embodiedElement.setElementName("isEmbodiedIn");
				embodiedElement.appendAttribute(rdfUri, "resource",base_uri + "manifestacion/" + id);
				expElement.appendElement(embodiedElement);
				
				//crear Manifestacion **************************************************************
				manifWriter.flush();
				manifElement = new Element();
				manifElement.setPrefix("owl");
				manifElement.setUri(owlUri);
				manifElement.setElementName("NamedIndividual");
				manifElement.appendAttribute(rdfUri, "about", base_uri + "manifestacion/" + id);
				
				Element typeManifElement = new Element();
				typeManifElement.setPrefix("rdf");
				typeManifElement.setUri(rdfUri);
				typeManifElement.setElementName("type");
				typeManifElement.appendAttribute(rdfUri, "resource", frbrerUri + "C1003");
				manifElement.appendElement(typeManifElement);
				
				// Agregar link a expresion
				Element embodimentElement = new Element();
				embodimentElement.setPrefix("frbrer");
				embodimentElement.setUri(frbrerUri);
				embodimentElement.setElementName("isEmbodimentOf");
				embodimentElement.appendAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
				manifElement.appendElement(embodimentElement);
				
				// Agregar Licencia e identificador
				Element licenseElement = new Element();
				licenseElement.setPrefix("dct");
				licenseElement.setUri(dctUri);
				licenseElement.setElementName("license");
				licenseElement.appendAttribute(rdfUri, "resource", "https://creativecommons.org/publicdomain/zero/1.0/");
				manifElement.appendElement(licenseElement);
				
				Element identifierElement = new Element();
				identifierElement.setPrefix("dct");
				identifierElement.setUri(dctUri);
				identifierElement.setElementName("identifier");
				identifierElement.appendAttribute(rdfUri, "resource","http://bibliotecadigital.uchile.cl/client/sisib/search/detailnonmodal?d=ent%3A%2F%2FSD_ASSET%2F"+id.substring(0, 2)+"%2F"+id+"~ASSET~0~17");
				manifElement.appendElement(identifierElement);
				
				isFirst = false;
			}
			if(tagname.equals("field")){
				//System.out.println("probando");
				if(attributeValueName.equals("Title")){	
					this.reader.next();
					if(this.reader.getName().toString().equals("value")	){
						System.out.println("el title es**************************: ");
						System.out.println(this.reader.getName().toString());
						String title = this.reader.getElementText();
						System.out.println(" asdasdasd ");
						//obraWriter.flush();
						
						Element titleElement = new Element();
						titleElement.setPrefix("dc");
						titleElement.setUri(dcUri);
						titleElement.setElementName("title");
						titleElement.setText(title);
						obraElement.appendElement(titleElement);							
					}
					isFirst=false;
				}
				if(attributeValueName.equals("NAME")){
					this.reader.next();
					if(this.reader.getName().toString().equals("value")	){
						System.out.println("el NAME es: ");
						String NAME = this.reader.getElementText();
						System.out.println(NAME);
						obraWriter.flush();
						
						Element alternativeElement = new Element();
						alternativeElement.setPrefix("dct");
						alternativeElement.setUri(dctUri);
						alternativeElement.setElementName("alternative");
						alternativeElement.setText(NAME);
						obraElement.appendElement(alternativeElement);
					}
					isFirst=false;
				}
				if(attributeValueName.equals("Date")){
					this.reader.next();
					if(this.reader.getName().toString().equals("value")	){
						System.out.println("el Date es: ");
						String Date = this.reader.getElementText();
						String Date2;
						System.out.println(Date);
						Date2 = Date;							
						if(Date.length()>17 && Date.substring(0, 6)=="[entre")
							Date2 = Date.substring(7, 11)+" - "+Date.substring(14,18)+" rango estimado";
						else{
							if(Date.contains("?"))
								Date2 = Date.substring(0,2)+"00 fecha estimada";
							else
								Date2 = Date;
						}
						obraWriter.flush();
						
						Element issuedElement = new Element();
						issuedElement.setPrefix("dct");
						issuedElement.setUri(dctUri);
						issuedElement.setElementName("issued");
						issuedElement.setText(Date2);
						obraElement.appendElement(issuedElement);
					}
					isFirst=false;
				}
					if(attributeValueName.equals("Language")){	
						this.reader.next();
						if(this.reader.getName().toString().equals("value")	){
							System.out.println("el Language es: ");
							String Language = this.reader.getElementText();
							System.out.println(Language);
							String Lan;
							if (Language.equals("spa") || Language.equals("Español"))
								Lan="http://www.lexvo.org/page/iso639-3/spa";
							else
							{
								if (Language.equals("eng") || Language.equals("Inglés"))
									Lan="http://www.lexvo.org/page/iso639-3/eng";
								else
									Lan="";
							}
							expWriter.flush();
							
							Element languageElement = new Element();
							languageElement.setPrefix("dc");
							languageElement.setUri(dcUri);
							languageElement.setElementName("language");
							languageElement.appendAttribute(dcUri, "resource", Lan);
							expElement.appendElement(languageElement);
						}
						isFirst=false;
					}
				if(attributeValueName.equals("Rights")){
					this.reader.next();
					if(this.reader.getName().toString().equals("value")	){
						System.out.println("el Rights es: ");
						String Rights = this.reader.getElementText();
						System.out.println(Rights);
						manifWriter.flush();
						
						Element rightsElement = new Element();
						rightsElement.setPrefix("dc");
						rightsElement.setUri(dcUri);
						rightsElement.setElementName("rights");
						rightsElement.setText(Rights);
						manifElement.appendElement(rightsElement);						
					}
					isFirst=false;
				}
				if(attributeValueName.equals("Publisher")){	
					this.reader.next();
					if(this.reader.getName().toString().equals("value")){
						String Publisher = this.reader.getElementText();
						if(Publisher.length() < 1) continue;
						System.out.println("el Publisher es-------------------------------------------------------------------------------------------: ");
						System.out.println(Publisher);
						manifWriter.flush();
						
						Element publisherElement = new Element();
						publisherElement.setPrefix("dc");
						publisherElement.setUri(dcUri);
						publisherElement.setElementName("publisher");
						publisherElement.setText(Publisher);
						manifElement.appendElement(publisherElement);
					}
					isFirst=false;
				}

				if(attributeValueName.equals("Source")){
					this.reader.next();
					if(this.reader.getName().toString().equals("value")	){
						System.out.println("el Source es: ");
						String Source = this.reader.getElementText();
						System.out.println(Source);
						manifWriter.flush();
						
						Element sourceElement = new Element();
						sourceElement.setPrefix("dc");
						sourceElement.setUri(dcUri);
						sourceElement.setElementName("source");
						sourceElement.setText(Source);
						manifElement.appendElement(sourceElement);
					}
					isFirst=false;
				}

				//dead code
				//if(attributeValueName == null) continue;
			}
			//obraWriter.writeEndElement();
			//fin del ciclo
			isFirst = true;
		}
		
		// end the last guy
		obraWriter.writeEndElement();
		expWriter.writeEndElement();
		manifWriter.writeEndElement();
		// end the rdf descriptions
		//this.writer.writeEndElement();
		obraWriter.writeEndDocument();
		obraWriter.close();
		expWriter.writeEndDocument();
		expWriter.close();
		manifWriter.writeEndDocument();
		manifWriter.close();

	}

}
