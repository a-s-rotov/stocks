package com.example.stocks.service.orderbook;

import static com.example.stocks.util.TimeUtil.roundFiveMinute;
import com.example.stocks.dto.HistoricalOrderbook;
import com.example.stocks.properties.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OrderbookStorageService {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ApplicationProperties applicationProperties;


  private ConcurrentMap<String, List<HistoricalOrderbook>> storage;

  @PostConstruct
  public void init() {
    storage = new ConcurrentHashMap<>();
  }


  public void putItem(HistoricalOrderbook item) {
    List<HistoricalOrderbook> orderbooks = storage.get(roundFiveMinute(item.getTime()));
    if (orderbooks == null) {
      orderbooks = new ArrayList<>();
    }
    orderbooks.add(item);
    storage.put(roundFiveMinute(item.getTime()), orderbooks);
  }

  @Scheduled(fixedRate = 6000)
  public void scheduleTask() throws IOException {
    OffsetDateTime currentTime = OffsetDateTime.now();
    String currentValue = roundFiveMinute(currentTime);
    Iterator<Map.Entry<String, List<HistoricalOrderbook>>> iterator = storage.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry<String, List<HistoricalOrderbook>> entry = iterator.next();
      if (!entry.getKey().equals(currentValue)) {

        if (applicationProperties.getFlightRecorder().isEnable()) {
          FileOutputStream fileOutputStream =
              new FileOutputStream(Path.of(currentTime.getHour() + currentValue + "_data.json").toFile(), true);
          fileOutputStream.write('[');
          for (List<HistoricalOrderbook> value : storage.values()) {
            String string = objectMapper.writeValueAsString(value);
            fileOutputStream.write(string.getBytes());
          }
          fileOutputStream.write(']');
          fileOutputStream.close();
        }

        storage.remove(entry.getKey());
      }
    }

  }

}
