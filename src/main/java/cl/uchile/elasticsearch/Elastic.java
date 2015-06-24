package main.java.cl.uchile.elasticsearch;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import main.java.cl.uchile.xml.Attribute;
import main.java.cl.uchile.xml.Element;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;


public class Elastic {

	private Client c;

	@SuppressWarnings( "resource" )
	public Elastic() {
		this.c = new TransportClient().addTransportAddress( new InetSocketTransportAddress( "localhost", 9300 ) );
	}

	public void close() {
		this.c.close();
	}

	public void index( Element el ) {
		String url = "";
		for( Attribute a : el.getAttributes() ) {
			if( a.getLocalname() == "about" ) {
				url = a.getValue();
				break;
			}
		}
		if( url == "" ) return;

		IndexResponse r;
		String log = "";
		for( Element e : el.getChildren() ) {
			String ename = e.getElementName();
			if( ename == "label" || ename == "name" || ename == "alternative" ) {
				try {
					r = c.prepareIndex( "repo", "nombre" ).setSource( "{ \"url\": \"" + url + "\", \"name\": \"" + e.getText() + "\" }" ).execute().actionGet();
					log += url + " " + e.getText() + " " + r.getId() + " " + r.isCreated() + "\n";
				}
				catch( Exception ex ) {
					StringWriter sw = new StringWriter();
					ex.printStackTrace( new PrintWriter( sw ) );
					log += "Ha ocurrido una excepcion: " + sw.toString() + "\n";
					break;
				}
			}
		}
		if( log != "" ) saveLog( log );
	}

	public String getCreator( String name ) {
		if( name == "" ) return "";
		SearchResponse searchRes = null;
		try {
			searchRes = this.c.prepareSearch( "repo" )
					.setIndices( "nombre" )
					.setQuery( QueryBuilders.fuzzyQuery( "name", name ) )
					.setSize( 1 )
					.execute()
					.actionGet();
		}
		catch( Exception ex ) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace( new PrintWriter( sw ) );
			saveLog( "Excepcion al buscar: " + sw.toString() + "\n" );
		}

		return searchRes == null || searchRes.getHits().getTotalHits() < 1 ? "" : (String) searchRes.getHits().getAt( 0 ).getSource().get( "url" );
	}

	private void saveLog( String s ) {
		FileWriter fw = null;
		DateFormat df = new SimpleDateFormat( "yyyyMMdd" );
		try {
			fw = new FileWriter( "output/index_" + df.format( new Date() ) + ".log", true );
			fw.write( s );
			fw.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}
