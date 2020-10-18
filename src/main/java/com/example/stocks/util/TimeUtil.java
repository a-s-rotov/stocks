package com.example.stocks.util;

import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@NoArgsConstructor( access = AccessLevel.PRIVATE)
public class TimeUtil {

  public static String roundFiveMinute(OffsetDateTime time){
    Integer divMinute = time.getMinute()/10;
    Integer modMinute = time.getMinute()%10;
    modMinute = modMinute > 5 ? 0 : 5;
    return divMinute.toString() + modMinute.toString();
  }


}
