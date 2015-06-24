package main.java.cl.uchile.datos;

import java.util.ArrayList;

import main.java.utils.Pretty;

/**
 * @author Avengers
 *
 */
public class Migrator {

	/**
	 * @param args
	 * @throws java.lang.Exception Excepcion lanzada en caso de que haya error.
	 */
	public static void main(String[] args) throws Exception {
		String inputFilename;
		String outputFilename;
		ArrayList<String> outputsFilenames = new ArrayList<String>();
		
		/* Run ETL personas */ 
		
		inputFilename = "input/autoridades-personas.xml";
		outputFilename = "output/autoridades-personas.rdf";

		outputsFilenames.add( outputFilename );
		outputsFilenames.add( "output/autoridades-fechas.rdf" );
		try{
			PersonETL p = new PersonETL(inputFilename, outputsFilenames);
			p.parseAndWrite();
		}catch{
			//crear log y decir que falta archivo =)
		}
		/* Run ETL eventos */
		
		inputFilename = "input/autoridades-eventos.xml";
		outputFilename = "output/autoridades-eventos.rdf";
		try{
			EventETL e = new EventETL(inputFilename, outputFilename);
			e.parseAndWrite();
		}catch{
			//crear log y decir que falta archivo =)
		}
		
		/* RUT ETL localidades */
	
		outputFilename = "output/localidades.rdf";
		try{
			LocationETL l = new LocationETL(outputFilename);
			l.parseAndWrite();
		}catch{
			//crear log y blah blah 
		}
		
		/* RUN ETL corporativos */
		
		inputFilename = "input/autoridades-corporativos.xml";
		outputFilename = "output/autoridades-corporativos.rdf";
		try{
			CorporateETL c = new CorporateETL(inputFilename, outputFilename);
			c.parseAndWrite();
		}catch{
			//blah blah
		}
		
		/* RUN ETL obras */
		inputFilename = "input/Portfolio-Andres-bello.xml";
		outputsFilenames = new ArrayList<String>();
		outputsFilenames.add("output/obra.rdf");
		outputsFilenames.add("output/expresion.rdf");
		outputsFilenames.add("output/manifestacion.rdf");
		try{
			ObraETL o = new ObraETL(inputFilename, outputsFilenames);
			o.parseAndWrite();
		}catch{

		}
		/* Inicio instancia de Pretty */
		Pretty pretty = new Pretty();
		
		/* Pretty Print personas */
		try{
		    pretty.print("autoridades-personas.rdf","pretty-personas.rdf");
		    pretty.print("autoridades-fechas.rdf","pretty-fechas.rdf");
		}catch{
			//blah
		}
		/* Pretty Print eventos */
		try{
    		pretty.print("autoridades-eventos.rdf","pretty-eventos.rdf");
		}catch{
			//
		}
		/* Pretty Print locations */
		try{
			pretty.print("localidades.rdf","pretty-localidades.rdf");
		}catch{

		}
		/* Pretty Print obras */
		try{
			pretty.print("obra.rdf","pretty-obra.rdf");
			pretty.print("expresion.rdf","pretty-expresion.rdf");
			pretty.print("manifestacion.rdf","pretty-manifestacion.rdf");	
		}catch{

		}
		/* Pretty Print corporativos */
		try{
			pretty.print("autoridades-corporativos.rdf","pretty-corporativos.rdf");
		}catch{

		}
	}
}
