package io.runon.trading.data;

import io.runon.jdbc.annotation.Column;
import io.runon.jdbc.annotation.Table;
import io.runon.jdbc.annotation.PrimaryKey;
import io.runon.jdbc.annotation.DateTime;
import lombok.Data;

/**
 * @author macle
 */
@Data
@Table(name="collect_info")
public class CollectInfo {

    @PrimaryKey(seq = 1)
    @Column(name = "collect_id")
    String collectId;

    @PrimaryKey(seq = 2)
    @Column(name = "data_key")
    String dataKey;

    @Column(name = "collect_info")
    String collectInfo;

    @DateTime
    @Column(name = "collected_at")
    long collectedAt = System.currentTimeMillis();


    public CollectInfo(){

    }

    public CollectInfo(String collectId, String dataKey, String collectInfo){
        this.collectId = collectId;
        this.dataKey = dataKey;
        this.collectInfo = collectInfo;
    }

}
