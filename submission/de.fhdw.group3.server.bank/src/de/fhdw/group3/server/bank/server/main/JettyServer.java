package de.fhdw.group3.server.bank.server.main;

import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * @author Sebastian Lühnen
 * 
 */
public class JettyServer {

	/**
	 * @param args
	 * @throws Exception
	 * Der Server wird gestartet so wie Port und Adresse werden festgelegt.
	 */
	public static void main(String[] args) throws Exception {		
		Server server = new Server(9998);

		// Log4J
        Logger logger = Logger.getRootLogger();
        SimpleLayout layout = new SimpleLayout();
        ConsoleAppender appender = new ConsoleAppender(layout);
		FileAppender fileAppender = new FileAppender(layout, "logs/example.log", false);
        logger.addAppender(appender);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.ALL);
        
        logger.info("" + (new Date()) + ": " + "Server wird gestartet.");

		// JERSEY
		ResourceConfig resourceConfig = new PackagesResourceConfig("de.fhdw.group3.server.bank.server.rest");
		ServletContextHandler sh = new ServletContextHandler();
		sh.setContextPath("/rest");
		sh.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

		// Angular2
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("../de.fhdw.client.angular2.start");

        ContextHandler contextHandler = new ContextHandler("/angular");
		contextHandler.setHandler(resourceHandler);

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandler, sh });
        server.setHandler(handlers);

		server.start();
	}
}
