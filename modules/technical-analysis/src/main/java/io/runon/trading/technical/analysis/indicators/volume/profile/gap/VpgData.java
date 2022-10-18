package io.runon.trading.technical.analysis.indicators.volume.profile.gap;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author macle
 */
@Data
public class VpgData {

    public static final Comparator<VpgData> SORT_PRICE = Comparator.comparing(o -> o.price);
    public static final Comparator<VpgData> SORT_VOLUME_DESC =  (o1, o2) -> {

        int compare = o2.volume.compareTo(o1.volume);

        if (compare == 0) {
            return o1.price.compareTo(o2.price);
        }
        return compare;
    };

    public static final VpgData [] EMPTY_ARRAY = new VpgData[0];

    //가격
    BigDecimal price;

    BigDecimal volume = BigDecimal.ZERO;

    //누적시간을 측정하기 위한 건수
    //캔들을 시가 고가 저가로 구분하기때문에 전체시간에서 3으로 나눈값을 사용하는게 좋을 수 있다
    int count = 0;

    
}
