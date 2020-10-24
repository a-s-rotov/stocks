package com.example.stocks.util;

import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor( access = AccessLevel.PRIVATE)
public class TimeUtil {

  public static String roundFiveMinute(OffsetDateTime time){
    Integer divMinute = time.getMinute()/10;
    Integer modMinute = time.getMinute()%10;
    modMinute = modMinute > 5 ? 5 : 0;
    return divMinute.toString() + modMinute.toString();
  }


}
