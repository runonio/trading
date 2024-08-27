package io.runon.trading.data;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.Table;
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

    @Column(name = "updated_at")
    long updatedAt;
}
