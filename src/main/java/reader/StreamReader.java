package reader;

import java.io.IOException;

/**
 * Created by ghost9087 on 2015. 11. 19..
 */
public interface StreamReader {
    byte[] readNextByteElement() throws IOException;
}
