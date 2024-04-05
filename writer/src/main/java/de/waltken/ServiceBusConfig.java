package de.waltken;

import java.util.Optional;
import java.util.function.Supplier;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

public class ServiceBusConfig {

	private final String queueName;
	private final String fqNamespace;

	private final ServiceBusClientBuilder builder;

	private final ServiceBusSenderClient sender;

	public ServiceBusConfig() {
		DefaultAzureCredential credential = new DefaultAzureCredentialBuilder()
				.build();

		queueName = Optional.ofNullable(System.getenv("QUEUE_NAME")).orElse("messages");
		fqNamespace = Optional.ofNullable(System.getenv("AZURE_SERVICEBUS_FQDN"))
				.orElseThrow(new Supplier<IllegalStateException>() {
					@Override
					public IllegalStateException get() {
						return new IllegalStateException("AZURE_SERVICEBUS_FQDN must be set");
					}
				});

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
