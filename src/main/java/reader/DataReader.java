package reader;

import model.DataItem;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public interface DataReader<T> {
    DataItem<T> readNext();

    boolean hasNext();
}
