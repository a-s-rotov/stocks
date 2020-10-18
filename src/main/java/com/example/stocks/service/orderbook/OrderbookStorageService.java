package com.example.stocks.service.orderbook;

import static com.example.stocks.util.TimeUtil.roundFiveMinute;
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
import ru.tinkoff.invest.openapi.models.streaming.StreamingEvent;

@Service
public class OrderbookStorageService {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ApplicationProperties applicationProperties;


  private ConcurrentMap<String, List<StreamingEvent.Orderbook>> storage;

  @PostConstruct
  public void init() {
    storage = new ConcurrentHashMap<>();
  }


  public void putItem(OffsetDateTime time, StreamingEvent.Orderbook item) {
    List<StreamingEvent.Orderbook> orderbooks = storage.get(roundFiveMinute(time));
    if (orderbooks == null) {
      orderbooks = new ArrayList<>();
    }
    orderbooks.add(item);
    storage.put(roundFiveMinute(time), orderbooks);
  }

  @Scheduled(fixedRate = 6000)
  public void scheduleTask() throws IOException {
    String currentValue = roundFiveMinute(OffsetDateTime.now());
    Iterator<Map.Entry<String, List<StreamingEvent.Orderbook>>> iterator = storage.entrySet().iterator();

    while (iterator.hasNext()) {
      Map.Entry<String, List<StreamingEvent.Orderbook>> entry = iterator.next();
      if (!entry.getKey().equals(currentValue)) {

        if (applicationProperties.getFlightRecorder().isEnable()) {
          for (List<StreamingEvent.Orderbook> value : storage.values()) {
            objectMapper.writeValue(new FileOutputStream(Path.of(currentValue + "_data.json").toFile(), true), value);
          }
        }

        storage.remove(entry.getKey());
      }
    }

  }

}
