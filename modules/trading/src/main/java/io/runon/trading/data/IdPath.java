package io.runon.trading.data;

import lombok.Data;

/**
 * Id, 경로 정보
 * @author macle
 */
@Data
public class IdPath {
    String id;
    String path;

    public IdPath(){

    }

    public IdPath(String id, String path){
        this.id = id;
        this.path = path;
    }

}
