package controller;

import logger.ProcessLogger;
import model.BatchResult;
import processor.DataProcessor;
import reader.DataReader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class BatchController<T> {
    private BatchResult result;

    public void addProcessor(DataProcessor<T> processor) {
        throw new NotImplementedException();
    }

    public void addProcessLogger(ProcessLogger<T> logger) {
        throw new NotImplementedException();
    }

    public void setDataReader(DataReader reader) {
        throw new NotImplementedException();
    }

    public BatchResult getResult() {
        return result;
    }

    public void startProcess() {

    }
}
