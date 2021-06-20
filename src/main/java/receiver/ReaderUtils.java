package receiver;

import java.io.BufferedReader;
import java.io.Reader;

public class ReaderUtils {

    public static BufferedReader createBufferedReader(final Reader reader) {
        return new BufferedReader(reader);
    }
}
