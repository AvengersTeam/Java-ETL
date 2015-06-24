package main.java.cl.uchile.datos;

import java.util.ArrayList;

import main.java.utils.Pretty;
import main.java.utils.ETLLogger;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
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
		new File("log").mkdirs();
		new File("output").mkdirs();
		Logger log = ETLLogger.getLog("log/Migrator.txt");
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
		}catch(FileNotFoundException e){
			String error = "No se pudo encontrar el archivo: " + inputFilename;
			log.warning(error);
			System.out.println(error);
		}catch(XMLStreamException e){
			String error = "Hubo un error con la biblioteca StAX";
			log.warning(error);
			System.out.println(error);
		}
		/* Run ETL eventos */
		
		inputFilename = "input/autoridades-eventos.xml";
		outputFilename = "output/autoridades-eventos.rdf";
		try{
			EventETL e = new EventETL(inputFilename, outputFilename);
			e.parseAndWrite();
		}catch(FileNotFoundException e){
			String error = "No se pudo encontrar el archivo: " + inputFilename;
			log.warning(error);
			System.out.println(error);
		}catch(XMLStreamException e){
			String error = "Hubo un error con la biblioteca StAX";
			log.warning(error);
			System.out.println(error);
		}
		
		/* RUT ETL localidades */
	
		outputFilename = "output/localidades.rdf";
		try{
			LocationETL l = new LocationETL(outputFilename);
			l.parseAndWrite();
		}catch(FileNotFoundException e){
			String error = "No se pudo encontrar el archivo: " + inputFilename;
			log.warning(error);
			System.out.println(error);
		}catch(XMLStreamException e){
			String error = "Hubo un error con la biblioteca StAX";
			log.warning(error);
			System.out.println(error);
		}
		
		/* RUN ETL corporativos */
		
		inputFilename = "input/autoridades-corporativos.xml";
		outputFilename = "output/autoridades-corporativos.rdf";
		try{
			CorporateETL c = new CorporateETL(inputFilename, outputFilename);
			c.parseAndWrite();
		}catch(FileNotFoundException e){
			String error = "No se pudo encontrar el archivo: " + inputFilename;
			log.warning(error);
			System.out.println(error);
		}catch(XMLStreamException e){
			String error = "Hubo un error con la biblioteca StAX";
			log.warning(error);
			System.out.println(error);
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
		}catch(FileNotFoundException e){
			String error = "No se pudo encontrar el archivo: " + inputFilename;
			log.warning(error);
			System.out.println(error);
		}catch(XMLStreamException e){
			String error = "Hubo un error con la biblioteca StAX";
			log.warning(error);
			System.out.println(error);
		}
		/* Inicio instancia de Pretty */
		Pretty pretty = new Pretty();
		
		/* Pretty Print personas */
		try{
		    pretty.print("autoridades-personas.rdf","pretty-personas.rdf");
		    pretty.print("autoridades-fechas.rdf","pretty-fechas.rdf");
		}catch(Exception e){
			String error = "Algo salió mal al indentar los archivos: " + "autoridades-personas.rdf" + ", " + "autoridades-fechas.rdf" ;
			log.warning(error);
			System.out.println(error);
		}
		/* Pretty Print eventos */
		try{
    		pretty.print("autoridades-eventos.rdf","pretty-eventos.rdf");
		}catch(Exception e){
			String error = "Algo salió mal al indentar el archivo: " + "autoridades-eventos.rdf";
			log.warning(error);
			System.out.println(error);
		}
		/* Pretty Print locations */
		try{
			pretty.print("localidades.rdf","pretty-localidades.rdf");
		}catch(Exception e){
			String error = "Algo salió mal al indentar el archivo: " + "localidades.rdf" ;
			log.warning(error);
			System.out.println(error);
		}
		/* Pretty Print obras */
		try{
			pretty.print("obra.rdf","pretty-obra.rdf");
			pretty.print("expresion.rdf","pretty-expresion.rdf");
			pretty.print("manifestacion.rdf","pretty-manifestacion.rdf");	
		}catch(Exception e){
			String error = "Algo salió mal al indentar los archivos: " + "obra.rdf" + ", " + "expresion.rdf" + ", " + "manifestacion.rdf";
			log.warning(error);
			System.out.println(error);
		}
		/* Pretty Print corporativos */
		try{
			pretty.print("autoridades-corporativos.rdf","pretty-corporativos.rdf");
		}catch(Exception e){
			String error = "Algo salió mal al indentar el archivo: " + "autoridades-corporativos.rdf" ;
			log.warning(error);
			System.out.println(error);
		}
	}
}
