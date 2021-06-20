package receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    public void setup() {
        mockInputStreamReader = Mockito.mock(InputStreamReader.class);
        mockBufferedReader = Mockito.mock(BufferedReader.class);
        mockInputStream = Mockito.mock(InputStream.class);
        streamReaderImpl = new StreamReaderImpl(mockInputStream);
    }

    @Test
    public void readAndProcessBatteryDataTest() throws Exception {
        String batteryReading1 = "Application has started - Getting Battery Parameters data.";
        String batteryReading2 = "Press Ctrl-C to end";
        String batteryReading3 = "{\"temperature\":50,\"stateOfCharge\":4.17}";
        String batteryReading4 = "{\"temperature\":85,\"stateOfCharge\":5.77}";
        String batteryReading5 = "Streaming stop event is triggered";
        PowerMockito.whenNew(InputStreamReader.class).withAnyArguments().thenReturn(mockInputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Mockito.any(InputStreamReader.class)).thenReturn(mockBufferedReader);
        String readLine = mockBufferedReader.readLine();
        Mockito.when(readLine).thenReturn(batteryReading1, batteryReading2, batteryReading3, batteryReading4, batteryReading5);
        streamReaderImpl.readAndProcessBatteryData();
    }

    @Test(expected = ApplicationException.class)
    public void readAndProcessBatteryDataTest_throwException() throws Exception {
        PowerMockito.whenNew(InputStreamReader.class).withAnyArguments().thenReturn(mockInputStreamReader);
        PowerMockito.whenNew(BufferedReader.class).withArguments(Mockito.any(InputStreamReader.class)).thenReturn(mockBufferedReader);
        String readLine = mockBufferedReader.readLine();
        Mockito.when(readLine).thenThrow(new IOException());
        streamReaderImpl.readAndProcessBatteryData();
    }

    @Test
    public void getMaxParametertest() {
        double maxValue = streamReaderImpl.getMaxParameter(45, 76);
        Assert.assertEquals(76, maxValue, 1e-15);
    }
}
