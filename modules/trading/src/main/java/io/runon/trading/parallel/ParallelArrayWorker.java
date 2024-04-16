package io.runon.trading.parallel;

import com.seomse.commons.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author macle
 */
@Slf4j
public class ParallelArrayWorker<T> implements Runnable{

    private final ParallelArrayJob<T> job;

    private boolean isStop = false;

    ParallelArrayWorker(ParallelArrayJob<T> job){
        this.job = job;
    }

    @Override
    public void run() {

        ParallelArrayWork<T> work = job.work;

        try{

            for(;;){
                if(isStop){
                    break;
                }

                T t = job.get();
                if(t == null){
                    break;
                }

                work.work(t);
            }

        }catch (Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
        }

        job.endJob();
    }


    void stop(){
        isStop = true;
    }
}
