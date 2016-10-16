package fr.glouglouwine.resources;

import java.io.IOException;
import java.net.ServerSocket;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.glouglouwine.GlouGlouServer;
import fr.glouglouwine.domain.Bottle;
import fr.glouglouwine.domain.GrapeTypes;

public class GlouGlouResourceTest {

	GlouGlouServer serverUnderTest;
	Integer actualServerPort;

	@Before
	public void startServer() throws Exception {
		actualServerPort = findRandomOpenPortOnAllLocalInterfaces();
		serverUnderTest = new GlouGlouServer(actualServerPort);
		serverUnderTest.start();
	}

	@After
	public void stopServer() throws Exception {
		serverUnderTest.stop();
	}

	@Test
	public void testBasicScenario() throws Exception {
		Client client = ClientBuilder.newClient();
		Bottle bottleToAdd = new Bottle(3, "owner", GrapeTypes.CABERNET, "domain", "1980");
		WebTarget resource = client.target(getBaseUrl() + "/api/glouglou/bottles");
		Builder request = resource.request();
		Response response = request.post(Entity.entity(bottleToAdd, MediaType.APPLICATION_JSON));
		Assert.assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
		Assert.assertEquals(204, response.getStatus());
		resource = client.target(getBaseUrl() + "/api/glouglou/bottles/3");
		request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		response = request.get();
		Assert.assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
		Bottle b = response.readEntity(Bottle.class);
		Assert.assertEquals("domain", b.getDomain());
		Assert.assertEquals("1980", b.getYear());
		resource = client.target(getBaseUrl() + "/api/glouglou/bottles/1");
		request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		response = request.get();
		Assert.assertEquals(Family.CLIENT_ERROR, response.getStatusInfo().getFamily());
		Assert.assertEquals(404, response.getStatus());
	}

	private String getBaseUrl() {
		return "http://localhost:" + actualServerPort;
	}

	private Integer findRandomOpenPortOnAllLocalInterfaces() throws IOException {
		try (ServerSocket socket = new ServerSocket(0);) {
			return socket.getLocalPort();

		}
	}

}
