import io.runon.trading.data.file.TimeLines;

public class TimeLineUpdateExample {
    public static void main(String[] args) {

        String [] lines = {
                "1664583540001,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                , "1664583540002,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540003,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540004,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540005,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540006,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540007,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540008,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540009,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
                ,"1664583540010,19472,19468.56,19472,19466.15,19468.56,22.65422,441061.0464354,342,13.18467,256705.9321429"
        };


        TimeLines.updateCandle("D:\\data\\temp\\1m", lines);
    }
}
