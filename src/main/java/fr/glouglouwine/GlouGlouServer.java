package fr.glouglouwine;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class GlouGlouServer {

	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);

		ResourceConfig config = new ResourceConfig();
		config.packages("fr.glouglouwine", "com.jersey.jaxb", "com.fasterxml.jackson.jaxrs.json");

		ServletContainer servletContainer = new ServletContainer(config);
		ServletContextHandler context = new ServletContextHandler(server, "/*");

		ServletHolder servlet = new ServletHolder(servletContainer);
		context.addServlet(servlet, "/api/*");

		server.start();
		// makes the main thread wait for server thread terminiation
		server.join();

	}

}
