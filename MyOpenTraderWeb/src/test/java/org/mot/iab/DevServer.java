package org.mot.iab;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;


/**
 * @author stephan
 * This is just a small dev server, for development purposes. It doesnt require recompiling/packaging the war file all the time ;)
 */
public class DevServer { 
	    public static void main(String[] args) throws Exception {
	        Server server = new Server();
	        ServerConnector connector = new ServerConnector(server);

	        // Set some timeout options to make debugging easier.
	        connector.setSoLingerTime(-1);
	        connector.setPort(8082); 
	        server.addConnector(connector);

	        WebAppContext bb = new WebAppContext();
	        bb.setServer(server);
	        bb.setContextPath("/");
	        bb.setWar("src/main/webapp");

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
