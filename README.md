# trading
- system trading, algorithmic trading, 퀀트를 활용해서 주식, 암호화폐, 원자재, 채권, 외환, 기타 파생상품에 사용하기위한 기본적인 프로젝트
- 매매 프로젝트
## 시스템트레이딩 이란?
- https://namu.wiki/w/%EC%8B%9C%EC%8A%A4%ED%85%9C%20%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%94%A9
- https://namu.wiki/w/%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98%20%ED%8A%B8%EB%A0%88%EC%9D%B4%EB%94%A9

## interval
- 분: m
  - 1m 1분봉, 5m 5분봉, 15m 15분봉
- 시: h
  - 1h 1시간봉, 4h 4시간봉
- 일(하루): d
  - 1d 1일봉
- 주(7일): w
  - 1w: 주봉(1주일) 
- 월: M (대소문자 구분 필수, 소문자는 분봉)
  - 1M :1달 

# 개발환경
- open jdk 17

# 참고자료
- 하이투자증권 설명자료
  - https://www.hi-ib.com/systemtrade/st020901.jsp
- 추천 서적
  - 심리투자의 법칙

# 데이터구조
## csv TradeCandle(캔들) 데이터 기본 구조
캔들시작시간(밀리초 유닉스타임)[0],종가[1],시가[2],고가[3],저가[4],직전가[5],거래량[6],거래대금[7],거래횟수[8],매수거래량[9],매수거래대금[10],가격제한여부[11],가격변화유형[12],종료시간[13],데이터(json)
<br>
캔들 데이터는 12번쨰까지는 공통입니다. 12번째인 json데이터는 캔들에 추가정보가 있을때 활용하는 데이터 입니다. (일봉보다 큰데이터기준 , 신용정보, 락정보(배당락, 권리락), ) 과 같은 데이터정보입니다.
<br>
가격제한여부 종료시간등은 시간간격이 일정하지 않은경우에 없을 수 있습니다.
<br>
캔들의 구조에 사용할 데이터가 추가되는 경우가 발생해서, json타입으로 변활할까도 고민했지만 대부분의 캔들 데이터는 추가 데이터는 있지 않을것 입니다. 데이터 로드시간이나 용량값을 생각하면 아직은 csv구조가 좋다고 마지막 부분에만 json을 추가하는 형식으로 사용하려고 합니다.

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
implementation 'io.runon.trading:trading:0.4.7'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/trading/0.4.7


implementation 'io.runon.trading:backtesting:0.1.8'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/backtesting/0.1.8

implementation 'io.runon.trading:chart-view:0.2.7'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/chart-view/0.2.7
  
# 기술적분석
## 이동평균
### SMA: 단순 이동 평군
io.runon.trading.technical.analysis.indicators.ma.Sma
### EMA: 지수 이동 평균
io.runon.trading.technical.analysis.indicators.ma.Ema
### VWMA: 거래량 가중 이동 평균
io.runon.trading.technical.analysis.indicators.ma.Vwma

## 다이버전스
io.runon.trading.technical.analysis.indicators.divergence.Divergence

## 피보나치 (fibonacci)
io.runon.trading.technical.analysis.indicators.fibonacci.Fibonacci

## 가격지표
### 엘리엇 파동이론 (Elliott wave principle)
io.runon.trading.technical.analysis.indicators.wave.ElliottWave
- 구현예정 항목

### 스토캐스틱
io.runon.trading.technical.analysis.indicators.stochastic.Stochastic
### 볼린저밴드
io.runon.trading.technical.analysis.indicators.band.BollingerBands
### MACD
io.runon.trading.technical.analysis.indicators.ma.Macd
### RSI (Relative Strength Index)
io.runon.trading.technical.analysis.indicators.Rsi
### RMI  (Relative Momentum Index)
io.runon.trading.technical.analysis.indicators.Rmi
### 일목 균형표
io.runon.trading.technical.analysis.indicators.ichimoku.IchimokuBalance
### ROC
io.runon.trading.technical.analysis.indicators.Roc
### ROCM (ROC Middle)
io.runon.trading.technical.analysis.indicators.Rocm
### Momentum
io.runon.trading.technical.analysis.indicators.Momentum
### MomentumMiddle
io.runon.trading.technical.analysis.indicators.MomentumMiddle
### 윌림엄스 %R
io.runon.trading.technical.analysis.indicators.Wpr
### CCI  Commodity Channel Index
io.runon.trading.technical.analysis.indicators.Cci
### ADX (Average Directional Movement Index)
io.runon.trading.technical.analysis.indicators.adx.Adx
### ElderRay
io.runon.trading.technical.analysis.indicators.elder.ElderRay
### 파라볼릭 시스템 (SAR)
io.runon.trading.technical.analysis.indicators.elder.Sar

## 거래량 지표
### 매집분산지표 (ADI)
io.runon.trading.technical.analysis.indicators.volume.Adi
### VR (Volume Ratio)
io.runon.trading.technical.analysis.indicators.volume.Vr

### 차이킨오실레이터 (Chaikin Oscillator)
io.runon.trading.technical.analysis.indicators.volume.ChaikinOscillator
### MFI (Money Flow Index)
io.runon.trading.technical.analysis.indicators.volume.Mfi
### ForceIndex
io.runon.trading.technical.analysis.indicators.elder.ForceIndex
### HPI 혜릭정산지수 (미체결 약정 활용) Herrick Payoff Index
io.runon.trading.technical.analysis.indicators.volume.Hpi

## 매물대분석 (Volume Profile)
io.runon.trading.technical.analysis.indicators.volume.profile.VolumeProfile
io.runon.trading.technical.analysis.indicators.volume.profile.gap.Vpg

## 시장 지표
### 신고가 신저가
io.runon.trading.technical.analysis.indicators.market.nhnl.Nhnl
- 다른부분은 비율값을 제공하는데 이는 전체 값에서 전체건수로 나눈값. -100 ~ 100 사이의 값을 만들어서 사용하기위해 추가
### STV (soaring trading volume)
io.runon.trading.technical.analysis.indicators.market.stv.SoaringTradingVolume
- 비율값을 제공하는데 이는 전체 값에서 전체건수로 나눈값. 0 ~ 100 사이의 값을 만들어서 사용하기위해 추가
- 거래량 급증 종목의 수를 지표화 해서 사용
- 자체개발

### AD Issue
io.runon.trading.technical.analysis.indicators.market.AdIssue
### McClellan Oscillator
io.runon.trading.technical.analysis.indicators.market.McClellanOscillator
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

### 생존비율 지표 (Market Survival Rate)
io.runon.trading.technical.analysis.indicators.market.MarketSurvivalRate
- 생존비율 지표
- 200일선(설정값) 보다 종가가 같거나 위에 있는 종목비율
- 바닥권에서 10% 미만의 값이 나타남


# 바닥신호
- 바닥신호 (vix40이상, 생존비율 10% 미만 200일선 위에종목, 거래량 급증)

# communication
### blog, homepage
- [runon.io](https://runon.io)
- [github.com/runonio](https://github.com/runonio)
- [github.com/seomse](https://github.com/seomse)
- [www.seomse.com](https://www.seomse.com/)


### email
- iorunon@gmail.com

### cafe
- [cafe.naver.com/radvisor](https://cafe.naver.com/radvisor)


### talk
- 로보어드바이저, 시스템트레이딩, 퀀트 단톡방
  - https://open.kakao.com/o/g6vzOKqb
  - 참여코드: runon


## main developer
- macle
  - github(source code): [github.com/macle86](https://github.com/macle86)
  - email: ysys86a@gmail.com
