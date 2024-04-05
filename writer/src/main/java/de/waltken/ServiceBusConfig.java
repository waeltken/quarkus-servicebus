package de.waltken;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

public class ServiceBusConfig {

	private final String queueName = "messages";
	private final String fqNamespace = "clwaltke.servicebus.windows.net";

	private final ServiceBusClientBuilder builder;

	private final ServiceBusSenderClient sender;

	public ServiceBusConfig() {
		DefaultAzureCredential credential = new DefaultAzureCredentialBuilder()
				.build();

		builder = new ServiceBusClientBuilder()
				.fullyQualifiedNamespace(fqNamespace)
				.credential(credential);

		sender = builder.sender().queueName(queueName).buildClient();
	}
	
	@ApplicationScoped
	public ServiceBusClientBuilder builder() {
		return builder;
	}

	@ApplicationScoped
	public ServiceBusSenderClient sender() {
		return sender;
	}

	void onStop(@Observes ShutdownEvent ev) {
		sender.close();
	}
}
