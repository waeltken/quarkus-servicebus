package de.waltken;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Path("/health")
class Health {
  @GET
  @Produces("application/json")
  public String health() {
    JsonObject jsonObject = Json.createObjectBuilder()
        .add("status", "up")
        .build();
    return jsonObject.toString();
  }
}
