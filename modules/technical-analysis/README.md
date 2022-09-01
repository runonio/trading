# technical-analysis

# 개발환경
- open jdk 17

# 개요
 - 기술적 분석
 - 트레이딩을 시작한다면 지금으로서는 가장 기본적인 모듈
 - 관련 모듈을 활용한 많은 연구일지들이 팀 기술블로그에 정리될 예정입니다.
 
# gradle
implementation 'io.runon.trading:technical-analysis:0.4.0'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/technical-analysis/0.4.0

# 목차
## 이동평균
### SMA: 단순 이동 평군 
io.runon.trading.technical.analysis.indicators.ma.Sma
### EMA: 지수 이동 평균
io.runon.trading.technical.analysis.indicators.ma.Ema
### VWMA: 거래량 가중 이동 평균
io.runon.trading.technical.analysis.indicators.ma.Vwma

## 다이버전스
io.runon.trading.technical.analysis.indicators.divergence.Divergence

## 가격지표
### 스토캐스틱
io.runon.trading.technical.analysis.indicators.stochastic.Stochastic
### 볼린저밴드
io.runon.trading.technical.analysis.indicators.band.BollingerBands
### MACD
io.runon.trading.technical.analysis.indicators.ma.Macd
### RSI
io.runon.trading.technical.analysis.indicators.Rsi
### 일목 균형표
io.runon.trading.technical.analysis.indicators.ichimoku.IchimokuBalance

## 거래량 지표
### 매집분산지표 (A/D Line)
io.runon.trading.technical.analysis.indicators.volume.AdLine
### VR (Volume Ratio)
io.runon.trading.technical.analysis.indicators.volume.Vr
### 매물대분석 (Volume Profile)
io.runon.trading.technical.analysis.indicators.volume.profile.VolumeProfile

## 시장 지표
### 신고가 신저가 
io.runon.trading.technical.analysis.indicators.market.nhnl.Nhnl
- 다른부분은 비율값을 제공하는데 이는 전체 값에서 전체건수로 나눈값. -100 ~ 100 사이의 값을 만들어서 사용하기위해 추가
### STV (soaring trading volume)
io.runon.trading.technical.analysis.indicators.market.stv.SoaringTradingVolume
- 비율값을 제공하는데 이는 전체 값에서 전체건수로 나눈값. 0 ~ 100 사이의 값을 만들어서 사용하기위해 추가
- 거래량 급증 종목의 수를 지표화 해서 사용
- 자체개발
### ADR
io.runon.trading.technical.analysis.indicators.market.Adr
### ADMR
io.runon.trading.technical.analysis.indicators.market.Admr
- (A - D) / Market 종목의수 * 100
- AD 수치를 0 ~ 100의 값으로 만들어서 사용해보기 위해 추가
- 급등 급락종목만 사용할 수 있는 최소 변화율 설정을 지원
- 자체개발

# 구현 예정 목록
- 파라볼릭 시스템 (손실제한 구현 후)
- 헤릭정산지수 (미체결 약정 활용)
- 방향성지표 (ADX) (기존구현정보 활용으로 어렵지않음)
- ROC (기존구현정보 활용으로 어렵지않음)
- 윌리엄스R (기존구현정보 활용으로 어렵지않음)
- 지지선, 저항선
- 피보나치
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
