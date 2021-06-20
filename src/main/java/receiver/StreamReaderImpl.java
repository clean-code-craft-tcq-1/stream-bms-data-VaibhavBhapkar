package receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamReaderImpl implements IStreamReader {
    private final ObjectMapper  mapper;
    private final MovingAverage movingAverageOfTemp;
    private final MovingAverage movingAverageOfSoc;
    private final InputStream   fInputStream;

    public StreamReaderImpl(InputStream inputStream) {
        fInputStream = inputStream;
        mapper = new ObjectMapper();
        movingAverageOfTemp = new MovingAverage(5); // Average of last 5 values
        movingAverageOfSoc = new MovingAverage(5);
    }

    @Override
    public void readAndProcessBatteryData() throws ApplicationException {
        double maxTemp = 0;
        double maxSOC = 0;
        try (BufferedReader bufOut = ReaderUtils.createBufferedReader(new InputStreamReader(fInputStream))) {
            String line = getLine(bufOut); // Input: Application has started - Getting Battery Parameters data.
            getLine(bufOut); // Input: Press Ctrl-C to end
            while (true) {
                line = getLine(bufOut);
                if (line.isBlank() || line.equalsIgnoreCase(ReceiverConstants.END_OF_INPUT)) {
                    break;
                }
                BatteryParameter batteryParameter = mapper.readValue(line, BatteryParameter.class);
                maxTemp = getMaxParameter(maxTemp, batteryParameter.getTemperature());
                maxSOC = getMaxParameter(maxSOC, batteryParameter.getStateofCharge());
                printMaxValue(ReceiverConstants.TEMPERATURE, maxTemp);
                printMaxValue(ReceiverConstants.STATE_OF_CHARGE, maxSOC);
                printMovingAverage(ReceiverConstants.TEMPERATURE, movingAverageOfTemp.next(batteryParameter.getTemperature()));
                printMovingAverage(ReceiverConstants.STATE_OF_CHARGE, movingAverageOfSoc.next(batteryParameter.getStateofCharge()));
                markEndOfReading();
            }
        } catch (IOException e) {
            System.out.println(ReceiverConstants.ERROR_MESSAGE);
            throw new ApplicationException(ReceiverConstants.ERROR_MESSAGE, e);
        }
    }

    private String getLine(BufferedReader bufOut) throws IOException {
        return bufOut.readLine();
    }

    public double getMaxParameter(double maxValue, double currentValue) {
        maxValue = Math.max(maxValue, currentValue);
        return maxValue;
    }

    private void printMaxValue(String param, double value) {
        System.out.println("Maximum " + param + " recorded so far: " + value);
    }

    private void printMovingAverage(String param, double value) {
        System.out.println("Simple Moving Average of " + param + ": " + value);
    }

    private void markEndOfReading() {
        System.out.println("-----------------------------------------------------------");
    }
}
