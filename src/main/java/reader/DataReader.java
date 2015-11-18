package reader;

import model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public abstract class DataReader<T extends DataItem> {
    protected InputStream inputStream;

    public DataReader(InputStream inputStream) {
        if (inputStream == null)
            throw new NullPointerException("input stream must be initiated");

        this.inputStream = inputStream;
    }

    public T readNext() throws IOException {
        return readFromStream();
    }

    public List<T> readFully() throws IOException {
        T dataItem = null;

        List<T> dataList = new ArrayList<T>();

        while ((dataItem = readNext()) != null){
            dataList.add(dataItem);
        }

        return dataList;
    }

    protected abstract T readFromStream() throws IOException;
}
