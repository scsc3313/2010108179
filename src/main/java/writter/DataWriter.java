package writter;

import java.io.IOException;

/**
 * Created by ghost9087 on 2015. 11. 27..
 */
public interface DataWriter<T> {
    void writeData(T data) throws IOException;
}
