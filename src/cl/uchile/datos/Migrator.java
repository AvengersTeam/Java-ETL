package cl.uchile.datos;

import java.util.ArrayList;

/**
 * @author Avengers
 *
 */
public class Migrator {

	/**
	 * @param args
	 * @throws Exception Excepción lanzada en caso de error.
	 */
	public static void main(String[] args) throws Exception {
		String inputFilename;
		String outputFilename;
		ArrayList<String> outputsFilenames = new ArrayList<String>();
		
//		/* Run ETL personas */ 
//		
//		inputFilename = "input/autoridades-personas.xml";
//		outputFilename = "output/autoridades-personas.rdf";
//
//		outputsFilenames.add("output/autoridades-personas.rdf");
//		outputsFilenames.add("output/autoridades-fechas.rdf");
//		PersonETL p = new PersonETL(inputFilename, outputsFilenames);
//		p.parseAndWrite();
//		
		/* Run ETL eventos */ 
		/*
		inputFilename = "input/autoridades-eventos.xml";
		outputFilename = "output/autoridades-eventos.rdf";
		
		EventETL e = new EventETL(inputFilename, outputFilename);
		e.parseAndWrite();
		*/
//		/* RUT ETL localidades */
//		
//		outputFilename = "output/localidades.rdf";
//		LocationETL l = new LocationETL(outputFilename);
//		l.parseAndWrite();
//		
//		/* RUN ETL corporativos */
//		inputFilename = "input/autoridades-corporativos.xml";
//		outputFilename = "output/autoridades-corporativos.rdf";
//		CorporateETL c = new CorporateETL(inputFilename, outputFilename);
//		c.parseAndWrite();
//		
//		/* RUN ETL obras */
		
		inputFilename = "input/Portfolio-Andres-bello.xml";
		outputsFilenames = new ArrayList<String>();
		outputsFilenames.add("output/obra.rdf");
		outputsFilenames.add("output/expresion.rdf");
		outputsFilenames.add("output/manifestacion.rdf");
		ObraETL o = new ObraETL(inputFilename, outputsFilenames);
		o.parseAndWrite();
		
//		/* Inicio instancia de Pretty */
		Pretty pretty = new Pretty();
//		
//		/* Pretty Print personas */
//        pretty.print("autoridades-personas.rdf","pretty-personas.rdf");
//        pretty.print("autoridades-fechas.rdf","pretty-fechas.rdf");
//		/* Pretty Print eventos */
//        pretty.print("autoridades-eventos.rdf","pretty-eventos.rdf");
//		/* Pretty Print locations */
//		pretty.print("localidades.rdf","pretty-localidades.rdf");
//		/* Pretty Print obras */
		pretty.print("obra.rdf","pretty-obra.rdf");
		pretty.print("expresion.rdf","pretty-expresion.rdf");
		pretty.print("manifestacion.rdf","pretty-manifestacion.rdf");
//		/* Pretty Print corporativos */
//		pretty.print("autoridades-corporativos.rdf","pretty-corporativos.rdf");
	}
}
