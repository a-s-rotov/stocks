package com.example.stocks.subscriber;

import com.example.stocks.dto.HistoricalOrderbook;
import com.example.stocks.enums.EntityType;
import com.example.stocks.service.orderbook.OrderbookStorageService;
import java.time.OffsetDateTime;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.example.unicast.AsyncSubscriber;
import ru.tinkoff.invest.openapi.models.streaming.StreamingEvent;


public class StreamingApiSubscriber extends AsyncSubscriber<StreamingEvent> {


  private final Logger logger;
  private final OrderbookStorageService orderbookStorageService;

  public StreamingApiSubscriber(@NotNull final Logger logger, @NotNull final Executor executor,
                                @NotNull OrderbookStorageService orderbookStorageService) {
    super(executor);
    this.logger = logger;
    this.orderbookStorageService = orderbookStorageService;
  }

  @Override
  protected boolean whenNext(final StreamingEvent event) {
    EntityType entityType = EntityType.valueOf(event.getClass().getSimpleName().toUpperCase());
    switch (entityType) {
      case CANDLE:
        logger.info("New candle event received from Streaming API\n" + event);
        break;
      case ORDERBOOK:
        logger.info("New orderbook event received from Streaming API\n" + event);
        orderbookStorageService.putItem(HistoricalOrderbook.builder()
                .orderbook((StreamingEvent.Orderbook) event)
                .time(OffsetDateTime.now())
                .build());
        break;

    }


    return true;
  }

}