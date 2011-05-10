package org.jboss.seam.wicket.examples.numberguess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.Resource;
import org.mortbay.xml.XmlConfiguration;

public class Start {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        SocketConnector connector = new SocketConnector();
        connector.setPort(9090);
        server.setConnectors(new Connector[]{connector});

        WebAppContext bb = new WebAppContext();
        bb.setServer(server);
        bb.setContextPath("/");
        bb.setWar("src/main/webapp");
        bb.setOverrideDescriptor("src/main/webapp/WEB-INF/jetty-web-fragment.xml");

        List<String> configurationClasses = new ArrayList<String>();
        configurationClasses.add("org.mortbay.jetty.plus.webapp.EnvConfiguration");
        configurationClasses.addAll(Arrays.asList(bb.getConfigurationClasses()));
        bb.setConfigurationClasses(configurationClasses.toArray(new String[0]));

        Resource jettyEnvXml = Resource.newResource("src/main/webapp/WEB-INF/jetty-env.xml");
        XmlConfiguration config = new XmlConfiguration(jettyEnvXml.getURL());
        config.configure(bb);

        server.addHandler(bb);

        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            while (System.in.available() == 0) {
                Thread.sleep(500);
            }
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
