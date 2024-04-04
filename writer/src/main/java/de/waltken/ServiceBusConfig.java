package de.waltken;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceBusConfig {

	private final String queueName = "messages";
	private final String fqNamespace = "clwaltke.servicebus.windows.net";

	private final ServiceBusClientBuilder builder;

	private final ServiceBusSenderClient sender;
	private final ServiceBusReceiverClient receiver;

	public ServiceBusConfig() {
		DefaultAzureCredential credential = new DefaultAzureCredentialBuilder()
				.build();

		builder = new ServiceBusClientBuilder()
				.fullyQualifiedNamespace(fqNamespace)
				.credential(credential);

		sender = builder.sender().queueName(queueName).buildClient();
		receiver = builder.receiver().queueName(queueName).buildClient();
	}

	@ApplicationScoped
	public ServiceBusSenderClient sender() {
		return sender;
	}

	@ApplicationScoped
	public ServiceBusReceiverClient receiver() {
		return receiver;
	}
}
