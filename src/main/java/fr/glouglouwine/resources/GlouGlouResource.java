package fr.glouglouwine.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.glouglouwine.domain.Bottle;
import fr.glouglouwine.repository.BottleRepository;

@Path("/glouglou")
public class GlouGlouResource {

	BottleRepository bottleRepository = BottleRepository.INSTANCE;

	private static Logger logger = LoggerFactory.getLogger(GlouGlouResource.class.getName());

	@GET
	@Path("bottles/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBottle(@PathParam("id") Long id) {
		logger.debug("Accessing to bottle #" + id);
		Bottle bottle = bottleRepository.getById(id);
		if(bottle == null){
			return Response.status(404).entity("Bottle not found").build();
		}
		return Response.status(200).entity(bottle).build();
	}

	@GET
	@Path("bottles")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Bottle> getBottles() {
		logger.debug("Accessing to the complet bottle collection");
		return bottleRepository.findAll();
	}

	@POST
	@Path("bottles")
	@Consumes("application/json")
	public void addBottles(Bottle bottle) {
		logger.debug("Adding bottle #" + bottle.getBottleId());
		Bottle lBottle = new Bottle(bottle.getBottleId(), bottle.getOwner(), bottle.getGrapeType(), bottle.getDomain(), bottle.getYear());
		bottleRepository.add(lBottle);
	}
	
	@POST
	@Path("bottles/{id}/drink")
	@Consumes("application/json")
	public Response drink(@PathParam("id") Long id,DrinkData drinkData) {
		logger.debug("Drinking in bottle #" + id);
		try{
		Bottle bottle = bottleRepository.getById(id);
		if(bottle == null){
			return Response.status(404).entity("Bottle not found").build();
		}
		bottle.drink(drinkData.quantity, drinkData.dateTime);
		return Response.status(204).build();
		}catch(IllegalArgumentException e){
			return Response.status(404).entity(e.getMessage()).build();
		}
	}

}
