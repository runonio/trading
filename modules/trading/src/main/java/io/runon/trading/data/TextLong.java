package io.runon.trading.data;

import lombok.Data;

import java.util.Comparator;

/**
 * 문자열과 long
 * id와 long 형 시간값을 처리하는 경우가 많아서 만듬.
 * 좀더 범용적으로 사용될 수 있다고 보여서
 * @author macle
 */
@Data
public class TextLong {

    public final static Comparator<TextLong> SORT_DESC = (o1, o2) -> Long.compare(o2.number, o1.number);
    public final static Comparator<TextLong> SORT_ASC = Comparator.comparingLong(o -> o.number);


    String text;
    long number;

    public TextLong(){

    }

    public TextLong(String text, long number){
        this.text = text;
        this.number = number;
    }

}
