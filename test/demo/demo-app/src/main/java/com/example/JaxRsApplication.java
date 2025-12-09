package com.example;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/resources")
public class JaxRsApplication extends Application {

  @Liveness
  @Produces
  public HealthCheck aliveness() {
    return () -> HealthCheckResponse.up("live " + getClass().getSimpleName());
  }

  @Readiness
  @Produces
  public HealthCheck readiness() {
    return () -> HealthCheckResponse.up("ready " + getClass().getSimpleName());
  }
}
