# technical-analysis

# 개발환경
- open jdk 17

# 개요
 - 기술적 분석
 - 트레이딩을 시작한다면 지금으로서는 가장 기본적인 모듈
 - 관련 모듈을 활용한 많은 연구일지들이 팀 기술블로그에 정리될 예정입니다.
 
# gradle
implementation 'io.runon.trading:technical-analysis:0.4.2'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/technical-analysis/0.4.2

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



## 거래량 지표
### 매집분산지표 (ADI)
io.runon.trading.technical.analysis.indicators.volume.Adi
### VR (Volume Ratio)
io.runon.trading.technical.analysis.indicators.volume.Vr
### 매물대분석 (Volume Profile)
io.runon.trading.technical.analysis.indicators.volume.profile.VolumeProfile
### 차이킨오실레이터 (Chaikin Oscillator)
io.runon.trading.technical.analysis.indicators.volume.ChaikinOscillator
### MFI (Money Flow Index)
io.runon.trading.technical.analysis.indicators.volume.Mfi



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

# 구현 예정 목록

- 방향성지표 (ADX) (기존구현정보 활용으로 어렵지않음)
- 파라볼릭 시스템 (SAR)
    - https://academy.binance.com/ko/articles/a-brief-guide-to-the-parabolic-sar-indicator
    - https://www.youthkor.com/10-%ED%8C%8C%EB%9D%BC%EB%B3%BC%EB%A6%AD-sar-%EC%A7%80%ED%91%9C-%EB%B3%B4%EB%8A%94%EB%B2%95/
- 헤릭정산지수 (미체결 약정 활용)


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
