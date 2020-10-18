package com.example.stocks.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

@Data
@Builder
public class CandlesChart {
  private HistoricalCandles historicalCandles;
  private List<SupportPoint> supportPoints;
}
