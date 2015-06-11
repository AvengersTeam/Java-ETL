/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;


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
		String id = ""; String tagname; int w;
		
		XMLStreamWriter obraWriter = this.writers.get(0);
		XMLStreamWriter expWriter = this.writers.get(1);
		XMLStreamWriter manifWriter = this.writers.get(2);

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
			if (!tagname.equals( "asset" ) &&  !(isFirst)) continue;
			try {if (attributeValueType.equals("FOLDER")) continue;}
			catch(Exception e){}
			w = this.reader.getAttributeCount();
			//System.out.println(tagname);
			//System.out.println(w);
			
			// we assume that CHARACTERS event comes next always, which could not be true
			//System.out.println(attributeValueType);
			this.reader.next();
			
			try {if (attributeValueType.equals("ASSET")) {	
				isFirstAsset=true;
					if (!isFirst){
						obraWriter.writeEndElement();
						expWriter.writeEndElement();
						manifWriter.writeEndElement();
					}
				}
				isFirst = false;				
			}			
			catch(Exception e){}
			if(!isFirstAsset) continue;
			if (tagname.equals("id")){
				if(named){
					obraWriter.writeEndElement();
					expWriter.writeEndElement();
					manifWriter.writeEndElement();
					System.out.println("holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
				}
				named = true;
				System.out.println("el id es: ");
				id = this.reader.getText();
				System.out.println(id);
				//if (!isFirst) this.writer.writeEndElement();
				//crear Obra ***********************************************************************
				obraWriter.flush();			
				obraWriter.setPrefix("owl", owlUri);
				obraWriter.writeStartElement(owlUri, "NamedIndividual");
				obraWriter.writeAttribute(rdfUri, "about", base_uri + "obra/" + id);
				obraWriter.writeEmptyElement(rdfUri, "type");
				obraWriter.writeAttribute(rdfUri, "resource", frbrerUri + "C1001");
				// Agregar link a expresion
				obraWriter.writeEmptyElement(frbrerUri, "isRealizedThrough");
				obraWriter.writeAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
				//crear Expresion ******************************************************************
				expWriter.flush();			
				expWriter.setPrefix("owl", owlUri);
				expWriter.writeStartElement(owlUri, "NamedIndividual");
				expWriter.writeAttribute(rdfUri, "about", base_uri + "expresion/" + id);
				expWriter.writeEmptyElement(rdfUri, "type");
				expWriter.writeAttribute(rdfUri, "resource", frbrerUri + "C1002");
				// Agregar link a obra y manifestacion
				expWriter.writeEmptyElement(frbrerUri, "isRealizationOf");
				expWriter.writeAttribute(rdfUri, "resource",base_uri + "obra/" + id);
				expWriter.writeEmptyElement(frbrerUri, "isEmbodiedln");
				expWriter.writeAttribute(rdfUri, "resource",base_uri + "manifestacion/" + id);
				//crear Manifestacion **************************************************************
				manifWriter.flush();			
				manifWriter.setPrefix("owl", owlUri);
				manifWriter.writeStartElement(owlUri, "NamedIndividual");
				manifWriter.writeAttribute(rdfUri, "about", base_uri + "manifestacion/" + id);
				manifWriter.writeEmptyElement(rdfUri, "type");
				manifWriter.writeAttribute(rdfUri, "resource", frbrerUri + "C1003");
				// Agregar link a expresion
				manifWriter.writeEmptyElement(frbrerUri, "isEmbodimentOf");
				manifWriter.writeAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
				// Agregar Licencia e identificador
				manifWriter.writeEmptyElement(dctUri, "license");
				manifWriter.writeAttribute(rdfUri, "resource", "https://creativecommons.org/publicdomain/zero/1.0/");
				manifWriter.writeEmptyElement(dctUri, "identifier");
				manifWriter.writeAttribute(rdfUri, "resource","http://bibliotecadigital.uchile.cl/client/sisib/search/detailnonmodal?d=ent%3A%2F%2FSD_ASSET%2F"+id.substring(0, 2)+"%2F"+id+"~ASSET~0~17");
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
						obraWriter.setPrefix("dc", dcUri);
						obraWriter.writeStartElement(dcUri,"title");
						obraWriter.writeCharacters(title);
						obraWriter.writeEndElement();							
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
						obraWriter.setPrefix("dct", dctUri);
						obraWriter.writeStartElement(dctUri,"alternative");
						obraWriter.writeCharacters(NAME);
						obraWriter.writeEndElement();	
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
						obraWriter.setPrefix("dct", dctUri);
						obraWriter.writeStartElement(dctUri,"issued");
						obraWriter.writeCharacters(Date2);
						obraWriter.writeEndElement();	
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
							expWriter.setPrefix("dc", dcUri);
							expWriter.writeEmptyElement(dcUri,"language");
							expWriter.writeAttribute(dcUri, "resource", Lan);
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
						manifWriter.setPrefix("dc", dcUri);
						manifWriter.writeStartElement(dcUri,"rights");
						manifWriter.writeCharacters(Rights);
						manifWriter.writeEndElement();								
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
						manifWriter.setPrefix("dc", dcUri);
						manifWriter.writeStartElement(dcUri,"publisher");
						manifWriter.writeCharacters(Publisher);
						manifWriter.writeEndElement();
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
						manifWriter.setPrefix("dc", dcUri);
						manifWriter.writeStartElement(dcUri,"source");
						manifWriter.writeCharacters(Source);
						manifWriter.writeEndElement();
					}
					isFirst=false;
				}

				if(attributeValueName == null) continue;
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
