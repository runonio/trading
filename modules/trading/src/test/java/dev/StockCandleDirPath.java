package dev;

import io.runon.trading.CountryCode;
import io.runon.trading.data.candle.CandleDataUtils;

import java.nio.file.FileSystems;

/**
 * @author macle
 */
public class StockCandleDirPath {
    public static void main(String[] args) {
        String fileSeparator = FileSystems.getDefault().getSeparator();
        String filesDirPath = CandleDataUtils.getStockSpotCandlePath(CountryCode.kOR)+fileSeparator+"KOR_TEST"+fileSeparator+"1d";
        System.out.println(filesDirPath);
    }
}
