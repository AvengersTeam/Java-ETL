/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;
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
	public ObraETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
	}

	/**
	 * @throws XMLStreamException 
	 * 
	 */
	public void parse() throws XMLStreamException {
		String id = ""; String tagname; int w;
		String base_uri = "http://datos.uchile.cl/recurso/";
		
		String owlUri = "http://datos.uchile.cl/ontologia/";
		String foafUri = "http://xmlns.com/foaf/0.1/";
		String bioUri = "http://vocab.org/bio/0.1/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String dcUri = "http://purl.org/dc/elements/1.1/";
		String dctUri = "http://purl.org/dc/terms/";
		String frbrerUri = "http://iflastandards.info/ns/fr/frbr/frbrer#";

		this.writer.writeStartDocument();
		this.writer.setPrefix("rdf", rdfUri);
		this.writer.writeStartElement(rdfUri, "RDF");
		// XML namespaces
		this.writer.writeNamespace("dc",dcUri);
		this.writer.writeNamespace("dct",dctUri);
		this.writer.writeNamespace("frbrer",frbrerUri);
		this.writer.writeNamespace("owl", owlUri);
		this.writer.writeNamespace("foaf", foafUri);
		this.writer.writeNamespace("bio", bioUri);
		this.writer.writeNamespace("rdf", rdfUri);
		
		boolean isFirst = true;
	
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();	
			String attributeValue = this.reader.getAttributeValue("", "type");
			if (!tagname.equals( "asset" ) &&  !(isFirst)) continue;
			try {if (attributeValue.equals("FOLDER")) continue;}
			catch(Exception e){}
			w = this.reader.getAttributeCount();
			System.out.println(tagname);
			System.out.println(w);
			
			// we assume that CHARACTERS event comes next always, which could not be true
			System.out.println(attributeValue);
			this.reader.next();
			try {if (attributeValue.equals("ASSET")) {
				
				//if (!isFirst) this.writer.writeEndElement();
				isFirst = false;
			}}
			catch(Exception e){}
			if (tagname.equals("id")){
				System.out.println("el id es: ");
				id = this.reader.getText();
				System.out.println(id);
				//if (!isFirst) this.writer.writeEndElement();
				isFirst = false;
			}
				// We should not close the parent element
				
				// Write buffered element, this can be optimized
				this.writer.flush();
				// New guy starts here
				this.writer.setPrefix("owl", owlUri);
				this.writer.writeStartElement(owlUri, "NamedIndividual");
				this.writer.writeAttribute(rdfUri, "about", base_uri + "autoridad/" + id);
			
			
			if(attributeValue == null) continue;
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
