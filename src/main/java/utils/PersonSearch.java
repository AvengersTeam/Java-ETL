/**
 * 
 */
package main.java.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author Avengers
 *
 */
public class PersonSearch {
	XMLInputFactory inputFactory;
	XMLStreamReader reader;
	Map<String, String> map;
	
	public PersonSearch(String inputFilename, String Path, String Keyword) throws FileNotFoundException, XMLStreamException{
		map = new HashMap<String, String>();
		NameParser nameParser = new NameParser();
		String tagname; int count;
		String Uri_person,Name;
		Uri_person=Name="";
		this.inputFactory = XMLInputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename),"UTF8");
		count=0;
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			
			String attributeValue = this.reader.getAttributeLocalName(0);
			try{
				if(attributeValue.equals(Path)){					
					Uri_person = this.reader.getAttributeValue(0);					
					count++;										
				}
			}
			catch(Exception e){}
			try{
				if(tagname.equals(Keyword)){
					Name=this.reader.getElementText();
					Name = nameParser.ParserName(Name).toUpperCase();
					count++;
				}
			}
			catch(Exception e){}			
			if(count==2){
				map.put(Name,Uri_person);
				/*if(Name.contains("KARLLA")){
					System.out.println("Person= "+Name);
				}*/				
				//System.out.println("Person= "+Name+" Uri= "+map.get(Name));
				count=0;
			}
		}
	}

	/**
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
