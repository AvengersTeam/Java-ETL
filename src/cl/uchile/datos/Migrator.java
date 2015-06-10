/**
 * 
 */
package cl.uchile.datos;

import java.io.FileNotFoundException;

/**
 * @author Avengers
 *
 */
public class Migrator {

	/**
	 * @param args
	 * @throws XMLStreamException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		String inputFilename;
		String outputFilename;
		
		/* Run ETL personas */ 
		/*
		inputFilename = "input/autoridades-personas.xml";
		outputFilename = "output/autoridades-personas.rdf";

		PersonETL p = new PersonETL(inputFilename, outputFilename);
		p.parse();
		*/
		
		/* Run ETL eventos */ 
		/*
		inputFilename = "input/autoridades-eventos.xml";
		outputFilename = "output/autoridades-eventos.rdf";
		EventETL e = new EventETL(inputFilename, outputFilename);
		e.parse();
		
		/* RUT ETL localidades */
		
		outputFilename = "output/localidades.rdf";
		LocationETL l = new LocationETL( outputFilename);
		l.parse();
		
		
		/* RUN ETL corporativos */
		
		
		
		/* Inicio instancia de Pretty */
		Pretty pretty = new Pretty();
		
		/* Pretty Print personas */
        //pretty.print("autoridades-personas.rdf","pretty-personas.rdf");
		/* Pretty Print eventos */
        //pretty.print("autoridades-eventos.rdf","pretty-eventos.rdf");
		/* Pretty Print locations */
		pretty.print("localidades.rdf","pretty-localidades");
	}
}
