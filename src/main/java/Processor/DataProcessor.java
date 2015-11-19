package processor;

import model.DataItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public abstract class DataProcessor<T extends DataItem> {
    private List<T> dataList;

    public DataProcessor() {
        this.dataList = new ArrayList<T>();
    }

    public void startProcess(T item) throws ProcessFailException{
        if (item == null || item.getData() == null)
            throw new ProcessFailException();

        T processed = processItem(item);

        dataList.add(processed);
    }

    public T getDataAtIndex(int index){
        return dataList.get(index);
    }

    protected abstract T processItem(T item) throws ProcessFailException;
}
