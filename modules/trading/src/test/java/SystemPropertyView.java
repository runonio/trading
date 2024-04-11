import java.util.Enumeration;
/**
 * csv 파일을 활용한 캔들 생성
 * @author macle
 */
public class SystemPropertyView {
    public static void main(String[] args) {
        //시스템 프로퍼티 찍어보기
        Enumeration<Object> keys = System.getProperties().keys();

        while(keys.hasMoreElements()){
            try {
                String key = (String) keys.nextElement();
                if(key.equals("line.separator")){
                    continue;
                }
                System.out.println(key +"="+System.getProperty(key));
            }catch (Exception ignore){}
        }

    }
}
