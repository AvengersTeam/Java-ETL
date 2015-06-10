package cl.uchile.xml;

public class Attribute {

	String localname;
	String value;
	String nameSpaceUri;
		
	public Attribute(String attribute, String value) {
		this.localname = attribute;
		this.value = value;
	}
	
	public String getLocalname(){
		return this.localname;
	}
	
	public String getValue(){
		return this.value;
	}
	
	public void setNameSpaceUri(String nameSpaceUri){
		this.nameSpaceUri = nameSpaceUri;
	}

	public String getNameSpaceUri(){
		return this.nameSpaceUri;
	}
}

