package com.example.stocks.enums;

public enum  EntityType {
  ORDERBOOK("Orderbook"), CANDLE("Candle");

  private String name;

  EntityType(String name) {
    this.name = name;
  }

  public String getName(){
    return name;
  }
}
