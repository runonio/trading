package io.runon.trading.closed.days;

import com.seomse.commons.service.Service;
import com.seomse.commons.utils.ExceptionUtil;
import com.seomse.commons.utils.time.Times;
import lombok.extern.slf4j.Slf4j;

/**
 * 호출은 3개월치까지 저장하고
 * 60일 이하의 일정만 저장되었을때 다시 호출한다
 * 이후 60일저장을 위한 서비스
 * 휴장일 정보갱신 서비스
 * @author macle
 */
@Slf4j
public class ClosedDaysFileOutService extends Service {
    


    public ClosedDaysFileOutService(){
        setSleepTime(Times.HOUR_4);
        setState(Service.State.START);
    }

    @Override
    public void work() {
        try{

        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }
    }
}
