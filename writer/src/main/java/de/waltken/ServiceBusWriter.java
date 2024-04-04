package de.waltken;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import com.azure.messaging.servicebus.*;
import com.azure.identity.*;

@Path("/")
public class ServiceBusWriter {
	
	private final String queueName = "messages";
	private final String fqNamespace = "clwaltke.servicebus.windows.net";
  private final ServiceBusSenderClient senderClient;
	
	public ServiceBusWriter() {
    DefaultAzureCredential credential = new DefaultAzureCredentialBuilder()
            .build();

    senderClient = new ServiceBusClientBuilder()
            .fullyQualifiedNamespace(fqNamespace)
            .credential(credential)
            .sender()
            .queueName(queueName)
            .buildClient();
	}

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