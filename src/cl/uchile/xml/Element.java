package cl.uchile.xml;

import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import visitors.Visitor;

/*
 * this are our own homebrew xml elements. Although we use some of the javax.xml , 
 * this is a different work with a diferent objective in mind (to write an rdf/xml file)
 */

public class Element {

	private List<Attribute> attributes; // the set of attributes
	private List<Element> children;     //the set of children of the element
	private String uri;                 //the uri in wich the element belong to
	private String elementName;         //element's name
	
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
	
	public void appendAttribute(String attribute, String value, String nameSpace){
		Attribute attr = new Attribute(attribute,value, nameSpace);
		this.attributes.add(attr);
	}
	
	public void appendElement(Element child){
		this.children.add(child);
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	public void accept(Visitor visitor){
		visitor.visit(this);
	}
	
	public String getElementName(){
		return this.elementName;
	}
	public void export2sql() {
		
	}
}
