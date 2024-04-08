import io.runon.trading.data.file.TimeName;
import io.runon.trading.data.json.JsonTimeFilePathChange;

/**
 * 시간 형상의 파일을 새로운경로에 새로운 파일로 이관한다.
 * @author macle
 */
public class JsonTimeFilePathChangeExample {

    public static void main(String[] args) {

        JsonTimeFilePathChange pathChange = new JsonTimeFilePathChange();
        pathChange.setType(TimeName.Type.DAY_5);

        pathChange.outDirs("D:\\data\\cryptocurrency\\merge\\volume_backup","D:\\data\\cryptocurrency\\merge\\volume");

    }
}
