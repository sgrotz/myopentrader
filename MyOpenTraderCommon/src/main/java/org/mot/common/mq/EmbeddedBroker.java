package org.mot.common.mq;

import org.apache.activemq.apollo.broker.Broker;
import org.apache.activemq.apollo.broker.store.leveldb.dto.*;
import org.apache.activemq.apollo.dto.*;
import org.apache.activemq.apollo.openwire.dto.OpenwireDTO;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.mot.common.tools.PropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class EmbeddedBroker {
	
	static Logger logger = LoggerFactory.getLogger(EmbeddedBroker.class);

    public static void main(String[] args) throws Exception {

    	
		Options options = new Options();
		options.addOption("c", true, "Config directory");
		
		if (args.length == 0) {
			System.out.println("*** Missing arguments: ***");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "runMyOpenTraderCore.sh|.bat", options );
			System.exit(0);
		}
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse( options, args);
		
		String confDir = "conf";
		// Get the configuration directory
		if (cmd.getOptionValue("c") != null) {
			confDir = cmd.getOptionValue("c");
		} 
    	
		PropertiesFactory pf = PropertiesFactory.getInstance();
		pf.setConfigDir(confDir);

		System.setProperty("PathToConfigDir", pf.getConfigDir());
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		
		// Read in the properties
		Configuration brokerProperties = new PropertiesConfiguration(confDir + "/activemq.properties");

		// Make sure to set the global property and initiate logging
		PropertyConfigurator.configure(pf.getConfigDir() + "/log4j.properties");
		logger.debug("Setting PathToCoreConfigDir property to: " + confDir);	
    	
        //
        // Creating and initially configuring the broker.
        Broker broker = new Broker();
        broker.setTmp(new File("./tmp"));
        
        String url = brokerProperties.getString("activemq.connection.URL", "tcp://localhost:61613");
        String adminURL = brokerProperties.getString("activemq.connection.adminURL", "http://localhost:61610");
        String dataDir = brokerProperties.getString("activemq.data.dir", "./data");
    
        broker.setConfig(createConfig(url, adminURL, dataDir));
        System.out.println("*****************");
        System.out.println("Broker Name: " + broker.toString());
        System.out.println("Broker URL: " + url);
        System.out.println("Broker Admin URL: " + adminURL);
        System.out.println("Setting data directory to: " + dataDir);
        System.out.println("*****************");
        
        broker.init_logs();
        
        //
        // The broker starts asynchronously. The runnable is invoked once
        // the broker if fully started.
        System.out.println("Starting the broker.");
        broker.start(new Runnable(){
            public void run() {
                System.out.println("The broker has now started.");
                System.out.println("Press any button to exit ...");
            }
        });
        
        
        
        System.in.read();
        
        //
        // The broker stops asynchronously. The runnable is invoked once
        // the broker if fully stopped.
        broker.stop(new Runnable(){
            public void run() {
                System.out.println("The broker has now stopped.");
            }
        });
        
        
    }

    /**
     * Builds a simple configuration model with just plain Java.  Corresponds 1 to 1 with
     * the XML configuration model.  See the Apollo user guide for more details.
     * @return
     */
    private static BrokerDTO createConfig(String url, String adminURL, String dataDir) {
        BrokerDTO broker = new BrokerDTO();

        // Brokers support multiple virtual hosts.
        VirtualHostDTO host = new VirtualHostDTO();
        host.id = "localhost";
        host.host_names.add("localhost");
        host.host_names.add("127.0.0.1");

        // The message store is configured on the virtual host.
        LevelDBStoreDTO store = new LevelDBStoreDTO();
        store.directory = new File(dataDir);
        host.store = store;

        broker.virtual_hosts.add(host);

        //
        // Control which ports and protocols the broker binds and accepts
        AcceptingConnectorDTO connector = new AcceptingConnectorDTO();
        connector.id = "tcp";
        connector.enabled = true;
        connector.bind = url;
        connector.protocols.add(new OpenwireDTO() );
        broker.connectors.add(connector);
       
        //broker.authentication.enabled = false;
        AuthenticationDTO ad = new AuthenticationDTO();
        ad.enabled = false;
        broker.authentication = ad;
        
        
        //
        // Fires up the web admin console on HTTP.
        WebAdminDTO webadmin = new WebAdminDTO();
        webadmin.bind = adminURL;
        broker.web_admins.add(webadmin); 

        return broker;
    }

}
