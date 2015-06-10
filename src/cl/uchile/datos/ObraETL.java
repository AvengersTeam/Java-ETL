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
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writers.get(0).writeNamespace("dc",dcUri);
		this.writers.get(0).writeNamespace("dct",dctUri);
		this.writers.get(0).writeNamespace("frbrer",frbrerUri);
		this.writers.get(0).writeNamespace("owl", owlUri);		
		this.writers.get(0).writeNamespace("rdf", rdfUri);
		
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
				this.writers.get(0).flush();			
				this.writers.get(0).setPrefix("owl", owlUri);
				this.writers.get(0).writeStartElement(owlUri, "NamedIndividual");
				this.writers.get(0).writeAttribute(rdfUri, "about", base_uri + "obra/" + id);
				this.writers.get(0).writeEmptyElement(rdfUri, "type");
				this.writers.get(0).writeAttribute(rdfUri, "resource", "frbrer:C1001");
				// Agregar link a expresion
				this.writer.writeEmptyElement(frbrerUri, "isRealizedThrough");
				this.writer.writeAttribute(rdfUri, "resource",base_uri + "expresion/" + id);
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
						
			if(attributeValueName == null) continue;
			/* por terminar
			if(attributeValue.equals("100") && this.reader.getText().contains("|a")) {
				String text = this.reader.getText();
				String[] textArray = text.split("\\|");
				for (int i = 0; i < textArray.length; i++) {
					if (textArray[i].equals("")) continue;
					if (textArray[i].substring(0,1).equals("a")) {
						System.out.println("Nombre: " + textArray[i].substring(1));
						this.writer.setPrefix("foaf", foafUri);
						this.writer.writeStartElement(foafUri, "name");
						this.writer.writeCharacters(textArray[i].substring(1));
						this.writer.writeEndElement();
					}
					else if (textArray[i].substring(0,1).equals("d")) {
						System.out.println("Fecha: " + textArray[i].substring(1));
						String[] birthArray = textArray[i].substring(1).split("\\-");
						this.writer.setPrefix("bio", bioUri);
						this.writer.writeEmptyElement(bioUri, "event");
						this.writer.writeAttribute(rdfUri, "resource", base_uri + "nacimiento/" + birthArray[0]);
						if (birthArray.length == 2) {
							this.writer.setPrefix("bio", bioUri);
							this.writer.writeEmptyElement(bioUri, "event");
							this.writer.writeAttribute(rdfUri, "resource", base_uri + "muerte/" + birthArray[1]);
						}
					}
				}
			}
			*/
			isFirst = true;
		}
		
		// end the last guy
		this.writer.writeEndElement();
		// end the rdf descriptions
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();

	}

}
