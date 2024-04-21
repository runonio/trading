package io.runon.trading.data;

import java.math.BigDecimal;

/**
 * 데이터 변화 체크
 * 데이터가 변하면 정보를 업데이트 해야한다.
 * 데이터 변화여부 체크 (기준시간 변화등)
 * @author macle
 */
public class EqualsNullCheck {


    public static boolean equals(String source, String target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }

        return source.equals(target);

    }

    public static boolean equals(Long source, Long target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }


        return source.longValue() == target.longValue();

    }

    public static boolean equals(Integer source, Integer target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }

        return source.intValue() == target.intValue();
    }

    public static boolean equals(BigDecimal source, BigDecimal target){
        if(source== null && target == null){
            return true;
        }

        if(source != null && target == null){
            return false;
        }

        if(source == null){
            return false;
        }


        return source.compareTo(target) == 0;

    }

}
