package main.java.cl.uchile.xml;

import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import main.java.visitors.Visitor;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/*
 * this are our own homebrew xml elements. Although we use some of the javax.xml , 
 * this is a different work with a diferent objective in mind (to write an rdf/xml file)
 */

public class Element {

	private List<Attribute> attributes; // the set of attributes
	private List<Element> children;     //the set of children of the element
	private String uri;                 //the uri in wich the element belongs
	private String elementName;         //element's name
	private String prefix;				//element prefix
	private String text;				//element text
	
	public Element() {
		attributes = new ArrayList<>();
		children = new ArrayList<>();
		prefix = "";
		text = "";
	}
	
	public void write(XMLStreamWriter writer) throws XMLStreamException{
		writer.setPrefix(prefix, uri);
		writer.writeStartElement(uri, elementName);
		this.writeAttributes(writer);
		writer.writeCharacters(text);
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
	
	public void appendAttribute(String nameSpace, String attribute, String value){
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
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void accept(Visitor visitor){
		visitor.visit(this);
	}
	
	public String getElementName(){
		return this.elementName;
	}

	
	@SuppressWarnings( "resource" )
	public void index2elastic() {
		String url = "";
		for( Attribute a : this.attributes ) {
			if( a.getLocalname() == "about" ) {
				url = a.getValue();
				break;
			}
		}
		if( url == "" ) return;
		
		//if c has a error, what would I do?
		Client c = new TransportClient().addTransportAddress( new InetSocketTransportAddress( "localhost", 9300 ) );
		IndexResponse r; String log = "";
		for( Element e : this.children ) {
			if( e.elementName == "label" || e.elementName == "name" || e.elementName == "alternative" ) {
				r = c.prepareIndex( "repo", "nombre" )
						.setSource( "{ 'url': '"+url+"', 'name': '"+e.text+"' }" )
						.execute()
						.actionGet();
				log += url+" "+e.text+" "+ r.getId() + " " + r.isCreated() + "\n";
			}
		}
		if( log != "" ) saveLog( log );
		
		c.close();
	}
	
	//Esto deberia estar en una clase aparte, please hacer refactoring despues
	private void saveLog( String s ) {
		FileWriter fw = null;
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		try {
			fw = new FileWriter( "index_" + df.format( new Date() ) + ".log" );
			fw.write( s );
			fw.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString(){
		String str = "";
		str += this.prefix + ":" + this.elementName + " attr:";
		for(Attribute attribute : attributes){
			str += " " + attribute.toString() + "\n";
		}
		str += " text: " + this.text + "\n";
		str += " child:";
		for(Element element : children){
			str += " " + element.toString() + "\n";
		}
		return str;
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	public List<Element> getChildren() {
		return children;
	}


}
