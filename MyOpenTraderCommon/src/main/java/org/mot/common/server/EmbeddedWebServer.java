package org.mot.common.server;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.mot.common.tools.PropertiesFactory;

public class EmbeddedWebServer {
	public static void main(String[] args) throws Exception {

		Options options = new Options();
		options.addOption("c", true, "Config directory");
		options.addOption("w", true, "Path to WAR file, containing web-app");
		options.addOption("p", true,
				"Specify a port to start webserver (default: 8082)");
		options.addOption("s", true,
				"Specify a SSL port to start webserver (default: 8444)");
		options.addOption("h", false, "Print the command line help");

		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		PropertiesFactory pf = PropertiesFactory.getInstance();

		if (args.length == 0 || cmd.hasOption("h")) {
			System.out.println("*** Missing arguments: ***");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("runEmbeddedWebServer.sh|.bat", options);
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
		
		String path ="../resources/MyOpenTraderWeb.war";
		// Overwrite the path to the war file
		if (cmd.getOptionValue("w") != null) {
			path = String.valueOf(cmd.getOptionValue("w"));
		}

		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);

		// Set some timeout options to make debugging easier.
		connector.setIdleTimeout(3600000);
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

			SslContextFactory factory = new SslContextFactory();
			factory.setKeyStoreResource(keystore);
			factory.setKeyStorePassword("wicket");
			factory.setTrustStoreResource(keystore);
			factory.setKeyManagerPassword("wicket");

			ServerConnector sslConnector = new ServerConnector(server,
					new SslConnectionFactory());
			sslConnector.setIdleTimeout(3600000);
			sslConnector.setPort(SSLport);
			server.addConnector(sslConnector);

			System.out
					.println("SSL access has been enabled on port " + SSLport);
			System.out
					.println("You can access the application using SSL on https://localhost:"
							+ SSLport);
			System.out.println();
		}

		WebAppContext bb = new WebAppContext();
		bb.setServer(server);
		bb.setContextPath("/");
		//bb.setWar("src/main/webapp");
		//bb.setWar("D:/Data/GitHub/myopentrader/MyOpenTraderBin/resources/MyOpenTraderWeb-0.0.15.war");
		bb.setWar(path);

		// START JMX SERVER
		// MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		// MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		// server.getContainer().addEventListener(mBeanContainer);
		// mBeanContainer.start();

		server.setHandler(bb);

		try {
			System.out
					.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
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
