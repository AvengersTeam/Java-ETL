/**
 * 
 */
package cl.uchile.datos;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

/**
 * @author SISIB
 *
 */
public abstract class AbstractETL {
	
	SAXParserFactory factory;
	SAXParser saxParser;
	DefaultHandler handler;
	
	/**
	 * 
	 */
	public AbstractETL() {
		this.factory = SAXParserFactory.newInstance();
		try {
			this.saxParser = this.factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the factory
	 */
	public SAXParserFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(SAXParserFactory factory) {
		this.factory = factory;
	}

	/**
	 * @return the saxParser
	 */
	public SAXParser getSaxParser() {
		return saxParser;
	}

	/**
	 * @param saxParser the saxParser to set
	 */
	public void setSaxParser(SAXParser saxParser) {
		this.saxParser = saxParser;
	}

	/**
	 * @return the handler
	 */
	public DefaultHandler getHandler() {
		return handler;
	}

	/**
	 * @param handler the handler to set
	 */
	public void setHandler(DefaultHandler handler) {
		this.handler = handler;
	}
}
