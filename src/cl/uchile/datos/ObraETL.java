/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;


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
	public void parse() throws XMLStreamException {
		String id = ""; String tagname; int w;
		String base_uri = "http://datos.uchile.cl/recurso/";
		
		String owlUri = "http://datos.uchile.cl/ontologia/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String dcUri = "http://purl.org/dc/elements/1.1/";
		String dctUri = "http://purl.org/dc/terms/";
		String frbrerUri = "http://iflastandards.info/ns/fr/frbr/frbrer#";

		this.writers.get(0).writeStartDocument();
		this.writers.get(0).setPrefix("rdf", rdfUri);
		this.writers.get(0).writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writers.get(0).writeNamespace("dc",dcUri);
		this.writers.get(0).writeNamespace("dct",dctUri);
		this.writers.get(0).writeNamespace("frbrer",frbrerUri);
		this.writers.get(0).writeNamespace("owl", owlUri);		
		this.writers.get(0).writeNamespace("rdf", rdfUri);
		
		this.writers.get(1).writeStartDocument();
		this.writers.get(1).setPrefix("rdf", rdfUri);
		this.writers.get(1).writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writers.get(1).writeNamespace("dc",dcUri);
		this.writers.get(1).writeNamespace("dct",dctUri);
		this.writers.get(1).writeNamespace("frbrer",frbrerUri);
		this.writers.get(1).writeNamespace("owl", owlUri);		
		this.writers.get(1).writeNamespace("rdf", rdfUri);
		
		this.writers.get(2).writeStartDocument();
		this.writers.get(2).setPrefix("rdf", rdfUri);
		this.writers.get(2).writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writers.get(2).writeNamespace("dc",dcUri);
		this.writers.get(2).writeNamespace("dct",dctUri);
		this.writers.get(2).writeNamespace("frbrer",frbrerUri);
		this.writers.get(2).writeNamespace("owl", owlUri);		
		this.writers.get(2).writeNamespace("rdf", rdfUri);
		boolean isFirst = true;
	
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();	
			String attributeValueType = this.reader.getAttributeValue("", "type");
			String attributeValueName = this.reader.getAttributeValue("", "name");
			if (!tagname.equals( "asset" ) &&  !(isFirst)) continue;
			try {if (attributeValueType.equals("FOLDER")) continue;}
			catch(Exception e){}
			w = this.reader.getAttributeCount();
			System.out.println(tagname);
			System.out.println(w);
			
			// we assume that CHARACTERS event comes next always, which could not be true
			System.out.println(attributeValueType);
			this.reader.next();
			try {if (attributeValueType.equals("ASSET")) {				
				//if (!isFirst) this.writer.writeEndElement();
				isFirst = false;
			}}
			catch(Exception e){}
			if (tagname.equals("id")){
				System.out.println("el id es: ");
				id = this.reader.getText();
				System.out.println(id);
				//if (!isFirst) this.writer.writeEndElement();
				//crear Obra ***********************************************************************
				this.writers.get(0).flush();			
				this.writers.get(0).setPrefix("owl", owlUri);
				this.writers.get(0).writeStartElement(owlUri, "NamedIndividual");
				this.writers.get(0).writeAttribute(rdfUri, "about", base_uri + "obra/" + id);
				this.writers.get(0).writeEmptyElement(rdfUri, "type");
				this.writers.get(0).writeAttribute(rdfUri, "resource", "frbrer:C1001");
				// Agregar link a expresion
				this.writers.get(0).writeEmptyElement(frbrerUri, "isRealizedThrough");
				this.writers.get(0).writeAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
				//crear Expresion ******************************************************************
				this.writers.get(1).flush();			
				this.writers.get(1).setPrefix("owl", owlUri);
				this.writers.get(1).writeStartElement(owlUri, "NamedIndividual");
				this.writers.get(1).writeAttribute(rdfUri, "about", base_uri + "expresion/" + id);
				this.writers.get(1).writeEmptyElement(rdfUri, "type");
				this.writers.get(1).writeAttribute(rdfUri, "resource", "frbrer:C1002");
				// Agregar link a obra y manifestacion
				this.writers.get(1).writeEmptyElement(frbrerUri, "isRealizationOf");
				this.writers.get(1).writeAttribute(rdfUri, "resource",base_uri + "obra/" + id);
				this.writers.get(1).writeEmptyElement(frbrerUri, "isEmbodiedln");
				this.writers.get(1).writeAttribute(rdfUri, "resource",base_uri + "manifestacion/" + id);
				//crear Manifestacion **************************************************************
				this.writers.get(2).flush();			
				this.writers.get(2).setPrefix("owl", owlUri);
				this.writers.get(2).writeStartElement(owlUri, "NamedIndividual");
				this.writers.get(2).writeAttribute(rdfUri, "about", base_uri + "manifestacion/" + id);
				this.writers.get(2).writeEmptyElement(rdfUri, "type");
				this.writers.get(2).writeAttribute(rdfUri, "resource", "frbrer:C1003");
				// Agregar link a expresion
				this.writers.get(2).writeEmptyElement(frbrerUri, "isEmbodimentOf");
				this.writers.get(2).writeAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
				// Agregar Licencia e identificador
				this.writers.get(2).writeEmptyElement(dctUri, "license");
				this.writers.get(2).writeAttribute(rdfUri, "resource", "https://creativecommons.org/publicdomain/zero/1.0/");
				this.writers.get(2).writeEmptyElement(dctUri, "identifier");
				this.writers.get(2).writeAttribute(rdfUri, "resource","http://bibliotecadigital.uchile.cl/client/sisib/search/detailnonmodal?d=ent%3A%2F%2FSD_ASSET%2F"+id.substring(0, 2)+"%2F"+id+"~ASSET~0~17");
				isFirst = false;
			}
			try{ 
				if(attributeValueName.equals("Title")){			
					System.out.println("el title es: ");
					String title = this.reader.getText();
					System.out.println(title);
					this.writers.get(0).flush();
					this.writers.get(0).writeAttribute(dcUri,"title",title);
					isFirst=false;
				}
			}
			catch(Exception e){}
			try{ 
				if(attributeValueName.equals("NAME")){			
					System.out.println("el NAME es: ");
					String NAME = this.reader.getText();
					System.out.println(NAME);
					this.writers.get(0).flush();
					this.writers.get(0).writeAttribute(dctUri,"alternative",NAME);
					isFirst=false;
				}
			}
			catch(Exception e){}
			try{ 
				if(attributeValueName.equals("Date")){			
					System.out.println("el Date es: ");
					String Date = this.reader.getText();
					String Date2;
					System.out.println(Date);
					if(Date.substring(0, 6)=="[entre")
						Date2 = Date.substring(7, 11)+" - "+Date.substring(14,18)+" rango estimado";
					else{
						if(Date.contains("?"))
							Date2 = Date.substring(0,2)+"00 fecha estima";
						else
							Date2 = Date;
					}						
					this.writers.get(0).flush();
					this.writers.get(0).writeAttribute(dctUri,"issued",Date2);
					isFirst=false;
				}
			}
			catch(Exception e){}			
			try{ 
				if(attributeValueName.equals("Language")){			
					System.out.println("el Language es: ");
					String Language = this.reader.getText();
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
					this.writers.get(1).flush();
					this.writers.get(1).writeEmptyElement(dcUri,"language");
					this.writers.get(1).writeAttribute(rdfUri,"resource",Lan);
					isFirst=false;
				}
			}
			catch(Exception e){}
			try{ 
				if(attributeValueName.equals("Rights")){			
					System.out.println("el Rights es: ");
					String Rights = this.reader.getText();
					System.out.println(Rights);
					this.writers.get(2).flush();
					this.writers.get(2).writeAttribute(dcUri,"rigths",Rights);
					isFirst=false;
				}
			}
			catch(Exception e){}
			try{ 
				if(attributeValueName.equals("Publisher")){			
					System.out.println("el Publisher es: ");
					String Publisher = this.reader.getText();
					System.out.println(Publisher);
					this.writers.get(2).flush();
					this.writers.get(2).writeAttribute(dcUri,"publisher",Publisher);
					isFirst=false;
				}
			}
			catch(Exception e){}
			try{ 
				if(attributeValueName.equals("Source")){			
					System.out.println("el Source es: ");
					String Source = this.reader.getText();
					System.out.println(Source);
					this.writers.get(2).flush();
					this.writers.get(2).writeAttribute(dcUri,"source",Source);
					isFirst=false;
				}
			}
			catch(Exception e){}
			if(attributeValueName == null) continue;
						
			//fin del ciclo
			isFirst = true;
		}
		
		// end the last guy
		this.writers.get(0).writeEndElement();
		this.writers.get(1).writeEndElement();
		this.writers.get(2).writeEndElement();
		// end the rdf descriptions
		//this.writer.writeEndElement();
		this.writers.get(0).writeEndDocument();
		this.writers.get(0).close();
		this.writers.get(1).writeEndDocument();
		this.writers.get(1).close();
		this.writers.get(2).writeEndDocument();
		this.writers.get(2).close();

	}

}
