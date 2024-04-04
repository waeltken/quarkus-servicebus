package de.waltken;

import jakarta.inject.Inject;

import com.azure.messaging.servicebus.*;

public class ServiceBusReader {
	@Inject
  private ServiceBusReceiverClient receiverClient;
	
}