package cl.uchile.xml;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/*
 * this are our own homebrew xml elements. Although we use some of the javax.xml , 
 * this is a different work with a diferent objective in mind (to write an rdf/xml file)
 */

public class Element {

	private List<Attribute> attributes; // the set of attributes
	private List<Element> children;     //the set of children of the element
	private String uri;                 //the uri in wich the element belong to
	private String elementName;         //element's name
	private String representation;      //the string representation of the xml element with all of his attributes and children
	
	public void write(XMLStreamWriter writer) throws XMLStreamException{
		writer.writeStartElement(uri, elementName);
		this.writeAttributes(writer);
		this.writeElements(writer);
		writer.writeEndElement();
	}
	
	private void writeAttributes(XMLStreamWriter writer) throws XMLStreamException{
		for(Attribute attribute : attributes){
			writer.writeAttribute(attribute.getNameSpaceUri(), attribute.getLocalname(), attribute.getValue());
		}
	}
	
	private void writeElements(XMLStreamWriter writer) throws XMLStreamException{
		for(Element element : children){
			element.write(writer);
		}
	}
	
	public void appendAttributes(String attribute, String value){
		Attribute attr = new Attribute(attribute,value);
		this.attributes.add(attr);
	}
	
	public void export2sql() {
		
	}
}
