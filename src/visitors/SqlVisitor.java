package visitors;

import cl.uchile.xml.*;

public class SqlVisitor {
	
	private String attributeName;
	private String elementName;
	private Element resultElement;
	private Attribute resultAttribute;
	
	public Attribute getResultAttribute(){
		assert this.resultAttribute != null;
		return this.resultAttribute;
	}
	
	public Element getResultElement(){
		assert this.resultElement != null;
		return this.resultElement;
	}
	
	public void visit(Attribute attribute){
		resultAttribute = null;
		if(attribute.getLocalname().equals(attributeName)){
			resultAttribute = attribute;
		}
	}

	public void visit(Element element){
		resultElement = null;
		if(element.getElementName().equals(elementName)){
			resultElement = element;
		}
	}
}
