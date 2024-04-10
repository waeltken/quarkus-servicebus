package de.waltken;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import com.azure.messaging.servicebus.*;

@Path("/inject")
public class ServiceBusWriter {
	
	@Inject
  private ServiceBusSenderClient senderClient;
	
	@GET
	@Produces("application/json")
	public String write() {
		senderClient.sendMessage(new ServiceBusMessage("Hello, World!"));
		JsonObject jsonObject = Json.createObjectBuilder()
				.add("response", "okay")
				.build();
		return jsonObject.toString();
	}
}
