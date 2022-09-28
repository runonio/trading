package io.runon.trading;

import com.seomse.commons.config.Config;
import com.seomse.commons.utils.time.Times;

/**
 * 숫자 데이터
 * @author macle
 */
public abstract class TimeDataStorage<E extends Time> {

    protected E [] dataArray;

    protected long lastTime = -1;

    protected int maxLength = Config.getInteger("time.data.array.length.max",1000000);

    protected long dataTimeGap = Times.DAY_1;

    public void setDataArray(E[] dataArray) {
        this.dataArray = dataArray;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setDataTimeGap(long dataTimeGap) {
        this.dataTimeGap = dataTimeGap;
    }


    protected final Object lock = new Object();

    public void setArray(E add, E [] newArray){
        int maxLength = this.maxLength;

        synchronized (lock){
            if(lastTime > add.getTime()){
                return;
            }
            if(dataArray.length >= maxLength){
                int gap = dataArray.length - (maxLength - 1);

                int index = 0;
                for (int i = gap; i <dataArray.length ; i++) {
                    newArray[index++] = dataArray[i];
                }
                newArray[newArray.length-1] = add;
                this.dataArray = newArray;

            }else{
                System.arraycopy(dataArray, 0, newArray, 0, dataArray.length);
                newArray[newArray.length-1] = add;
                this.dataArray = newArray;
            }

            lastTime = add.getTime();

        }
    }

    public void setArray(E [] addArray, E [] newArray){
        int maxLength = this.maxLength;

        synchronized (lock){

            if(addArray.length> maxLength) {

                int gap = addArray.length - maxLength;
                int index = 0;
                for (int i = gap; i <addArray.length ; i++) {
                    newArray[index++] = addArray[i];
                }
                this.dataArray = newArray;

            }else if(dataArray.length + addArray.length > maxLength){
                int gap = dataArray.length - (maxLength - addArray.length);

                int index = 0;
                for (int i = gap; i <dataArray.length ; i++) {
                    newArray[index++] = dataArray[i];
                }

                for (E data : addArray) {
                    newArray[index++] = data;
                }

                this.dataArray = newArray;

            }else{
                System.arraycopy(dataArray, 0, newArray, 0, dataArray.length);

                int index = dataArray.length;
                for (E data : addArray) {
                    newArray[index++] = data;
                }

                this.dataArray = newArray;
            }

            lastTime =  this.dataArray[dataArray.length-1].getTime();

        }
    }

    public E getData(long time){
        //데이터가 변경될 수 있기때문 먼저 선언.
        E [] dataArray = this.dataArray;

        if(dataArray == null || dataArray.length == 0){
            return null;
        }
        int lsatIndex = dataArray.length-1;

        long lsatTime = dataArray[lsatIndex].getTime();

        if(lsatTime < time){
            return dataArray[lsatIndex];
        }


        int startIndex = lsatIndex  - (int)((lsatTime - time)/dataTimeGap);

        if(startIndex < 0){
            startIndex = 0;
        }

        if(startIndex == 0){
            E data = dataArray[0];
            if(data.getTime() == time){
                return data;
            }

            //이전 시간의 미체결 약정값은 구할 수 없음
            if(data.getTime() > time){
                return null;
            }
        }

        int idx = -1;
        for (int i = startIndex + 1; i <dataArray.length ; i++) {
            E data = dataArray[i];
            if(data.getTime() == time){
                return data;
            }

            if(data.getTime() > time){
                return dataArray[i-1];
            }

        }



        return null;
    }


    public E getData(){

        if(dataArray == null || dataArray.length == 0){
            return null;
        }

        return dataArray[dataArray.length-1];
    }


}
