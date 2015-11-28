package controller;

import logger.ProcessLogger;
import model.BatchResult;
import model.DataItem;
import processor.DataProcessor;
import reader.DataReader;
import writter.DataWriter;

import java.io.IOException;
import java.util.List;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public interface BatchController<T> {
    void addProcessor(DataProcessor<T> processor);

    void addProcessLogger(ProcessLogger<T> logger);

    void setDataReader(DataReader<T> reader);

    void setDataWriter(DataWriter<T> dataWriter);

    BatchResult getResult();

    void startProcess() throws NoProcessorExistException, IOException;

    List<DataItem<T>> getItemList();
}
