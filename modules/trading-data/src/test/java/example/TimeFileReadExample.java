package example;

import io.runon.trading.data.StringRead;
import io.runon.trading.data.file.TimeFileRead;

/**
 * 시간정보가 있는 파일 읽기 예제
 * @author macle
 */
public class TimeFileReadExample {

    private int count = 0;


    public void read(String path){
        TimeFileRead timeFileRead = new TimeFileRead();
        StringRead read = str -> {
            count ++;

            System.out.println(str);

            if(count >= 5){
                timeFileRead.endRead();
            }
        };
        
        
        //앞에서 읽기
        System.out.println("앞에서 읽기");
        count = 0;
        timeFileRead.fromBegin(path, read);
        
        
        //뒤에서 읽기
        System.out.println("뒤에서 읽기");
        count = 0;
        timeFileRead.fromEnd(path, read);
    }


    public static void main(String[] args)  {
        new TimeFileReadExample().read("C:\\data\\trd\\vm");

    }

}
