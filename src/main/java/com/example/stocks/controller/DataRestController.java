package com.example.stocks.controller;

import com.example.stocks.dto.CandlesChart;
import com.example.stocks.service.candle.AnalyzerService;
import com.example.stocks.service.candle.CandleStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

@RestController
@RequiredArgsConstructor
public class DataRestController {

  private final AnalyzerService analyzerService;
  private final CandleStorageService candleStorageService;


  @GetMapping("candles/{figi}")
  public CandlesChart getHistoricalCandles(@PathVariable String figi)  {
    HistoricalCandles historicalCandles = candleStorageService.getDayCandles(figi, CandleInterval.ONE_MIN);
    return CandlesChart.builder()
        .historicalCandles(historicalCandles)
        .supportPoints(analyzerService.getTrends(historicalCandles))
        .build();
  }
}
