package com.example.stocks.service;

import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

public interface StorageService {
  HistoricalCandles getDayCandles(String figi, CandleInterval interval);
  void evictDayCandlesCache(String figi, CandleInterval interval);
}
