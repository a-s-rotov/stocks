package com.example.stocks.dto;

import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.invest.openapi.models.streaming.StreamingEvent;

@Builder
@Data
public class HistoricalOrderbook {
  private StreamingEvent.Orderbook orderbook;
  private OffsetDateTime time;
}
