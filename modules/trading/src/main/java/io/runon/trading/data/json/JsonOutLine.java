package io.runon.trading.data.json;
/**
 * @author macle
 */
public interface JsonOutLine {

    static String [] getLines(JsonOutLine [] array){
        String [] lines = new String[array.length];
        for (int i = 0; i <lines.length ; i++) {
            lines[i] = array[i].outTimeLineJsonText();
        }

        return lines;
    }

    String outTimeLineJsonText();

}
