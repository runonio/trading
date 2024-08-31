package io.runon.trading.data;

import io.runon.trading.TradingConfig;

import java.nio.file.FileSystems;

/**
 * @author macle
 */
public class TradingDataPath {


    public static String getAbsolutePath(String relativePath){
        String fileSeparator = FileSystems.getDefault().getSeparator();

        String dataPth = TradingConfig.getTradingDataPath();


        if(fileSeparator.equals("\\")){
            relativePath = relativePath.replace("/", "\\");
        }else{
            relativePath = relativePath.replace("\\", "/");
        }

        return dataPth + fileSeparator + relativePath;
    }


    public static void main(String[] args) {
        String path = getAbsolutePath("commodities\\futures\\candle\\GBR_brent_oil\\1h");
        System.out.println(path);


    }
}
