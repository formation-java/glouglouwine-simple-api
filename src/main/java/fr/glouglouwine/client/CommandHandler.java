package fr.glouglouwine.client;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import fr.glouglouwine.domain.Bottle;
import fr.glouglouwine.domain.GrapeTypes;
import fr.glouglouwine.resources.DrinkDTO;

public class CommandHandler {
	private Client client = ClientBuilder.newClient();

	public void help() {
		System.out.println("Operations");
		System.out.println("list : return list of all stored bottles");
		System.out.println("get <id> : return bottles stored at <id>");
		System.out.println("add <bottleId> <owner> <grapeType>, <domain>, <year> : store a bottle");
		System.out.println("drink <bottleId> <quantity> : drink a litle bit of wine");
		System.out.println("exit : quit application");
		System.out.println("help : display help");
		System.out.println("<grapeType> = \"CABERNET\"|\"GRENACHE\"|\"MALBEC\"|\"MERLOT\"|\"PINOT_NOIR\"|\"SYRAH\"");
	}

	public void list() {
		WebTarget resource = client.target("http://localhost:8080/api/glouglou/bottles");
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		Response response = request.get();
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			System.out.println(response.readEntity(List.class));
		} else {
			System.out.println("ERROR! " + response.getStatus());
		}

	}

	public void get(String id) {
		WebTarget resource = client.target("http://localhost:8080/api/glouglou/bottles/" + id);
		Builder request = resource.request();
		request.accept(MediaType.APPLICATION_JSON);
		Response response = request.get();
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			Bottle b = response.readEntity(Bottle.class);
			System.out.println("Bottle #" + b.getBottleId() + ", owned by " + b.getOwner() + ", from " + b.getDomain()
					+ ", remaining quantity" + b.getQuantity() + "%");
		} else {
			System.out.println("ERROR! " + response.getStatus());
		}
	}

	// {"bottleId" : "1","owner" : "me", "grapeType": "CABERNET", "domain":"d",
	// "year": "1900"}
	public void add(String bottleId, String owner, String grapeType, String domain, String year) {
		Bottle bottleToAdd = new Bottle(Long.valueOf(bottleId), owner, GrapeTypes.valueOf(grapeType), domain, year);
		WebTarget resource = client.target("http://localhost:8080/api/glouglou/bottles");
		Builder request = resource.request();
		Response response = request.post(Entity.entity(bottleToAdd, MediaType.APPLICATION_JSON));
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			System.out.println("Successfully added! " + response.getStatus());
		} else {
			System.out.println("ERROR! " + response.getStatus());
		}

	}

	// {"quantity" : "20", "dateTime": "2016-10-14T13:16:53.735"}
	public void drink(String bottleId, String quantity) {
		WebTarget resource = client.target("http://localhost:8080/api/glouglou/bottles/" + bottleId + "/drink");
		DrinkDTO drinkData = new DrinkDTO();
		drinkData.quantity = Integer.valueOf(quantity);
		drinkData.dateTime = LocalDateTime.now();
		Builder request = resource.request();
		Response response = request.post(Entity.entity(drinkData, MediaType.APPLICATION_JSON));
		if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
			System.out.println("Successfully added! " + response.getStatus());
		} else {
			System.out.println("ERROR! " + response.getStatus());
		}
	}

	public void exit() {
		System.exit(0);
	}
}