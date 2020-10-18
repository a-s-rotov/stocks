package com.example.stocks.controller;

import com.example.stocks.dto.CandlesChart;
import com.example.stocks.service.AnalyzerService;
import com.example.stocks.service.OnlineStorageService;
import com.example.stocks.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

@RestController
@RequiredArgsConstructor
public class DataRestController {

  private final AnalyzerService analyzerService;
  private final StorageService storageService;


  @GetMapping("candles/{figi}")
  public CandlesChart getHistoricalCandles(@PathVariable String figi)  {
    HistoricalCandles historicalCandles = storageService.getDayCandles(figi, CandleInterval.ONE_MIN);
    return CandlesChart.builder()
        .historicalCandles(historicalCandles)
        .supportPoints(analyzerService.getTrends(historicalCandles))
        .build();
  }
}
