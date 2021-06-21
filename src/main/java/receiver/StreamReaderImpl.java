package receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
        try (BufferedReader bufOut = ReaderUtils.createBufferedReader(new InputStreamReader(fInputStream))) {
            getLine(bufOut); // Input: Application has started - Getting Battery Parameters data.
            getLine(bufOut); // Input: Press Ctrl-C to end
            getAndProcessData(bufOut);
        } catch (IOException e) {
            System.out.println(ReceiverConstants.ERROR_MESSAGE);
            throw new ApplicationException(ReceiverConstants.ERROR_MESSAGE, e);
        }
    }

    public Map<String, Double> getAndProcessData(BufferedReader bufOut) throws IOException {
        Map<String, Double> parametersMap = new HashMap<>();
        double maxTemp = 0;
        double maxSOC = 0;
        while (true) {
            String line = getLine(bufOut);
            if (line == null || line.equalsIgnoreCase(ReceiverConstants.END_OF_INPUT)) {
                break;
            }
            parametersMap.clear();
            BatteryParameter batteryParameter = getBatteryParameters(line);
            maxTemp = getMaxParameter(maxTemp, batteryParameter.getTemperature());
            maxSOC = getMaxParameter(maxSOC, batteryParameter.getStateofCharge());
            parametersMap.put(ReceiverConstants.TEMPERATURE, maxTemp);
            parametersMap.put(ReceiverConstants.STATE_OF_CHARGE, maxSOC);
            parametersMap.put(ReceiverConstants.AVERAGE_TEMPERATURE, movingAverageOfTemp.next(batteryParameter.getTemperature()));
            parametersMap.put(ReceiverConstants.AVERAGE_SOC, movingAverageOfSoc.next(batteryParameter.getStateofCharge()));
            printValues(parametersMap);
        }
        return parametersMap;
    }

    public BatteryParameter getBatteryParameters(String line) throws IOException, JsonParseException, JsonMappingException {
        BatteryParameter batteryParameter = mapper.readValue(line, BatteryParameter.class);
        return batteryParameter;
    }

    public double getMaxParameter(double maxValue, double currentValue) {
        maxValue = Math.max(maxValue, currentValue);
        return maxValue;
    }

    private void printValues(Map<String, Double> parametersMap) {
        printMaxValue(ReceiverConstants.TEMPERATURE, parametersMap.get(ReceiverConstants.TEMPERATURE));
        printMaxValue(ReceiverConstants.STATE_OF_CHARGE, parametersMap.get(ReceiverConstants.STATE_OF_CHARGE));
        printMovingAverage(ReceiverConstants.TEMPERATURE, parametersMap.get(ReceiverConstants.AVERAGE_TEMPERATURE));
        printMovingAverage(ReceiverConstants.STATE_OF_CHARGE, parametersMap.get(ReceiverConstants.AVERAGE_SOC));
        markEndOfEachData();
    }

    private String getLine(BufferedReader bufOut) throws IOException {
        return bufOut.readLine();
    }

    private void printMaxValue(String param, double value) {
        System.out.println("Maximum " + param + " recorded so far: " + value);
    }

    private void printMovingAverage(String param, double value) {
        System.out.println("Simple Moving Average of " + param + ": " + value);
    }

    private void markEndOfEachData() {
        System.out.println("-----------------------------------------------------------");
    }
}
