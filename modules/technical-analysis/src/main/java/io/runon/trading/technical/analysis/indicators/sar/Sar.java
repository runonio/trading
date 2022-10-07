package io.runon.trading.technical.analysis.indicators.sar;

import com.seomse.commons.config.Config;
import io.runon.trading.TimeNumber;
import io.runon.trading.TimeNumberData;
import io.runon.trading.technical.analysis.candle.CandleStick;
import io.runon.trading.technical.analysis.hl.HighLowCandleLeftSearch;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 파라볼릭 SAR
 *
 * 관련 지표는 시작 EP 지점매번 계산하게 접근하려고 한다.
 * 추세라는건 추상적이기에 상승이라고 생갈될때의 sar 값과 하락이라고 생각할때의 Sar값을 같이 제공하는 방법으로 접근해 보려고 한다.
 * 이전 고점과 저점을 사용하는 방식은 HighLowLeftSearch 클래스를 이용한다. 초기검색범위와 연속검색범위 활용하는 방식으로 접근한다.
 *
 * 이번지표는 HTS나 트레이딩뷰, 혹은 다른 거래소에서 제공하는 정보와 다를 수 있다.
 *
 * academy.binance.com/ko/articles/a-brief-guide-to-the-parabolic-sar-indicator
 *
 * 파바볼릭 SAR이란 무엇인가요?
 * 1970년대 후반 기술적 분석가인 웰스 와일더(J. Welles Wilder Jr.)가 파라볼릭 스톱과 리버스(Stop and Reverse, SAR) 지표를 개발했습니다. 이는 그의 책 "기술적 트레이딩 시스템의 새로운 개념"에서 상대적 강도 지수(RSI)와 같은 다른 인기 있는 지표들과 함께 제시되었습니다.
 * 사실, 와일더는 해당 접근 방법을 파라볼릭 시간/가격(Parabolic Time/Price) 시스템이라 불렀으며, SAR의 개념은 다음과 같이 기술되어 있습니다.
 *
 * SAR은 스톱(Stop)과 리버스(Reverse)를 의미한다. 이는 롱 트레이드(더 높은 가격에 팔기 위해 매수 후 이익을 실현하는 것)가 끝나고 숏 트레이드(더 낮은 가격에 사기 위해 매도 후 이익을 실현하는 것)가 시작되거나, 반대의 경우가 시작되는 지점이다.
 *
 * - Wilder, J. W., Jr. (1978) 기술적 트레이딩 시스템의 새로운 개념 (p.8)
 * 오늘날 해당 시스템은 보통 파라볼릭 SAR 지표를 가리키며, 이는 시장 추세와 잠재적 전환 지점을 파악하는 도구로 사용됩니다. 와일더가 수많은 기술적 지표(TA)를 손으로 써가며 개발했음에도 불구하고, 오늘날 대부분의 디지털 트레이딩 시스템과 차트 소프트웨어의 한 부분을 차지하고 있습니다. 이제는 해당 기술을 더는 수동으로 계산하지 않아도 되었고, 상대적으로 사용이 간편해졌습니다.
 *
 *
 * 어떻게 사용하나요?
 * 파라볼릭 SAR 지표는 시장 가격 위나 아래에 찍힌 작은 점들로 이뤄집니다. 이러한 점들의 분포는 포물선을 형성합니다. 그러나 각 점은 단일한 SAR 값을 나타냅니다.
 *
 * 간단히 말해, 상승 추세 동안 점들은 시장 가격보다 아래에 표시되며, 하락 추세 동안에는 위에 표시됩니다. 해당 점들은 시장 가격이 옆으로 흐르는 횡보 기간에도 표시됩니다. 그러나 이러한 경우에는 점들이 위치가 한쪽에서 다른 쪽으로 훨씬 자주 바뀌곤 합니다. 즉, 파라볼릭 SAR 지표는 시장 추세가 없는 경우에는 활용성이 떨어집니다.
 *
 *
 *
 * 이점
 * 파라볼릭 SAR은 방향성과 시장 추세의 지속 기간, 잠재적 추세 반전 지점에 대한 통찰을 제공할 수 있습니다. 이는 투자자가 좋은 매수 기회와 매도 기회를 얻을 수 있는 가능성을 높여줄 수 있습니다.
 *
 * 일부 트레이더들은 유동적인 스톱-로스 가격을 지정하기 위해 파라볼릭 SAR 지표를 사용하기도 합니다. 이를 통해 그들의 스톱은 시장 추세를 따라갈 수 있습니다. 이러한 기술을 보통 트레일링 스톱-로스라고 합니다.
 *
 * 기본적으로 해당 지표는 기존에 창출한 수익을 유지할 수 있게 하는데, 이는 추세가 반전되면 즉시 그들의 포지션이 종료되기 때문입니다. 어떤 경우에는, 트레이더로 하여금 이익이 나는 포지션을 종료하기 힘들게 하거나 너무 일찍 거래를 시작하게 할 수도 있습니다.
 *
 *
 *
 * 한계
 * 앞서 살펴본 것처럼, 파라볼릭 SAR은 추세가 있는 시장에서 특별히 유용하나, 횡보 기간 동안에는 그렇지 않습니다. 분명한 추세가 존재하지 않을 경우, 해당 지표는 잘못된 신호를 제공할 가능성이 높으며, 이는 심각한 손실을 초래할 수 있습니다.
 *
 * 변동성이 큰 시장(위아래로 상당히 빠르게 움직이는) 또한 오해의 소지가 있는 신호들을 많이 제공할 수 있습니다. 따라서 파라볼릭 SAR 지표는 가격이 점진적으로 변할 때 가장 잘 맞아떨어지는 경향이 있습니다.
 *
 * 또 한 가지 생각해 봐야 하는 것은 지표의 민감도이며, 이는 수동으로 조절할 수 있습니다. 민감도가 높을수록, 잘못된 신호가 발생될 확률이 높습니다.
 *
 * 어떤 경우에는 잘못된 신호가 트레이더로 하여금 이익이 나는 포지션을 지나치게 일찍 종료하게 하고, 이익을 얻을 수 있는 가능성이 있는 자산을 판매하게 할 수 있습니다. 더 나쁜 경우에는, 가짜 신호가 잘못된 낙관을 심어주어 투자자가 지나치게 일찍 매수하게 만들 수도 있습니다.
 *
 * 마지막으로, 해당 지표는 거래량을 고려하지 않기 때문에 추세의 강도에는 별다른 정보를 주지 않습니다. 시장의 큰 움직임은 각 점의 간격을 벌어지게 함에도 불구하고, 이것이 추세의 강도를 나타내는 것으로 받아들여서는 안 됩니다.
 *
 * 트레이더와 투자자들이 얼마나 많은 정보를 갖고 있느냐와 무관하게 금융 시장에는 언제나 위험이 존재합니다. 그러나 파라볼릭 SAR과 다른 전략, 지표들을 위험을 최소화하고 한계를 보충하는 방법으로 함께 사용할 수 있습니다.
 * 와일더는 추세의 강도를 측정하기 위해 파라볼릭 SAR과 함께 평균 방향성 지표(Average Directional Index)를 함께 사용했습니다. 또한 포지션에 진입하기 전 분석을 위해 이동 평균과 RSI 지표를 사용할 수도 있습니다.
 *
 *
 * 파라볼릭 SAR 계산 방법
 * 오늘날에는 컴퓨터 프로그램이 자동적으로 계산을 수행합니다. 그러나 보다 자세히 알고 싶어하는 이들은, 파라볼릭 SAR 계산에 대한 간단한 설명을 살펴볼 수 있습니다.
 *
 * SAR 지점들은 기존의 시장 데이터에 기초해 계산됩니다. 따라서, 오늘의 SAR을 계산하기 위해서는 어제의 SAR을 사용하며, 내일의 값을 계산하기 위해서는 오늘의 SAR을 사용합니다.
 *
 * 상승 추세 동안 SAR 값은 이전의 고점에 기반해 계산됩니다. 하락 추세 동안에는 이전의 저점이 사용됩니다. 와일더는 한 추세 안에서 가장 높은 지점과 낮은 지점을 익스트림 포인트(Extreme Points, EP)라 불렀습니다. 그러나 상승 추세와 하락 추세에서의 방정식은 동일하지 않습니다.
 *
 * 상승 추세일 경우:
 *
 * SAR = 이전 SAR + 가속변수(AF) x (이전 익스트림 포인트(EP) - 이전 SAR)
 *
 * 하락 추세일 경우:
 *
 * SAR = 이전 SAR - 가속변수(AF) x (이전 SAR - 이전 익스트림 포인트(EP))
 *
 * AF는 가속 변수(Acceleration Factor)를 의미합니다. 이는 0.02부터 시작해 가격이 새로운 고점을 형성(상승 추세에서)하거나 새로운 저점(하락 추세에서)을 형성할 때마다 0.02씩 증가합니다. 그러나, 한계값인 0.20에 도달할 경우 추세 기간 동안 해당 값은 동일하게 유지됩니다(추세가 반전될 때까지).
 *
 * 실제로, 일부 차트 분석가들은 지표의 민감도를 변경하기 위해 수동으로 AF를 조정하기도 합니다. 0.2보다 높은 AF는 민감도를 증가시킵니다(더 많은 반전 신호). 0.2보다 낮은 AF는 이와 반대됩니다. 그럼에도 와일더는 자신의 책에서 0.02 증가가 전반적으로 가장 좋다고 주장합니다.
 *
 * 계산식은 비교적 사용하기 간편한 것지만, 일부 트레이더들은 첫 SAR을 어떻게 계산하는지 묻곤 합니다. 해당 방정식이 이전의 값을 필요로 하기 때문입니다. 와일더에 따르면, 처음 SAR은 시장 추세가 역전되기 전 마지막 익스트림 포인트에 기초에 계산될 수 있습니다.
 *
 * 와일더는 트레이더들에게 분명한 추세 반전을 발견하기 위해 차트를 되돌아보고, 첫 SAR 값으로 익스트림 포인트를 사용할 것을 추천했습니다. 이후 SAR은 최종 시장 가격에 도달할 때까지 계산될 수 있습니다.
 *
 * 예를 들어, 시장이 상승 추세일 경우 트레이더는 이전의 조정 구간을 찾기 위해 며칠 혹은 몇 주 전을 살펴볼 수 있습니다. 이후, 해당 조정 구간의 저점(익스트림 포인트)을 찾고, 이를 이어지는 상승 추세의 첫 SAR로 사용할 수 있습니다.
 *
 * 마치며
 * 파라볼릭 SAR은 1970년대에 개발된 지표임에도 불구하고 오늘날에도 여전히 광범위하게 사용되고 있습니다. 투자자들은 이를 외환, 상품, 주식, 암호 화폐 시장과 같은 투자에 적용할 수 있습니다.
 *
 * 그러나 어떠한 시장 분석 도구도 100% 정확할 수는 없습니다. 따라서 파라볼릭 SAR과 다른 전략을 사용하기 전에, 투자자들은 금융 시장과 기술적 분석을 충분히 이해해야 합니다. 또한 투자자들은 피할 수 없는 위험들에 대처하기 위한 적절한 트레이딩 전략과 위험 관리 전략을 갖고 있어야 합니다.
 * @author macle
 */
public class Sar {

    public static final SarData[] NULL_DATA = new SarData[0];
    private BigDecimal af = new BigDecimal(Config.getConfig("sar.af", "0.02"));
    private BigDecimal maxAf = new BigDecimal(Config.getConfig("sar.af.max", "0.2"));


    public void setAf(BigDecimal af) {
        this.af = af;
    }

    public void setMaxAf(BigDecimal maxAf) {
        this.maxAf = maxAf;
    }

    private int searchInitN = Config.getInteger("sar.search.init.n", 50);
    private int searchContinueN = Config.getInteger("sar.search.continue.n", 10);

    public void setSearchInitN(int searchInitN) {
        this.searchInitN = searchInitN;
    }

    public void setSearchContinueN(int searchContinueN) {
        this.searchContinueN = searchContinueN;
    }

    private int scale = 4;
    public void setScale(int scale) {
        this.scale = scale;
    }

    public SarData get(CandleStick [] array){
        return get(array, array.length-1);
    }


   public SarData get(CandleStick [] array, int index){
        int highIndex = HighLowCandleLeftSearch.searchHigh(array, searchInitN, searchContinueN, index);
        int lowIndex = HighLowCandleLeftSearch.searchLow(array, searchInitN, searchContinueN, index);

        SarData sarData = new SarData();
        sarData.time = array[index].getTime();
        setAdvancing(array, index, sarData);
        setDecline(array, index, sarData);
        return sarData;
   }

    public void setAdvancing(CandleStick [] array, int index, SarData data){
        int lowIndex = HighLowCandleLeftSearch.searchLow(array, searchInitN, searchContinueN, index);

        BigDecimal ep = array[lowIndex].getLow();
        BigDecimal previousSar = ep;

        BigDecimal af = this.af;

        for (int i = lowIndex+1; i < index ; i++) {
            previousSar = previousSar.add( af.multiply(ep.subtract(previousSar)));

            CandleStick candle = array[i];

            if(candle.getHigh().compareTo(ep) > 0){
                af = af.add(this.af);
                if(af.compareTo(maxAf) > 0){
                    af = maxAf;
                }
                ep = candle.getHigh();
            }
        }
        CandleStick candle = array[index];
        data.advancingAf = af;
        data.advancing =  previousSar.add( af.multiply(ep.subtract(previousSar))).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
    }


    public void setDecline(CandleStick [] array, int index, SarData data){
        int highIndex = HighLowCandleLeftSearch.searchHigh(array, searchInitN, searchContinueN, index);

        BigDecimal ep = array[highIndex].getHigh();
        BigDecimal previousSar = ep;

        BigDecimal af = this.af;
        for (int i = highIndex+1; i < index ; i++) {
            previousSar = previousSar.subtract( af.multiply(previousSar.subtract(ep)));

            CandleStick candle = array[i];

            if(candle.getLow().compareTo(ep) < 0){
                af = af.add(this.af);
                if(af.compareTo(maxAf) > 0){
                    af = maxAf;
                }
                ep = candle.getLow();
            }

        }
        CandleStick candle = array[index];
        data.declineAf = af;
        data.decline = previousSar.subtract( af.multiply(previousSar.subtract(ep))).setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();

    }


    public SarData [] getArray(CandleStick [] array, int resultLength){
        return getArray(array, array.length-resultLength, array.length);
    }

    public SarData [] getArray(CandleStick [] array,int startIndex, int end){

        if(startIndex < 0){
            startIndex = 0;
        }

        if( end > array.length){
            end = array.length;
        }

        int resultLength = end - startIndex;

        if(resultLength < 1){
            return NULL_DATA;
        }

        SarData[] dataArray = new SarData[resultLength];

        for (int i = 0; i <resultLength ; i++) {
            dataArray[i] = get(array, i+ startIndex);
        }

        return dataArray;

    }

    public static TimeNumber [] getAdvancingAfArray(SarData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            SarData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.advancingAf);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getAdvancingArray(SarData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            SarData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.advancing);
        }

        return timeNumbers;
    }

    public static TimeNumber [] getDeclineAfArray(SarData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            SarData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.declineAf);
        }

        return timeNumbers;
    }


    public static TimeNumber [] getDeclineArray(SarData [] array){
        TimeNumber [] timeNumbers = new TimeNumber[array.length];
        for (int i = 0; i <timeNumbers.length ; i++) {
            SarData data = array[i];
            timeNumbers[i] = new TimeNumberData(data.time, data.decline);
        }

        return timeNumbers;
    }



}
