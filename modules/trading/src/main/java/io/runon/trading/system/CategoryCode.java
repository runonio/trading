package io.runon.trading.system;

import com.seomse.jdbc.annotation.Column;
import com.seomse.jdbc.annotation.Table;
import com.seomse.jdbc.annotation.PrimaryKey;
import com.seomse.jdbc.annotation.DateTime;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="category_code")
public class CategoryCode {

    @PrimaryKey(seq = 1)
    @Column(name = "category_id")
    String categoryId;

    @PrimaryKey(seq = 2)
    @Column(name = "code")
    String code;

    @Column(name = "name_ko")
    String nameKo;

    @Column(name = "name_en")
    String nameEn;

    @Column(name = "description")
    String description;

    @Column(name = "is_del")
    boolean isDel = false;

    @DateTime
    @Column(name = "updated_at")
    Long updatedAt;

}
