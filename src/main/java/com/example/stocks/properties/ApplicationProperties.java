package com.example.stocks.properties;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "project")
@Data
public class ApplicationProperties {

  private String token;
  private List<String> figis;
  private FlightRecorder flightRecorder;

  @Data
  public static class FlightRecorder {
    private boolean enable;
  }

}
