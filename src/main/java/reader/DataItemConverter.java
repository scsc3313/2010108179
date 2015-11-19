package reader;

import model.DataItem;

/**
 * Created by ghost9087 on 2015. 11. 19..
 */
public interface DataItemConverter<T extends DataItem> {
    T itemFromByteArray(byte[] rawData);
}
