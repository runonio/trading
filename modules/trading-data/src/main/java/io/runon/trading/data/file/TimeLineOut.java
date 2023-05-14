package io.runon.trading.data.file;

import com.seomse.commons.utils.FileUtil;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author macle
 */
public class TimeLineOut {

    private final String dirPath;
    private final TimeLine timeLine;

    private final TimeName timeName;

    private final Object lock = new Object();
    public TimeLineOut(String dirPath, TimeLine timeLine, TimeName timeName){
        this.dirPath = dirPath;
        this.timeLine = timeLine;
        this.timeName = timeName;
    }

    public void update(String [] lines){
        Arrays.sort(lines,  Comparator.comparingLong(timeLine::getTime));

        String lastFileName = timeName.getName(timeLine.getTime(lines[0]));
        List<String> lineList = new ArrayList<>();
        lineList.add(lines[0]);

        synchronized (lock) {
            for (int i = 1; i < lines.length ; i++) {
                String line = lines[i];
                String fileName = timeName.getName(timeLine.getTime(line));
                if(!fileName.equals(lastFileName)){
                    update(lastFileName, lineList);
                    lineList.clear();
                    lastFileName = fileName;
                }
                lineList.add(line);
            }

            if(lineList.size() > 0){
                update(lastFileName, lineList);
                lineList.clear();
            }

        }
    }

    /**
     * 정렬없이 데이터추가
     * 업데이트가 필요없는 빠른데이터 추가
     * @param lines 라인데이터 추가
     */
    public void add(String [] lines){
        String lastFileName = timeName.getName(timeLine.getTime(lines[0]));
        List<String> lineList = new ArrayList<>();
        lineList.add(lines[0]);
        synchronized (lock) {
            for (int i = 1; i < lines.length ; i++) {
                String line = lines[i];
                String fileName = timeName.getName(timeLine.getTime(line));
                if(!fileName.equals(lastFileName)){
                    String path = dirPath + "/" + lastFileName;
                    FileUtil.fileOutput(outValue(lineList), path, false);
                    lineList.clear();
                    lastFileName = fileName;
                }
                lineList.add(line);
            }

            if(lineList.size() > 0){
                String path = dirPath + "/" + lastFileName;
                FileUtil.fileOutput(outValue(lineList), path, false);
                lineList.clear();
            }
        }
    }

    public void update(String fileName, List<String> lineList){
        if(lineList.size() == 0){
            return ;
        }

        String path = dirPath + "/" + fileName;

        if(FileUtil.isFile(path)){

            String saveLastLine = FileUtil.getLastTextLine(path);
            if(saveLastLine.equals("")){
                //내용이 없으면 파일이 비어 있으면
                FileUtil.fileOutput(outValue(lineList), path, false);
                return;
            }

            long lastSaveTime = timeLine.getTime(saveLastLine);
            long firstTime = timeLine.getTime(lineList.get(0));
            if(firstTime > lastSaveTime){
                //저장된 시간보다크면 뒤에다 붙임
                FileUtil.fileOutput(outValue(lineList), path, true);

            }else if(firstTime == lastSaveTime ){
                //마지막 한줄만 수정이면

                List<String> saveLines =  FileUtil.getLineList(new File(path), StandardCharsets.UTF_8);
                int size = saveLines.size()-1;
                StringBuilder sb = new StringBuilder();
                sb.append(saveLines.get(0));
                for (int i = 1; i <size ; i++) {
                    sb.append("\n").append(saveLines.get(i));
                }
                for(String line : lineList){
                    sb.append("\n").append(line);
                }
                FileUtil.fileOutput(sb.toString(), path, false);

            }else{
                //석여있으면 중복이 있으면 새로운 라인으로 교체후 파일 변경
                List<String> saveLines =  FileUtil.getLineList(new File(path), StandardCharsets.UTF_8);

                Set<Long> checkSet = new HashSet<>();

                List<LineTime> newList = new ArrayList<>();
                for(String line : lineList){
                    long time = timeLine.getTime(line);
                    checkSet.add(time);
                    newList.add(new LineTime(line, time));
                }

                List<LineTime> sumList = new ArrayList<>();
                for(String line: saveLines){
                    long time = timeLine.getTime(line);
                    if(checkSet.contains(time)){
                        continue;
                    }
                    sumList.add(new LineTime(line,time));
                }
                sumList.addAll(newList);

                LineTime[] array = sumList.toArray(new LineTime[0]);

                Arrays.sort(array, LineTime.SORT);

                StringBuilder sb = new StringBuilder();
                sb.append(array[0].line);
                for (int i = 1; i <array.length ; i++) {
                    sb.append("\n").append(array[i].line);
                }
                FileUtil.fileOutput(sb.toString(), path, false);
                sumList.clear();
                newList.clear();
            }
        }else{
            FileUtil.fileOutput(outValue(lineList), path, false);
        }
    }


    public String outValue(List<String> lineList){
        StringBuilder sb = new StringBuilder();
        sb.append(lineList.get(0));

        int size = lineList.size();
        for (int i = 1; i <size ; i++) {
            sb.append("\n").append(lineList.get(i));
        }

        return sb.toString();
    }

}
