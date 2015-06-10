package cl.uchile.datos;

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
		*/
		/* RUT ETL localidades */
		/*
		outputFilename = "output/localidades.rdf";
		LocationETL l = new LocationETL(outputFilename);
		l.parse();
		*/
		/* RUN ETL corporativos */
		inputFilename = "input/autoridades-corporativos.xml";
		outputFilename = "output/autoridades-corporativos.rdf";
		long corporateStartTime = System.currentTimeMillis();
		CorporateETL c = new CorporateETL(inputFilename, outputFilename);
		c.parse();
		long corporateElapsedTime = System.currentTimeMillis() - corporateStartTime;
		
		/* RUN ETL obras */
		
		/*inputFilename = "Portfolio-Andres-bello.xml";
		outputFilename = "output/obra.rdf";
		outputFilename = "output/manifestacion.rdf";
		outputFilename = "output/expresion.rdf";
		
		ObraETL o = new ObraETL(inputFilename, outputFilename, outputFilename2, outputFilename3);
		o.parse();*/
		
		/* Inicio instancia de Pretty */
		Pretty pretty = new Pretty();
		
		/* Pretty Print personas */
        //pretty.print("autoridades-personas.rdf","pretty-personas.rdf");
		/* Pretty Print eventos */
        //pretty.print("autoridades-eventos.rdf","pretty-eventos.rdf");
		/* Pretty Print localidades */
		//pretty.print("localidades.rdf","pretty-localidades.rdf");
		/* Pretty Print corporativos */
		
	    
		pretty.print("autoridades-corporativos.rdf","pretty-corporativos.rdf");
		System.out.println("Tiempo corporativo: " + corporateElapsedTime);
		System.out.println("fin");
	}
}
