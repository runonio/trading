import com.seomse.commons.utils.time.Times;
import io.runon.trading.data.file.TimeName;

/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class TimeFileNamesExample {
    public static void main(String[] args) {
        String [] names = TimeName.getNames(System.currentTimeMillis()- Times.DAY_10, System.currentTimeMillis(), TimeName.Type.DAY_2);
        for(String name: names){
            System.out.println(name);
        }
    }
}
