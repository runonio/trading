package io.runon.trading.parallel;

import com.seomse.commons.callback.Callback;
import com.seomse.commons.utils.time.Times;
import io.runon.trading.TradingConfig;

/**
 * @author macle
 */
public class ParallelArrayJob <T>{

    private final Object lock = new Object();


    private final Object endLock = new Object();

    //작업을 요구하는 항목
    private final T [] array;

    private Callback callback = null;
    private int threadCount = TradingConfig.getTradingThreadCount();

    final ParallelArrayWork<T> work;

    public ParallelArrayJob(T [] array, ParallelArrayWork<T> work){
        this.array = array;
        this.work = work;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setThreadCount(int threadCount) {
        if(threadCount < 1){
            threadCount = 1;
        }

        this.threadCount = threadCount;
    }

    private int index = 0;

    T get(){
        synchronized (lock) {
            if (index >= array.length) {
                return null;
            }
            return array[index++];
        }
    }

    private ParallelArrayWorker<T> [] workers;

    private Thread currentThread = null;


    //동기실행
    public void runSync(){
        currentThread = Thread.currentThread();
        runAsync();
        //noinspection ConditionalBreakInInfiniteLoop
        for(;;){

            if(isEnd){
                break;
            }
            try{
                Thread.sleep(Times.DAY_1);
            }catch (Exception ignore){}
        }

    }

    public void runAsync(){
        //noinspection unchecked
        workers = new ParallelArrayWorker[threadCount];
        for (int i = 0; i <workers.length ; i++) {
            workers[i] = new ParallelArrayWorker<>(this);
            new Thread(workers[i]).start();
        }
    }

    private int endCount = 0;

    boolean isEnd = false;

    void endJob(){
        synchronized (endLock){
            endCount ++;
            if(endCount >= workers.length){

                isEnd = true;

                if(currentThread != null){
                    try {
                        currentThread.interrupt();
                    }catch (Exception ignore){}
                }

                if(callback != null){
                    callback.callback();
                }
            }
        }
    }


    public void stop(){
        if(workers == null){
            return;
        }

        for(ParallelArrayWorker<T> worker : workers){
            worker.stop();
        }

    }

}
