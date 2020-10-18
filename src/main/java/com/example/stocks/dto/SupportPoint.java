package com.example.stocks.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupportPoint {
  private OffsetDateTime time;
  private BigDecimal value;
}
