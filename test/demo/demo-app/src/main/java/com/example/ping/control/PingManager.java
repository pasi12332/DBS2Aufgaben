package com.example.ping.control;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Map;

@Default
public class PingManager {
  private final String message;

  @Inject
  public PingManager(@ConfigProperty(name = "com.example.message", defaultValue = "Hello, World!") String message) {
    this.message = message;
  }

  public Map<String, Object> getPing() {
    return Map.of("message", message, "dateTimeEpochMilli", Instant.now().toEpochMilli());
  }
}
