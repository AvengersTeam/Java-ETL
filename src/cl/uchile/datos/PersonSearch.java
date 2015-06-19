/**
 * 
 */
package cl.uchile.datos;

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
		String tagname; int count;
		String Uri_person,Name;
		Uri_person=Name="";
		this.inputFactory = XMLInputFactory.newInstance();
		this.reader = this.inputFactory.createXMLStreamReader(new FileInputStream(inputFilename),"UTF8");
		while(this.reader.hasNext()) {
			if (this.reader.next() != XMLStreamConstants.START_ELEMENT) continue; 
			tagname = this.reader.getName().toString();
			count=0;
			String attributeValue = this.reader.getAttributeLocalName(0);			
			//System.out.println("tag= "+tagname);
			try{
				if(attributeValue.equals(Path)){
					Uri_person = this.reader.getAttributeValue(0);
					count++;
					//String title = this.reader.getNamespaceURI();
					//System.out.println("text= "+title);
					// es cero porque puedo, gg					
					//System.out.println("Uri_person= "+Uri_person);				
					//System.out.println("name= "+attributeValue);
				}
			}
			catch(Exception e){}
			try{
				if(tagname.equals(Keyword)){					
					Name=this.reader.getElementText();
					count++;
					//System.out.println("Name = "+Name);
				}
			}
			catch(Exception e){}
			if(count>=2){
				map.put(Name,Uri_person);
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
