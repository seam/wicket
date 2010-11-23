This example follows the wicket standard usage of running from within eclipse
with jetty.   To generate an eclipse project, run 

	mvn -Pjetty eclipse:eclipse

Then, in eclipse, right-click on Start.java in the project and choose "Run as Java Application," 
which will launch jetty with the example.  Then hit http://localhost:8080/
