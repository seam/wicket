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



Running functional tests from commandline
==========================================

1.) Build and deploy the numberguess application to an application server
2.) run "mvn verify -Pftest"

Running functional tests from Eclipse
==========================================
You can run the functional tests directly from Eclipse. 
Firstly,start the selenium server.

java -jar ~/.m2/repository/org/seleniumhq/selenium/server/selenium-server/1.0.3/selenium-server-1.0.3-standalone.jar -port 14444

Then, run the test using Eclipse TestNG plugin.
It will fail at the first run. Modify the run configuration of the test
NumberGuessTest -> Run As -> Run Configurations and add the following VM 
arguments in the arguments tab:

-Dmethod=* -Dbrowser=*firefoxproxy -Dcontext.root=http://localhost:8080/ -Dcontext.path=/seam-wicket-example-numberguess/ -Dselenium.host=localhost -Dselenium.port=14444 -Dselenium.debug=false -Dselenium.maximize=false -Dselenium.timeout.default=30000 -Dselenium.timeout.gui=5000 -Dselenium.timeout.ajax=15000 -Dselenium.timeout.model=30000 -Dselenium.speed=0 -Dselenium.timeout=3000 -Dbasedir=.
