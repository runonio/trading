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

# gradle
implementation 'io.runon.trading:trading:0.1.4'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/trading/0.1.4

implementation 'io.runon.trading:technical-analysis:0.2.5'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/technical-analysis/0.2.5

implementation 'io.runon.trading:backtesting:0.0.5'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/backtesting/0.0.5

implementation 'io.runon.trading:trading-data:0.1.0'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/trading-data/0.1.0
    
implementation 'io.runon.trading:chart-view:0.1.9'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/chart-view/0.1.9

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
