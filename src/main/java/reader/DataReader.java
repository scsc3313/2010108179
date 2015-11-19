package reader;

import model.DataItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class DataReader<T extends DataItem> {
    protected StreamReader reader;
    private DataItemConverter<T> converter;

    public DataReader(StreamReader reader, DataItemConverter<T> converter) {
        if (reader == null) {
            throw new IllegalArgumentException("input stream must not be null");
        }
        if (converter == null) {
            throw new IllegalArgumentException("converter must not be null");
        }

        this.converter = converter;
        this.reader = reader;
    }

    public T readNext() throws IOException {
        byte[] bytes = reader.readNextByteElement();

        return converter.itemFromByteArray(bytes);
    }

    public List<T> readFully() throws IOException {
        T dataItem;

        List<T> dataList = new ArrayList<T>();

        while ((dataItem = readNext()) != null){
            dataList.add(dataItem);
        }

        return dataList;
    }
}
