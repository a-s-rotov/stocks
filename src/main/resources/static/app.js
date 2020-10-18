function updateChart(){
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      var responseObj = JSON.parse(this.responseText);
      var candlesForChart = [];

      for (let candle of responseObj.historicalCandles.candles) {
        var item = new Object();
        item.x = new Date(candle.time);
        item.y = [candle.o, candle.h, candle.l, candle.c];
        candlesForChart.push(item);
      }

      var linesForChart = [];

      for (let supportPoint of responseObj.supportPoints) {
        var item = new Object();
        item.x = new Date(supportPoint.time);
        item.y = supportPoint.value;
        linesForChart.push(item);
      }


      chart.appendData([{
        data: candlesForChart
      },
      {
        data: linesForChart
      }]);
    }
  }
  xhttp.open("GET", "/candles/BBG000BCSST7", true);
  xhttp.send();

}
