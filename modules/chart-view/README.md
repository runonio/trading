# chart-view

## 개발환경
- open jdk 17

## 개요
- lightweight-charts 이용한 Java용 차트 모듈 

## 사용법
프로그램을 사용 하기 위해서 다음 실행 방법을 따릅니다.

1. /resources 아래 폴더를 프로젝트 경로에 추가 합니다. 실행을 위해선 아래의 파일들이 필요 합니다. 
 * resources/chromedriver.exe
 * resources/lightweight-charts.standalone.production
 * resources/pure.js

2. 봉 데이터 표시를 위해 위해 io.runon.trading.technical.analysis.candle.TradeCandle [] 데이터를 준비 합니다.

3. 차트에 라인 / 마커 / 볼륨 데이터를 추가 할수 있습니다.
* TradingChart.addLineAll ( LineData[] 선형 데이터 배열 , color , size )
* TradingChart.addMarkerAll ( MarkerData[] 마커 데이터 배열 )
* TradingChart.addVolumeAll ( VolumeData[] 거래량 데이터 배열 , top margin , bottom margin )

4. 아래는 실제 사용 코드의 예시 입니다.

```
    /* 데이터 준비 */
    TradeCandle [] candleArray = createCandleArray();
    LineData [] lineDataArray = createLineArray();
    MarkerData[] markerDataArray = createMarkerArray();
    VolumeData[] volumeDataArray = createVolumeArray();

    /* 차트 생성 */
    TradingChart view = new TradingChart(candleArray);
    view.addLineAll(lineDataArray,"red",1);
    view.addVolumeAll(volumeDataArray,0.8,0);
    view.addMarkerAll(markerDataArray);
    
    /* Chart View */
    view.view();
```


# 참고
 - https://github.com/tradingview/lightweight-charts
 - https://chromedriver.chromium.org/downloads
 - https://pure-js.com/


# communication
## site, blog, git
- [runon.io](https://runon.io)
- [blog.runon.io](https://blog.runon.io)
- [github.com/runonio](https://github.com/runonio)

## contact
- email: info@runon.io

## main developer
- macle
    - git: [github.com/macle86](https://github.com/macle86)