package reader;

import model.DataItem;

import java.io.IOException;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public interface DataReader<T> {
    DataItem<T> readNext() throws IOException;

    boolean hasNext() throws IOException;

    void rewindOnce() throws IOException;

    void rewind() throws IOException;
}
