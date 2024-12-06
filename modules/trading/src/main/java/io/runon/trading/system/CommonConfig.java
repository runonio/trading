package io.runon.trading.system;

import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.Table;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.DateTime;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="common_config")
public class CommonConfig {

    @PrimaryKey(seq = 1)
    @Column(name = "config_key")
    String key;

    @Column(name = "config_value")
    String value;

    @Column(name = "description")
    String description;

    @Column(name = "is_del")
    boolean isDel = false;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();

}
