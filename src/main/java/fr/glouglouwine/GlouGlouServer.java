package fr.glouglouwine;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import fr.glouglouwine.jmx.JmxManager;
import fr.glouglouwine.resources.ManagedGlouGlouResource;

public class GlouGlouServer {

	private int serverPort;
	private Server server;

	public GlouGlouServer(int port) {
		this.serverPort = port;
	}

	public static void main(String[] args) throws Exception {
		new GlouGlouServer(8080).start();
	}

	private void start() throws Exception {
		server = new Server(serverPort);

		ResourceConfig config = initializeResources();

		initializeServlet(config);

		initializeMetrics();

		initializeCustomJmxOperations();

		server.start();
		// makes the main thread wait for server thread terminiation
		server.join();
	}

	private void initializeCustomJmxOperations() {
		JmxManager.INSTANCE.register(ManagedGlouGlouResource.INSTANCE);
	}

	private void initializeMetrics() {
		MetricsHolder.INSTANCE.startJmxExporter();
	}

	private void initializeServlet(ResourceConfig config) {
		ServletContainer servletContainer = new ServletContainer(config);
		ServletContextHandler context = new ServletContextHandler(server, "/*");

		ServletHolder servlet = new ServletHolder(servletContainer);
		context.addServlet(servlet, "/api/*");
	}

	private ResourceConfig initializeResources() {
		ResourceConfig config = new ResourceConfig();
		config.packages("fr.glouglouwine", "com.jersey.jaxb", "com.fasterxml.jackson.jaxrs.json");
		return config;
	}

}
