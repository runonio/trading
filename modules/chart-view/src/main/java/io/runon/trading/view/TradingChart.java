/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.runon.trading.view;

import com.seomse.commons.utils.FileUtil;
import com.seomse.commons.utils.time.DateUtil;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.view.util.BrowserUtil;
import io.runon.trading.view.util.JarUtil;

import java.io.*;
/**
 * 트레이딩 차트
 * @author ccsweets
 */
public class TradingChart {

    /* 차트 데이터 타입 */
    public enum ChartDateType {MINUTE, DAY}

    /* 캔들 데이터 */
    CandleStick[] candleStickArr;
    /* HTML chart create String */
    StringBuilder createChartStr = new StringBuilder();
    /* 차트 날짜유형 */
    ChartDateType dateType;

    /* pureJs contents */
    String pureJsContents;
    /* LightWeight Js contents */
    String lightWeightJsContents;

    /* title */
    String browserTitle = "Seomse LightWeight-Chart View";

    /* html file export path */
    String exportPath = "data";

    /**
     * 브라우저 타이틀을 설정 한다.
     * @param  browserTitle browserTitle
     */
    public void setBrowserTitle(String browserTitle) {
        this.browserTitle = browserTitle;
    }

    /**
     * Constructor
     * @param candleStickArr 캔들스틱 배열
     */
    public TradingChart(CandleStick[] candleStickArr){
        this(candleStickArr,600,300, ChartDateType.DAY);
    }
    /**
     * Constructor
     * @param candleStickArr 캔들스틱 배열
     * @param width X축 길이
     * @param height Y축 높이
     * @param dateType 날짜유형
     */
    public TradingChart(CandleStick[] candleStickArr , int width , int height , ChartDateType dateType){

        try {
            pureJsContents = JarUtil.readFromJarFile("pure.js");
            lightWeightJsContents = JarUtil.readFromJarFile("lightweight-charts.standalone.production.js");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.candleStickArr = candleStickArr;
        this.dateType = dateType;
        createChartStr.append( """
                var chart = LightweightCharts.createChart(document.body, {
                    width: %d,
                  height: %d,
                  crosshair: {
                    mode: LightweightCharts.CrosshairMode.Normal,
                  }
                });
                
                const candlestickSeries = chart.addCandlestickSeries({
                  priceScaleId: 'right'
                });
                """.formatted(width,height));
        createChartStr.append("candlestickSeries.setData([");
        int candleStickArrSize = candleStickArr.length;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < candleStickArrSize; i++) {
            CandleStick candleStick = candleStickArr[i];

            String timeStr;
            if(dateType.equals(ChartDateType.DAY)){
                timeStr  = DateUtil.getDateYmd(candleStick.getOpenTime(),"yyyy-MM-dd");
            } else {
                timeStr  = DateUtil.getDateYmd(candleStick.getOpenTime(),"yyyy-MM-dd HH:mm");
            }
            createChartStr.append("""
                    {
                        close: %s,
                        high: %s,
                        low: %s,
                        open: %s,
                        time: '%s'
                      },
                    """.formatted(
                    candleStick.getClose().stripTrailingZeros().toString()
                    ,candleStick.getHigh().stripTrailingZeros().toString()
                    ,candleStick.getLow().stripTrailingZeros().toString()
                    ,candleStick.getOpen().stripTrailingZeros().toString()
                    ,timeStr
            ));
        }
        createChartStr.setLength(createChartStr.length()-1);
        createChartStr.append("]);\n");
    }

    /**
     * 차트에 마커를 전부 추가 한다.
     * @param markerDataArray 마커 데이터 배열
     */
    public void addMarkerAll(MarkerData[] markerDataArray) {
        createChartStr.append("""
              var markers = [];
                """);

        for (MarkerData markerData : markerDataArray) {
            String timeStr;
            if(dateType.equals(ChartDateType.DAY)){
                timeStr  = DateUtil.getDateYmd(markerData.getTime(),"yyyy-MM-dd");
            } else {
                timeStr  = DateUtil.getDateYmd(markerData.getTime(),"yyyy-MM-dd HH:mm");
            }
            createChartStr.append("""
                markers.push({ time: '%s', position: '%s', color: '%s', shape: '%s', text: '%s'});
                """
            .formatted(timeStr,markerData.getMarkerType().name(),markerData.getColor(),markerData.getMarkerShape().name(),markerData.getText())
            );
        }
        createChartStr.append("candlestickSeries.setMarkers(markers);");
    }

    /**
     * 거래량 데이터를 전부 추가한다.
     * @param volumeDataArr 거래량 데이터 배열
     * @param topMargin topMargin
     * @param bottomMargin bottomMargin
     */
    public void addVolumeAll(VolumeData[] volumeDataArr, double topMargin , double bottomMargin){
        createChartStr.append("""
                var volumeSeries = chart.addHistogramSeries({
                  	color: '#26a69a',
                  	priceFormat: {
                  		type: 'volume',
                  	},
                  	priceScaleId: '',
                  	scaleMargins: {
                  		top: %.1f,
                  		bottom: %.1f,
                  	},
                  });
                  volumeSeries.setData([
                """.formatted(topMargin,bottomMargin));
        int volumeDataArrSize = volumeDataArr.length;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < volumeDataArrSize; i++) {
            VolumeData volumeData = volumeDataArr[i];
            String timeStr;
            if(dateType.equals(ChartDateType.DAY)){
                timeStr  = DateUtil.getDateYmd(volumeData.getTime(),"yyyy-MM-dd");
            } else {
                timeStr  = DateUtil.getDateYmd(volumeData.getTime(),"yyyy-MM-dd HH:mm");
            }

            createChartStr.append("""
                { time: '%s', value: %.2f, color: '%s' },
                """.formatted(timeStr,volumeData.getVolume() , volumeData.getColor()));
        }
        createChartStr.append("]);");
    }

    /**
     * 선형 데이터를 전부 추가한다.
     * @param lineDataArr 선형 데이터 배열
     * @param color 색깔
     * @param size 굵기
     */
    public void addLineAll(LineData[] lineDataArr , String color, int size){
        createChartStr.append("""
                chart.addLineSeries({
                  color: '%s',
                  lineWidth: %d,
                }).setData([
                """.formatted(color,size));
        int lineDataArrSize = lineDataArr.length;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < lineDataArrSize; i++) {
            LineData lineData = lineDataArr[i];
//            long openTime = lineData.getTime();
            double price = lineData.getPrice();


            String timeStr;
            if(dateType.equals(ChartDateType.DAY)){
                timeStr  = DateUtil.getDateYmd(lineData.getTime(),"yyyy-MM-dd");
            } else {
                timeStr  = DateUtil.getDateYmd(lineData.getTime(),"yyyy-MM-dd HH:mm");
            }
            createChartStr.append("""
                    {
                        time: '%s',
                        value: %.2f
                      },
                    """.formatted(
                    timeStr,price
            ));

        }

        createChartStr.setLength(createChartStr.length()-1);
        createChartStr.append("]);\n");
    }

    /**
     * HTML 데이터를 전달 받는다.
     * @return HTML
     */
    public String getHtml(){
        //noinspection StringBufferReplaceableByString
        StringBuilder result = new StringBuilder("""
                <!DOCTYPE html>
                <html>
                <head>
                  <title>%s</title>
                  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                </head>
                <body>
                    <script>%s</script>
                    <script>%s</script>
                  
                """.formatted(browserTitle,pureJsContents,lightWeightJsContents)
        );

        result.append("<script>\n").append(createChartStr.toString()).append("\n</script>\n");

        return result.append("</body></html>").toString();
    }

    /**
     * 결과를 HTML 파일로 생성 한다.
     * @return html file path
     */
    public String makeHtmlFile(){
        return makeHtmlFile(DateUtil.getDateYmd(System.currentTimeMillis(),"yyyyMMddHHmmss") + ".html");
    }

    /**
     * 결과를 HTML 파일로 생성 한다.
     * @param exportFileName out file name
     * @return html file path
     */
    public String makeHtmlFile(String exportFileName){
        File exportDir = new File(exportPath);
        if(!exportDir.exists()){
            //noinspection ResultOfMethodCallIgnored
            exportDir.mkdir();
        }
        String exportFileFullPath = exportPath + "/" + exportFileName;
        FileUtil.fileOutput(getHtml(),exportFileFullPath , false);
        return exportFileFullPath;
    }

    /**
     * 데이터를 브라우저로 확인 한다.
     */
    public void view(){
        String exportFileFullPath = makeHtmlFile();
        File viewHtmlFile = new File(exportFileFullPath);
        String htmlFileAbsolutePath = viewHtmlFile.getAbsolutePath();
        BrowserUtil.loadChromeByFile(htmlFileAbsolutePath);
    }
}
