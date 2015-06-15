package main.java.cl.uchile.sqllite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DB {

	private String path;
	private Connection link;

	public DB( String path ) {
		this.setPath( path );
		try {
			Class.forName( "org.sqlite.JDBC" );
			this.link = DriverManager.getConnection( "jdbc:sqlite:" + path );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}

	public ResultSet query( String sql, Object... args ) {
		ResultSet rs = null;
		int c = sql.split( ":[0-9]+\\s+" ).length;
		if( c != args.length ) return rs;
		int i = 0;
		try {
			PreparedStatement query = link.prepareStatement( sql );
			for( Object o : args ) { // tendre que filtrar yo?
				query.setObject( ':' + i++, o );
			}
			rs = query.executeQuery();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public int queryUpd( String sql, Object... args ) {
		int c = sql.split( ":[0-9]+\\s+" ).length;
		if( c != args.length ) return -1;
		int i = 0;
		try {
			PreparedStatement query = link.prepareStatement( sql );
			for( Object o : args ) { // tendre que filtrar yo?
				query.setObject( ':' + i++, o );
			}
			c = query.executeUpdate();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		return c;
	}

	public String getPath() {
		return path;
	}

	public void setPath( String path ) {
		this.path = path;
	}

}
