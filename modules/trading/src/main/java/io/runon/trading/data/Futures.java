package io.runon.trading.data;

import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import io.runon.trading.TradingGson;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author macle
 */
@Data
@Table(name="futures")
public class Futures {

    @PrimaryKey(seq = 1)
    @Column(name = "futures_id")
    String futuresId;

    @Column(name = "underlying_assets_type")
    String underlyingAssetsType;

    @Column(name = "exchange")
    String exchange;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "candle_path")
    String candlePath;

    @Column(name = "currency")
    String currency;

    @Column(name = "underlying_assets_id")
    String underlyingAssetsId;

    @Column(name = "product_type")
    String productType = "futures";

    @Column(name = "symbol")
    String symbol;

    @Column(name = "standard_code")
    String standardCode;

    @Column(name = "listed_ymd")
    Integer listedYmd;

    @Column(name = "last_trading_ymd")
    Integer lastTradingYmd;

    @Column(name = "settlement_ymd")
    Integer settlementYmd;

    @Column(name = "trade_multiplier")
    BigDecimal tradeMultiplier;

    @Column(name = "description")
    String description;

    @Column(name = "data_value")
    String dataValue;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();


    @Override
    public String toString(){
        return TradingGson.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
}
