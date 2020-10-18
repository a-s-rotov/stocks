package com.example.stocks.service.candle;

import com.example.stocks.dto.SupportPoint;
import com.example.stocks.enums.Flow;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.models.market.Candle;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;

@Service
public class AnalyzerService {

  public List<SupportPoint> getTrends(HistoricalCandles historicalCandles) {
    Flow currentTrend = null;
    List<SupportPoint> supportPoints = new ArrayList<>();
    Candle supportCandle = null;
    Candle startCandle = historicalCandles.candles.get(0);
    supportPoints.add(getSupportPoint(startCandle));
    for (Candle candle : historicalCandles.candles) {
      if (supportCandle == null) {
        supportCandle = candle;
      } else {
        if (currentTrend == null) {
          currentTrend = getTrend(supportCandle, candle);
        } else {
          Flow trend = getTrend(supportCandle, candle);
          if (currentTrend != trend) {
            currentTrend = null;
            supportPoints.add(getSupportPoint(supportCandle));
            supportPoints.add(getSupportPoint(candle));
          }
          supportCandle = candle;
        }

      }
    }
    Candle endCandle = historicalCandles.candles.get(historicalCandles.candles.size()-1);
    supportPoints.add(getSupportPoint(endCandle));
    return supportPoints;
  }

  private Flow getTrend(Candle firstCandle, Candle secondCandle) {
    if (firstCandle.lowestPrice.compareTo(secondCandle.lowestPrice) < 0) {
      return Flow.UP;
    } else {
      return Flow.DOWN;
    }
  }

  private SupportPoint getSupportPoint(Candle candle) {
    return SupportPoint.builder()
        .time(candle.time)
        .value(candle.lowestPrice)
        .build();
  }
}
