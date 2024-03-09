# trading-data

# 개발환경
- open jdk 21

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

## order book (호가창) (json object)
- t : 시간(밀리초 유닉스타임)[0]
- asks : 매도호가 목록 (json array)
  - ask : 매도호가 line (json array)  
    - 0 : 가격, 1 : 수량
- bids : 매수호가 목록 (json array)
  - bid : 매수호가 line (json array)
    - 0 : 가격, 1 : 수량
# gradle
implementation 'io.runon.trading:trading-data:0.2.83'
- etc
    - https://mvnrepository.com/artifact/io.runon.trading/trading-data/0.2.83

# communication
### blog, homepage
- [github.com/runonio](https://github.com/runonio)
- [runon.io](https://runon.io)
- [github.com/seomse](https://github.com/seomse)
- [www.seomse.com](https://www.seomse.com/)


### email
- iorunon@gmail.com

## main developer
- macle
    - github(source code): [github.com/macle86](https://github.com/macle86)
    - email: ysys86a@gmail.com
