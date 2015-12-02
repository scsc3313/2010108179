package writer;

import model.DataItem;

import java.io.IOException;

/**
 * Created by ghost9087 on 2015. 11. 27..
 */
public interface DataWriter<T> {
    void writeData(DataItem<T> data) throws IOException;
}
