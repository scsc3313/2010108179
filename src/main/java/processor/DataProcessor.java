package processor;

import model.DataItem;
import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public interface DataProcessor<T> {
    ProcessResult<T> processItem(DataItem<T> item) throws ProcessFailException;
}
