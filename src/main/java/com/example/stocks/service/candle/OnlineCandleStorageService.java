package com.example.stocks.service.candle;

import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

@Profile("!local")
@Service
@RequiredArgsConstructor
public class OnlineCandleStorageService implements CandleStorageService {

  private final OpenApi openApi;

  @Cacheable("dayCandles")
  public HistoricalCandles getDayCandles(String figi, CandleInterval interval) {
    Optional<HistoricalCandles> historicalCandles = openApi.getMarketContext()
        .getMarketCandles(figi, OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), interval).join();
    return historicalCandles.get();
  }

  @CacheEvict("dayCandles")
  public void evictDayCandlesCache(String figi, CandleInterval interval){}

}
