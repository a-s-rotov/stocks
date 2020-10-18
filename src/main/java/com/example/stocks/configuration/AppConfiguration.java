package com.example.stocks.configuration;

import com.example.stocks.properties.ApplicationProperties;
import com.example.stocks.service.orderbook.OrderbookStorageService;
import com.example.stocks.subscriber.StreamingApiSubscriber;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.streaming.StreamingRequest;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

@Log
@Profile("!local")
@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

  private final ApplicationProperties applicationProperties;
  private final OrderbookStorageService orderbookStorageService;


  @Bean
  public OpenApi init() {
    OkHttpOpenApiFactory factory = new OkHttpOpenApiFactory(applicationProperties.getToken(), log);
    OpenApi api = factory.createSandboxOpenApiClient(Executors.newSingleThreadExecutor());
    ((SandboxOpenApi) api).getSandboxContext().performRegistration(null).join();

    StreamingApiSubscriber listener =
        new StreamingApiSubscriber(log, Executors.newSingleThreadExecutor(), orderbookStorageService);

    api.getStreamingContext().getEventPublisher().subscribe(listener);


    for (String figi : applicationProperties.getFigis()) {
      api.getStreamingContext().sendRequest(StreamingRequest.subscribeCandle(figi, CandleInterval.FIVE_MIN));
      api.getStreamingContext().sendRequest(StreamingRequest.subscribeOrderbook(figi, 6));
    }

    return api;
  }


}
