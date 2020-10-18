package com.example.stocks.service.candle;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

@Slf4j
@Service
@Profile("local")
@RequiredArgsConstructor
public class LocalCandleStorageService implements CandleStorageService {

  private final ObjectMapper objectMapper;

  @Override
  public HistoricalCandles getDayCandles(String figi, CandleInterval interval) {
    try {
      File file = new ClassPathResource("5min_candel_BBG000BQDF10.json").getFile();
      return objectMapper.readValue(file, HistoricalCandles.class);
    } catch (IOException e) {
      log.error("Error file loading", e);
      return null;
    }
  }

  @Override
  public void evictDayCandlesCache(String figi, CandleInterval interval) {

  }
}
