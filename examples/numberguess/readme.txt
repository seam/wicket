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
