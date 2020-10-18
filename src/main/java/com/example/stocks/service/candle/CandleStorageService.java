package com.example.stocks.service.candle;

import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

public interface CandleStorageService {
  HistoricalCandles getDayCandles(String figi, CandleInterval interval);
  void evictDayCandlesCache(String figi, CandleInterval interval);
}
