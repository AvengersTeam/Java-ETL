package cl.uchile.datos;

import java.util.HashMap;
import java.util.Map;

/*
 * No estoy seguro de seguir esta estructura o no :S de seguro hay mejores maneras de hacerlo X.x
 * 
 * */
public class PrimitiveWork {
	
	private int id;
	private String url;
	private Map<String,String> attrsVal;
	private static final Map<String,String> attrs = new HashMap<String,String>() {{ 
		put( "subject", "dc:subject" );
		put( "title", "dc:title" );
		put( "name", "dct:alternative" );
		put( "creatorId", "dct:creator" ); 
		put( "date", "dct:issued" );
		put( "language", "dc:language" );
		put( "rights", "dc:rights" );
		put( "publisher", "dc:publisher" );
		put( "source", "dc:source" );
	}};
	
	public PrimitiveWork( int id, String url ) {
		this.id = id;
		this.url = url;
		this.attrsVal = new HashMap<String,String>();
	}
	
	public String getAttr( String attr ) {
		if( ! attrsVal.containsKey( attr ) ) throw new RuntimeException( "The attr " + attr + " isnt defined" );
		return attrsVal.get( attr );
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setAttr( String attr, String value ) {
		if( ! attrs.containsKey( attr ) ) throw new RuntimeException( "Imposible define " + attr + " attribute" );
		attrsVal.put( attr, value );
	}
	
	public void write() {
		String str = "<owl:NamedIndividual rdf:about=\""+url+"/obra/"+id+"\">";
		str += "<rdf:type rdf:resource=\"frbrer:C1001\"/>";
		for( String s : attrs.keySet() ) {
			if( s.equals( "language" ) ) {
				/* Asumiendo que los atributos se van revisando en orden */
				str += "</owl:NamedIndividual><owl:NamedIndividual rdf:about=\""+url+"/expresion/"+id+"\">";
				str += "<rdf:type rdf:resource=\"frbrer:C1002\"/>";
				str += getTriple( s );
				str += "</owl:NamedIndividual><owl:NamedIndividual rdf:about=\""+url+"/manifestacion/"+id+"\">";
				str += "<rdf:type rdf:resource=\"frbrer:C1003\"/>";
				continue;
			}
			str += getTriple( s );
		}
		str += "</owl:NamedIndividual>";
		
		saveContent( str );
	}

	private void saveContent( String s ) {
		/* BLAH! */
	}
	
	private String getTriple( String a ) {
		if( ! attrsVal.containsKey( a ) ) return "";
		if( a.equals("language") ) {
			String val = attrsVal.get( a );
			String ini = "<" + attrs.get( a ) + " rdf:resource=\"http://www.lexvo.org/page/iso639-3/";
			if( val.equals( "spa" ) || val.equals( "Español" ) ) return ini+"spa\"/>";
			else if( val.equals( "eng" ) || val.equals( "English" ) ) return ini+"eng\"/>";
			else return "";
		}
		return attrsVal.containsKey( a ) ? "<" + attrs.get( a ) + ">" + attrsVal.get( a ) + "</" + attrs.get( a ) + ">" : "";
	}
}
