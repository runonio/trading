package io.runon.trading.data.jdbc;

import lombok.Data;
/**
 * 데이터 시간 범위
 * @author macle
 */
@Data
public class TableNameClass {
    private String tableName;
    private Class<?> classes;

    public TableNameClass(){

    }

    public TableNameClass(String tableName, Class<?> classes){
        this.tableName = tableName;
        this.classes = classes;
    }

}
