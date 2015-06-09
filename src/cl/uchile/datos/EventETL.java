package cl.uchile.datos;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

public class EventETL extends AbstractETL {
	
	public EventETL(String inputFilename, String outputFilename) throws FileNotFoundException, XMLStreamException {
		super(inputFilename, outputFilename);
	}
	
	public void parse() throws XMLStreamException {
		String id = "";
		String tagname;
		String base_uri = "http://datos.uchile.cl/";
		String owlUri = base_uri+"ontologia/";
		String dctUri = "http://purl.org/dc/terms/";
		String rdfUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";	
		String frbrerUri = "http://iflastandards.info/ns/fr/frbr/frbrer#";
		String schemaUri = "http://schema.org";
		
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
		
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			if (!tagname.equals( "marcEntry" ) && ! tagname.equals( "authorityID" )) continue;
			String attributeValue = this.reader.getAttributeValue("", "tag");
			// we assume that CHARACTERS event comes next always, which could not be true
			this.reader.next();

			if (tagname.equals("authorityID")) {
				id = this.reader.getText();
				// We should not close the parent element
				if (!isFirst)
					this.writer.writeEndElement();
				isFirst = false;
				// Write buffered element, this can be optimized
				this.writer.flush();
				// New guy starts here
				this.writer.setPrefix("owl", owlUri);
				this.writer.writeStartElement(owlUri, "NamedIndividual");
				this.writer.writeAttribute(rdfUri, "about", base_uri + "recurso/evento/" + id);
			}
			
			if(attributeValue == null)
				continue;
			
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
				System.out.println("Nombre: " + name);
				this.writer.setPrefix("dct", dctUri);
				this.writer.writeStartElement(dctUri, "title");
				this.writer.writeCharacters(name);
				this.writer.writeEndElement();
				/* Caso type */
				this.writer.writeEmptyElement(rdfUri, "type");
				this.writer.writeAttribute(rdfUri, "resource", "frbrer:C1009");
				/* Según la letra luego de los pipes, creo los siguientes elementos */
				for(int i = 0; i < textArrayLength; i++){
					if (textArray[i].equals("")) continue;
					/* Caso fechas */
					if (textArray[i].substring(0,1).equals("d")) {
						String fecha = textArray[i].substring(1);
						fecha = fecha.replaceAll("[^0-9]", "");
						System.out.println("Fecha: " + fecha);
						this.writer.setPrefix("schema", schemaUri);
						this.writer.writeStartElement(schemaUri, "startDate");
						this.writer.writeAttribute(rdfUri, "datatype", "http://www.w3.org/2001/XMLSchema#dateTime");
						this.writer.writeCharacters(fecha);
						this.writer.writeEndElement();
					}
				}
			}
		}
		
		/* end the last event */
		this.writer.writeEndElement();
		/* end the rdf descriptions */
		this.writer.writeEndElement();
		this.writer.writeEndDocument();
		this.writer.close();
	}
}
