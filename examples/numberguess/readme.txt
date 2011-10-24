This example follows the Wicket standard usage of running from within Eclipse
using Jetty.

To generate an standard eclipse project, run 

  mvn -Pjetty eclipse:eclipse

If you have m2eclipse installed, then import the project as an existing Maven
project, then select Project > Properties > Maven and enter "jetty" in the
active profiles input field.

Within Eclipse, right-click on Start.java in the project and choose "Run as
Java Application", which will launch jetty with the example.

Finally, navigate to http://localhost:9090/ to find the application running.

To run the application on JBoss AS 6:
-------------------------------------

1. ensure that the JBOSS_HOME environment variable resolves to the JBoss AS 6 installation directory
2. From the commandline, run:

 mvn clean package arquillian:run -Darquillian=jbossas-managed-6
 
3. Visit http://localhost:8080/wicket-numberguess in your browser
4. Start guessing!

To run the application on JBoss AS 7:
-------------------------------------
same steps as for JBoss AS 6, but from the commandline run:

 mvn clean package arquillian:run -Darquillian=jbossas-managed-7
 
To run the application on Glassfish:
------------------------------------
1. Start the GlassFish
2. From the commandline run:

 mvn clean package arquillian:run -Darquillian=glassfish-remote-3.1
 

Running functional tests from commandline
==========================================
You can use following configurations:

mvn clean verify -Darquillian=jbossas-managed-7
mvn clean verify -Darquillian=jbossas-remote-7

mvn clean verify -Darquillian=jbossas-managed-6
mvn clean verify -Darquillian=jbossas-remote-6

mvn clean verify -Darquillian=glassfish-remote-3.1

