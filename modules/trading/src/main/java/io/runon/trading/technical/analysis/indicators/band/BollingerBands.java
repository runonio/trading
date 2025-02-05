package io.runon.trading.technical.analysis.indicators.band;

import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.TradingMath;
import io.runon.trading.technical.analysis.candle.CandleBigDecimals;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.indicators.ma.Sma;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * 볼린저밴드
 *
 * wikidocs.net/87171
 *
 * mbb = 중심선 = 주가의 20 기간 이동평균선 = clo20
 * ubb = 상한선 = 중심선 + 주가의 20기간 표준편차 * 2
 * lbb = 하한선 = 중심선 – 주가의 20기간 표준편차 * 2
 * perb = %b = (주가 – 하한선) / (상한선 – 하한선) = (close - lbb) / (ubb - lbb)
 * bw = 밴드폭 (Bandwidth) = (상한선 – 하한선) / 중심선 = (ubb - lbb) / mbb
 *
 * 1980년대 초반 미국의 재무분석가인 존 볼린저가 개발하고 상표권을 취득한 주가 기술적 분석 도구
 * 볼린저밴드 기본 원리를 살펴보면 주가의 변동이 표준정규분포 함수에 따른다고 가정하고
 * 주가를 따라 위 아래로 폭이 움직이는 밴드를 만들어 기준선이로 판단
 *
 * 볼린저 밴드는 이동평균선을 추세중심선으로 사용하며 상하한 변동폭은 추세중심선의 표준편차로 계산
 * 가격 변동성 분석과 추세분석을 동시에 수행
 *
 * 볼린저밴드의 기본 전제는 주가가 상한선과 하한선을 경계로 등락을 거듭하는 경향이 있다는 것
 * 주가의 95%이상 볼린저밴드 내에서 수렴과 발산을 반복하며 형성
 *
 * 밴드의 폭이 이전보다 상대적으로 크거나 줄어들 경우, 과매수 또는 과매도 상태로 가늠한다.
 *
 * 표준편차는 주가의 변동폭을 기준으로 형성된 값
 *
 * 정규분포표를 보면 주가의 변동과 표준편차 * 2 사이에 95%의 주가가 형성
 *
 * 볼린저 밴드의 상한선: 20일 이평선 값 + ( 20일 동안의 주가 표준편차 값 ) * 2
 * 볼린저 밴드의 하한선: 20일 이평선 값 - ( 20일 동안의 주가 표준편차 값 ) * 2
 *
 * 이동평균선에는 SMA (단순이동평균선; Simple Moving Average) EMA (지수이동평균선; Exponential Moving Average) 이 있는데
 * 볼린저 밴드에 쓰이는 이평선은 MA = SMA
 *
 * 밴드폭 (Bandwidth)은 상한선과 하한선의 밴드폭이 얼마나 큰지를 나타내 주는 지표이다.
 * 주가의 변동폭이 클 경우 볼린저 밴드의 폭이 넓어지고,
 * 주가의 변동폭이 적을 때는 볼린저 밴드의 폭 또한 좁아진다.
 *
 * %b 는 현재 주가가 볼린저밴드 내의 어느 위치에 있는지를 수치로 표현한 지표
 * 현재 주가가 하한선을 기준으로 위로 몇 % 지점에 있는가를 수치로 나타냅니다.
 *
 * 주가가
 * 1. 하한선에 있는 상태에는 0
 * 2. 상한선에 있는 상태에는 1
 * 3. 중심에 있는 상태에는 0.5
 * 4. 하한선을 벗어난 상태에는 0 이하 (음수)
 * 5. 상한선을 벗어난 상태에는 1이상
 *
 * 개발자 존 볼린저는 밴드 자체의 폭이 축소되고 밀집되는 구간을 거차고 난 후
 * 상단 밴드를 돌파할 때 주식을 매입하고
 * 하단 밴드를 벗어날 때 주식을 공매도 하는 것을 추천
 * 볼린저 밴드에서 폭이 좁아지는 것 = 주가 안정기
 * 그 후 추세를 결정하는데 상단 밴드를 건드리면 상단 돌파, 하단 밴드를 건드리면 하향 추세로 간다.
 *
 * 복수의 밴드 접촉과 지표 이용
 * 주가가 상단 밴드를 돌파 못하고 여러번 건드리며 약세 ->매도
 * 하단 밴드를 여러번 건드리며 점점 상승기운 -> 매수
 * 밴드의 상하단을 벗어난 이후 주가가 원래 가격대로 돌아올 경우 추세의 반전 예측
 *
 * 횡보구간 적용
 * 장기간 주가가 횡보하여 밴드의 폭이 좁아졌을때 상한선 돌파시 매수시점
 * 장기간 주가가 횡보하여 밴드의 폭이 좁아졌을때 하한선 돌파시 매도시점
 *
 * @author macle
 */
public class BollingerBands {

    public static int DEFAULT_N = 20;
    public static int DEFAULT_SD = 2;


    public static BollingerBandsData [] getArray(CandleStick[] array){

        return getArray(array, DEFAULT_N, new BigDecimal(DEFAULT_SD),0, array.length);
    }

    public static BollingerBandsData [] getArray(CandleStick[] array, int resultLength){

        return getArray(array, DEFAULT_N, new BigDecimal(DEFAULT_SD),array.length - resultLength, array.length);
    }

    public static BollingerBandsData [] getArray(CandleStick[] array, int startIndex, int end){

        return getArray(array, DEFAULT_N, new BigDecimal(DEFAULT_SD), startIndex,end);
    }
    public static BollingerBandsData [] getArray(CandleStick[] array, int n, BigDecimal sdm, int resultLength){
        return getArray(array, n, sdm, array.length - resultLength, array.length);
    }

    public static BollingerBandsData [] getArray(CandleStick[] array, int n, BigDecimal sdm, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;

        BollingerBandsData [] dataArray = new BollingerBandsData[length];

        for (int i = 0; i < length; i++) {
            int index = i+startIndex;

            dataArray[i] = get(array,n, sdm, index);
            dataArray[i].time = array[index].getTime();
        }

        return dataArray;
    }

    public static BollingerBandsData [] getArray(BigDecimal [] array, int resultLength){

        return getArray(array, DEFAULT_N, new BigDecimal(DEFAULT_SD),array.length - resultLength, array.length);
    }

    public static BollingerBandsData [] getArray(BigDecimal [] array, int startIndex, int end){

        return getArray(array, DEFAULT_N, new BigDecimal(DEFAULT_SD), startIndex,end);
    }
    public static BollingerBandsData [] getArray(BigDecimal [] array, int n, BigDecimal sdm, int resultLength){
        return getArray(array, n, sdm, array.length - resultLength, array.length);
    }

    public static BollingerBandsData [] getArray(BigDecimal [] array, int n, BigDecimal sdm, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;

        BollingerBandsData [] dataArray = new BollingerBandsData[length];

        for (int i = 0; i < length; i++) {
            dataArray[i] = get(array,n, sdm, i+startIndex);
        }

        return dataArray;
    }

    public static BollingerBandsData [] getArray(CandleStick[] array, BigDecimal [] maArray, int n, BigDecimal sdm, int resultLength){
        return getArray(array, maArray, n, sdm, array.length - resultLength, array.length);
    }



     //EMA를 사용할 수 있는 경우에 사용 한다.

    public static BollingerBandsData [] getArray(CandleStick[] array, BigDecimal [] maArray, int n, BigDecimal sdm, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;
        BollingerBandsData [] dataArray = new BollingerBandsData[length];
        for (int i = 0; i < length; i++) {
            int index = i + startIndex;

            dataArray[i] = get(array[index], maArray[index], CandleBigDecimals.sd(array,maArray[index],n, index) ,sdm);
        }
        return dataArray;
    }

    public static BollingerBandsData [] getArray(BigDecimal [] array, BigDecimal [] maArray, int n, BigDecimal sdm, int resultLength){
        return getArray(array, maArray, n, sdm, array.length - resultLength, array.length);
    }



     // EMA를 사용할 수 있는 경우에 사용 한다.

    public static BollingerBandsData [] getArray(BigDecimal [] array, BigDecimal [] maArray, int n, BigDecimal sdm, int startIndex, int end){
        if(startIndex < 0){
            startIndex = 0;
        }

        int length = end - startIndex;
        BollingerBandsData [] dataArray = new BollingerBandsData[length];
        for (int i = 0; i < length; i++) {
            int index = i + startIndex;

            dataArray[i] = get(array[index], maArray[index], TradingMath.sd(array,maArray[index],n, index) ,sdm);

        }
        return dataArray;
    }

    public static BollingerBandsData get(CandleStick[] array){
        return get(array, DEFAULT_N, new BigDecimal(DEFAULT_SD), array.length-1);
    }

    public static BollingerBandsData get(CandleStick[] array, int n, int sd){
        return get(array, n, new BigDecimal(sd), array.length-1);
    }
    public static BollingerBandsData get(CandleStick[] array, int n, BigDecimal sdm, int index){
        BigDecimal ma = Sma.get(array, n, index);
        BigDecimal sd = CandleBigDecimals.sd(array,ma,n, index);
        return get(array[array.length -1], ma,sd, sdm);
    }
    public static BollingerBandsData get(BigDecimal [] array){
        return get(array, DEFAULT_N, new BigDecimal(DEFAULT_SD), array.length-1);
    }

    public static BollingerBandsData get(BigDecimal [] array, int n, int sd){
        return get(array, n, new BigDecimal(sd), array.length-1);
    }
    public static BollingerBandsData get(BigDecimal [] array, int n, BigDecimal sdm, int index){
        BigDecimal ma = Sma.get(array, n, index);
        BigDecimal sd = TradingMath.sd(array,ma,n, index);
        return get(array[array.length -1], ma,sd, sdm);
    }

    public static BollingerBandsData get(TimeNumber timeNumber, BigDecimal ma, BigDecimal sd, BigDecimal sdMultiply){
        BollingerBandsData data = get(timeNumber.getNumber(), ma, sd, sdMultiply);
        data.time = timeNumber.getTime();
        return data;
    }

    public static BollingerBandsData get(BigDecimal close, BigDecimal ma, BigDecimal sd, BigDecimal sdMultiply){
        BollingerBandsData data = new BollingerBandsData();

        BigDecimal sdm = sd.multiply(sdMultiply);
        data.mbb = ma;
        data.ubb = ma.add(sdm);
        data.lbb = ma.subtract(sdm);

        BigDecimal height =data.ubb.subtract(data.lbb);
        if(height.compareTo(BigDecimal.ZERO) == 0){
            data.perb = BigDecimal.ZERO;
            data.bw = BigDecimal.ZERO;
        }else{
            data.perb = close.subtract(data.lbb).divide(height, MathContext.DECIMAL128);
            data.bw = height.divide(data.mbb, MathContext.DECIMAL128);
        }


        return data;
    }

    public static TimeNumber [] getMbbArray(BollingerBandsData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            BollingerBandsData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.mbb);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getUbbArray(BollingerBandsData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            BollingerBandsData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.ubb);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getLbbArray(BollingerBandsData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            BollingerBandsData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.lbb);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getPerbArray(BollingerBandsData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            BollingerBandsData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.perb);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getBwArray(BollingerBandsData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            BollingerBandsData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.bw);
        }

        return timeNumbers;
    }


}
