package receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ReaderUtils.class })
public class StreamReaderImplTest {
    StreamReaderImpl          streamReaderImpl;
    private InputStreamReader mockInputStreamReader;
    private BufferedReader    mockBufferedReader;
    private InputStream       mockInputStream;

    @Before
    public void setup() throws Exception {
        mockInputStreamReader = Mockito.mock(InputStreamReader.class);
        mockBufferedReader = Mockito.mock(BufferedReader.class);
        mockInputStream = Mockito.mock(InputStream.class);
        streamReaderImpl = new StreamReaderImpl(mockInputStream);
		PowerMockito.whenNew(InputStreamReader.class).withAnyArguments().thenReturn(mockInputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Mockito.any(InputStreamReader.class)).thenReturn(mockBufferedReader);
    }

    @Test
    public void getAndProcessDataTest() throws Exception {
        String batteryReading1 = "{\"temperature\":50,\"stateOfCharge\":9.17}";
        String batteryReading2 = "{\"temperature\":85,\"stateOfCharge\":5.77}";
        String batteryReading3 = "Streaming stop event is triggered";
        Mockito.when(mockBufferedReader.readLine()).thenReturn(batteryReading1, batteryReading2, batteryReading3);
        Map<String, Double> parametersMap = streamReaderImpl.getAndProcessData(mockBufferedReader);
        Assert.assertNotNull(parametersMap);
        Assert.assertEquals(85, parametersMap.get(ReceiverConstants.TEMPERATURE), 1e-15);
        Assert.assertEquals(9.17, parametersMap.get(ReceiverConstants.STATE_OF_CHARGE), 1e-15);
        Assert.assertEquals(67.5, parametersMap.get(ReceiverConstants.AVERAGE_TEMPERATURE), 1e-15);
        Assert.assertEquals(7.47, parametersMap.get(ReceiverConstants.AVERAGE_SOC), 1e-15);

    }

    @Test(expected = ApplicationException.class)
    public void readAndProcessBatteryDataTest_throwException() throws Exception {
        Mockito.when(mockBufferedReader.readLine()).thenThrow(new IOException());
        streamReaderImpl.readAndProcessBatteryData();
    }

    @Test
    public void getMaxParameterTest() {
        double maxValue = streamReaderImpl.getMaxParameter(45, 76);
        Assert.assertEquals(76, maxValue, 1e-15);
    }

    @Test
    public void getBatterParametersTest() throws IOException {
        String batteryReading = "{\"temperature\":50,\"stateOfCharge\":4.17}";
        BatteryParameter batteryParameter = streamReaderImpl.getBatteryParameters(batteryReading);
        Assert.assertEquals(50, batteryParameter.getTemperature(), 1e-15);
        Assert.assertEquals(4.17, batteryParameter.getStateofCharge(), 1e-15);

    }
}
