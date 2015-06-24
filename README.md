# Java-ETL
Repositorio con el código para realizar la migración de los datos a formato RDF

Requiere Java1.8

Tutorial instalación
--------------------

En la carpeta padre ejecutar:

```bash
> git clone https://github.com/AvengersTeam/Java-ETL.git
```

Tutorial ejecución
------------------
* Con maven:

```bash
> mvn clean
> mvn install
> mvn exec:java -Dexec.mainClass="main.java.cl.uchile.datos.Migrator"
```

* Con eclipse:

Run As -> Java Application

Precaución: En caso de correr manualmente solo algunos ETL, se comenta en la clase Migrator las secciones que no se requieren. Cabe mencionar que para correr el ETL de Obras, es necesario haber generado previamente el RDF de Personas obtenido por PersonETL.
