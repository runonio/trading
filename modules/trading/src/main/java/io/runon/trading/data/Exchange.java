package io.runon.trading.data;

import io.runon.commons.utils.GsonUtils;
import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.DateTime;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.Table;
import lombok.Data;

/**
 * Id, 경로 정보
 * @author macle
 */
@Data
@Table(name="exchange")
public class Exchange {


    @PrimaryKey(seq = 1)
    @Column(name = "exchange")
    String exchange;

    @Column(name = "country")
    String country;

    @Column(name = "currency")
    String currency;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "description")
    String description;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();


    @Override
    public String toString(){
        return GsonUtils.LOWER_CASE_WITH_UNDERSCORES.toJson(this);
    }
}
