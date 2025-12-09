package com.example.ping.boundaray;

import com.example.ping.control.PingManager;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@RequestScoped
@Path("/ping")
@Produces({MediaType.APPLICATION_JSON})
public class PingResource {

  @Inject
  PingManager pingManager;

  @GET
  public Map<String, Object> ping() {
    return pingManager.getPing();
  }
}
