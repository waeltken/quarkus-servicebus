package de.waltken;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusErrorContext;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import java.util.Optional;
import java.util.function.Consumer;
import io.quarkus.logging.Log; 

public class ServiceBusReader {
	private final String queueName = "messages";

	@Inject
	private ServiceBusClientBuilder builder;
	private ServiceBusProcessorClient processorClient;

	public void onStart(@Observes StartupEvent event) {
		if (Optional.ofNullable(System.getenv("READ_AZURE_SERVICEBUS")).isPresent())
			startClient();
	}

	public void onStop(@Observes ShutdownEvent event) {
		if (processorClient != null) {
			processorClient.close();
		}
	}

	private void startClient() {
		processorClient = builder
			.processor()
			.queueName(queueName)
			.processMessage(new Consumer<ServiceBusReceivedMessageContext>() {
				@Override
				public void accept(ServiceBusReceivedMessageContext context) {
					onMessage(context);
				}
			})
			.processError(new Consumer<ServiceBusErrorContext>() {
				@Override
				public void accept(ServiceBusErrorContext context) {
					onError(context);
				}
			})
			.buildProcessorClient();

		processorClient.start();
	}

	private void onMessage(ServiceBusReceivedMessageContext context) {
		ServiceBusReceivedMessage message = context.getMessage();
    Log.info("Received message: " + message.getBody().toString());
		Log.info("Waiting 5 seconds before completing the message...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			context.abandon();
			e.printStackTrace();
      throw new RuntimeException(e);
		}
    Log.info("Completed!");
	}

	private void onError(ServiceBusErrorContext context) {
		// Handle errors
	}
}
