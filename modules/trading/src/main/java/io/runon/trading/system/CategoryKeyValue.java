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
@Table(name="category_key_value")
public class CategoryKeyValue {

    @PrimaryKey(seq = 1)
    @Column(name = "category_id")
    String categoryId;

    @PrimaryKey(seq = 2)
    @Column(name = "data_key")
    String key;

    @Column(name = "data_value")
    String value;

    @DateTime
    @Column(name = "updated_at")
    long updatedAt = System.currentTimeMillis();
}
