package visitors;

import cl.uchile.xml.*;

public class SqlVisitor extends Visitor{
	
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
	@Override
	public void visit(Attribute attribute){
		resultAttribute = null;
		if(attribute.getLocalname().equals(attributeName)){
			resultAttribute = attribute;
		}
	}
	@Override
	public void visit(Element element){
		resultElement = null;
		if(element.getElementName().equals(elementName)){
			resultElement = element;
		}else{
			for(Element e : element.getChildren()){
				this.visit(e);
			}
		}
	}
	
	public Element searchAttribute(Attribute attribute, String attributeName){
		this.attributeName = attributeName;
		try{ 
			attribute.accept(this);
			return this.getResultElement();
		}catch(AssertionError e){
			return new EmptyElement();
		}
	}
	
	public Attribute searchElement(Element element, String elementName){
		this.elementName = elementName;
		try{ 
			element.accept(this);
			return this.getResultAttribute();
		}catch(AssertionError e){
			return new EmptyAttribute();
		}
	}
}
