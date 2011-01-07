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

1. ensure that the JBOSS_HOME environment variable resolves to the JBoss AS 6 installation directory
2. Start JBoss AS 6
3. From the commandline, run:

  mvn package jboss:hard-deploy

4. Visit http://localhost:8080/seam-wicket-example-numberguess in your browser
5. Start guessing!
