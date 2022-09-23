# trading
매매 프로젝트

# 개발환경
- open jdk 17

# 데이터구조
## csv TradeCandle(캔들) 데이터 기본 구조
캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10]

## csv Trade(거래) (거래대금이 설정된 경우)
- 유형종류 (BUY, SELL)

### 거래대금이 설정되어 있지 않은경우
시간(밀리초 유닉스타임)[0],유형[1],가격[2],거래량[3],거래대금[4]

### 거래대금이 설정되어 있는 경우
시간(밀리초 유닉스타임)[0],유형[1],가격[2],거래량[3]

## open interest (미체결 약정) 
시간(밀리초 유닉스타임)[0],미체결약정[1],미체결약정명목가치(Notional Value of Open Interest)[2]
## open interest (미체결 약정) 금액이 없는경우
시간(밀리초 유닉스타임)[0],미체결약정[1]

## long short ratio (롱숏 비율) long account / short account
시간(밀리초 유닉스타임)[0],ratio[1],long account[2],short account[3]

## long short ratio (롱숏 비율) 계좌정보를 모를경우
시간(밀리초 유닉스타임)[0],ratio[1]

# gradle
implementation 'io.runon.trading:trading:0.3.7'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/trading/0.3.7

implementation 'io.runon.trading:technical-analysis:0.4.1'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/technical-analysis/0.4.1

implementation 'io.runon.trading:backtesting:0.1.5'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/backtesting/0.1.5

implementation 'io.runon.trading:trading-data:0.2.2'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/trading-data/0.2.2
    
implementation 'io.runon.trading:chart-view:0.2.5'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/chart-view/0.2.5



# 목차
## 이동평균
### SMA: 단순 이동 평군
io.runon.trading.technical.analysis.indicators.ma.Sma
### EMA: 지수 이동 평균
io.runon.trading.technical.analysis.indicators.ma.Ema
### VWMA: 거래량 가중 이동 평균
io.runon.trading.technical.analysis.indicators.ma.Vwma

### MCMA: Middle Change Moving Average
io.runon.trading.technical.analysis.indicators.ma.Mcma
- 중간 가격의 변화량의 이동평균
- 중간가격은 고가 + 저가 + 종가/3
- 기울기 측정 지표로 사용하기 위해 연구중
### CMA: Change Moving Average
io.runon.trading.technical.analysis.indicators.ma.Cma
- 가격 변화량의 이동평균
- 기울기 측정 지표를 사용하기 위해 연구중

## 다이버전스
io.runon.trading.technical.analysis.indicators.divergence.Divergence

## 피보나치 (fibonacci)
io.runon.trading.technical.analysis.indicators.fibonacci.Fibonacci

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
### MVD
io.runon.trading.technical.analysis.indicators.market.Mvd
- Market Volume Disparity
- 시장 거래량 이격도
- 평균 거래량(상위 10%제외)에 따른 이격도
- Ema와 같이 쓰면서 효과를 측정중
- 자체개발 연구중 지표


### MTPD
io.runon.trading.technical.analysis.indicators.market.Mtpd
- Market Trading Price Disparity
- 시장 거래대금 이격도
- 평균 거래대금(상위 10%제외)에 따른 이격도
- Ema와 같이 쓰면서 효과를 측정중
- 자체개발 연구중 지표

### MVP
io.runon.trading.technical.analysis.indicators.market.Mvp
- Market Volume Power
- 시장 체결 강도
- Ema와 같이 쓰면서 효과를 측정중
- 자체개발 연구중 지표

# 구현 예정 목록
- ROC (기존구현정보 활용으로 어렵지않음)
- 윌리엄스R (기존구현정보 활용으로 어렵지않음)
- 방향성지표 (ADX) (기존구현정보 활용으로 어렵지않음)
- 파라볼릭 시스템
    - https://academy.binance.com/ko/articles/a-brief-guide-to-the-parabolic-sar-indicator
- 헤릭정산지수 (미체결 약정 활용)
- 차트패턴
    - 차트 패턴은 구현된 목록을 정리하여 사용할 만한 목록을 다시 추려서 정비

- 바닥신호 (vix40이상, 생존비율 10% 미만 200일선 위에종목, 거래량 급증)


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
