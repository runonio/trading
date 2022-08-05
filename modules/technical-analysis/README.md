# technical-analysis

# 개발환경
- open jdk 17

# 개요
 - 기술적 분석
 - 트레이딩을 시작한다면 지금으로서는 가장 기본적인 모듈
 - 관련 모듈을 활용한 많은 연구일지들이 팀 기술블로그에 정리될 예정입니다.
 
# gradle
implementation 'io.runon.trading:technical-analysis:0.3.7'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/technical-analysis/0.3.7

# 목차
## 이동평균
### SMA: 단순 이동 평군 
io.runon.trading.technical.analysis.indicator.ma.Sma
### EMA: 지수 이동 평균
io.runon.trading.technical.analysis.indicator.ma.Ema
### VWMA: 거래량 가중 이동 평균
io.runon.trading.technical.analysis.indicator.ma.Vwma

## 가격지표
### 스토캐스틱
io.runon.trading.technical.analysis.indicator.stochastic.Stochastic
### 볼린저밴드
io.runon.trading.technical.analysis.indicator.band.BollingerBands
### MACD
io.runon.trading.technical.analysis.indicator.ma.Macd
### RSI
io.runon.trading.technical.analysis.indicator.Rsi
### 일목 균형표
io.runon.trading.technical.analysis.indicator.ichimoku.IchimokuBalance

## 거래량 지표
### 매집분산지표 (A/D Line)
io.runon.trading.technical.analysis.indicator.volume.AdLine
### VR (Volume Ratio)
io.runon.trading.technical.analysis.indicator.volume.vr

## 시장 지표
### 신고가 신저가 
io.runon.trading.technical.analysis.market.nhnl.Nhnl
### STV (soaring trading volume)
io.runon.trading.technical.analysis.market.stv.SoaringTradingVolume

직접 개발하여 연구중인 지표

# 구현 예정 목록
- 파라볼릭 시스템 (손실제한 구현 후)
- 헤릭정산지수
- 방향성지표 (ADX)
- ROC
- 윌리엄스R
- 지지선, 저항선
- 매물대분석
- 피보나치
- 다이버전스
- 차트패턴
  - 차트 패턴은 구현된 목록을 정리하여 사용할 만한 목록을 다시 추려서 정비


# communication
### blog, homepage
- [www.seomse.com](https://www.seomse.com/)
- [runon.io](https://runon.io)
- [github.com/seomse](https://github.com/seomse)
- [github.com/runonio](https://github.com/runonio)

### 카카오톡 오픈톡
 - https://open.kakao.com/o/g6vzOKqb
     - 참여코드: runon
### 슬랙 slack
- https://seomse.slack.com/

### email
 - comseomse@gmail.com
 
# main developer
 - macle
    -  [github.com/macle86](https://github.com/macle86)
