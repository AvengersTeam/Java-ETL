package cl.uchile.xml;

import visitors.Visitor;

public class Attribute {

	String localname;
	String value;
	String nameSpaceUri;
		
	public Attribute(String attribute, String value, String nameSpaceUri) {
		this.localname = attribute;
		this.value = value;
		this.nameSpaceUri = nameSpaceUri;
	}
	
	public String getLocalname() {
		return this.localname;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setNameSpaceUri(String nameSpaceUri) {
		this.nameSpaceUri = nameSpaceUri;
	}

	public String getNameSpaceUri() {
		return this.nameSpaceUri;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return this.localname + ", " + this.value;
	}
}

