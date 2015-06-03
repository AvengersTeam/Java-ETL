/**
 * 
 */
package cl.uchile.datos;

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * @author SISIB
 *
 */
public class Migrator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PersonETL p = new PersonETL();
		String filename = "C:\\Users\\SISIB\\Downloads\\Autoridades-personales-hasta2005-con-subcampos_2015-04-17.xml";
		try {
			p.getSaxParser().parse(filename, p.getHandler());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
