package receiver;

public class ReceiverApp {

    public static void main(String[] args) throws ApplicationException {
        IStreamReader streamReader = new StreamReaderImpl(System.in);
        streamReader.readAndProcessBatteryData();

    }

}
