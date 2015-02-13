package org.mot.web.server;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.wicket.util.time.Duration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.mot.common.tools.PropertiesFactory;

public class EmbeddedWebServer {
    public static void main(String[] args) throws Exception {
    	
    	
		Options options = new Options();
		options.addOption("c", true, "Config directory");
		options.addOption("p", true, "Specify a port to start webserver (default: 8082)");
		options.addOption("s", true, "Specify a SSL port to start webserver (default: 8444)");
		options.addOption("h", false, "Print the command line help");
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse( options, args);
		PropertiesFactory pf = PropertiesFactory.getInstance();

		if (args.length == 0 || cmd.hasOption("h")) {
			System.out.println("*** Missing arguments: ***");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runEmbeddedWebServer.sh|.bat", options );
			System.exit(0);
		}
		
		String confDir = "conf";
		// Get the configuration directory
		if (cmd.getOptionValue("c") != null) {
			confDir = cmd.getOptionValue("c");
		} 
    	
		// Set the configuration directory
		pf.setConfigDir(confDir);
		
		int port = 8082;
		// Get the configuration directory
		if (cmd.getOptionValue("p") != null) {
			port = Integer.valueOf(cmd.getOptionValue("p"));
		} 
		
		int SSLport = 8444;
		// Get the configuration directory
		if (cmd.getOptionValue("s") != null) {
			SSLport = Integer.valueOf(cmd.getOptionValue("s"));
		} 
		
    	
    	
        int timeout = (int) Duration.ONE_HOUR.getMilliseconds();

        Server server = new Server();
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(timeout);
        connector.setSoLingerTime(-1);
        connector.setPort(port); 
        server.addConnector(connector);

        System.out.println("HTTP access has been enabled on port " + port);
        
        Resource keystore = Resource.newClassPathResource("/keystore");
        if (keystore != null && keystore.exists()) {
            // if a keystore for a SSL certificate is available, start a SSL
            // connector on port 8443.
            // By default, the quickstart comes with a Apache Wicket Quickstart
            // Certificate that expires about half way september 2021. Do not
            // use this certificate anywhere important as the passwords are
            // available in the source.

            connector.setConfidentialPort(SSLport);

            SslContextFactory factory = new SslContextFactory();
            factory.setKeyStoreResource(keystore);
            factory.setKeyStorePassword("wicket");
            factory.setTrustStoreResource(keystore);
            factory.setKeyManagerPassword("wicket");
            SslSocketConnector sslConnector = new SslSocketConnector(factory);
            sslConnector.setMaxIdleTime(timeout);
            sslConnector.setPort(SSLport);
            sslConnector.setAcceptors(4);
            server.addConnector(sslConnector);

            System.out.println("SSL access has been enabled on port " + SSLport);
            System.out.println("You can access the application using SSL on https://localhost:" + SSLport );
            System.out.println();
        }

        WebAppContext bb = new WebAppContext();
        bb.setServer(server);
        bb.setContextPath("/");
        bb.setWar("src/main/webapp");
      

        // START JMX SERVER
        // MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
        // server.getContainer().addEventListener(mBeanContainer);
        // mBeanContainer.start();

        server.setHandler(bb);

        try {
            System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
            server.start();
            System.in.read();
            System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
