package controller;

import logger.ProcessLogger;
import model.BatchResult;
import model.DataItem;
import model.ProcessResult;
import processor.DataProcessor;
import processor.ProcessFailException;
import reader.DataReader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class BatchController<T> {
    private BatchResult result;
    private List<DataProcessor<T>> processorList;
    private List<ProcessLogger<T>> loggerList;
    private DataReader<T> dataReader = null;
    private List<DataItem<T>> processedList;

    public BatchController() {
        processorList = new ArrayList<DataProcessor<T>>();
        loggerList = new ArrayList<ProcessLogger<T>>();
    }

    public void addProcessor(DataProcessor<T> processor) {
        if (processor == null)
            throw new IllegalArgumentException("processor must not be null");

        processorList.add(processor);
    }

    public void addProcessLogger(ProcessLogger<T> logger) {
        if (logger == null)
            throw new IllegalArgumentException("logger must not be null");

        loggerList.add(logger);
    }

    public void setDataReader(DataReader<T> reader) {
        if (reader == null)
            throw new IllegalArgumentException("reader must not be null");

        dataReader = reader;
    }

    public BatchResult getResult() {
        return result;
    }

    public void startProcess() throws NoProcessorExistException {
        if (processorList.size() == 0)
            throw new NoProcessorExistException();

        Date startTime = new Date();
        Date endTime;
        int processCount = 0;
        boolean success = true;
        processedList = new ArrayList<DataItem<T>>();

        while (dataReader.hasNext()){
            try{

                DataItem<T> item = dataReader.readNext();
                for (DataProcessor<T> processor : processorList){
                    ProcessResult processResult = processor.processItem(item);
                    item = processResult.getData();

                    writeLog(processResult);
                }

                processCount++;
                processedList.add(item);
            }
            catch (ProcessFailException e){
                success = false;
                break;
            }
        }

        endTime = new Date();

        result = new BatchResult(
                processCount,
                startTime,
                endTime,
                success
        );

        writeResult(result);
    }

    private void writeResult(BatchResult result) {
        for (ProcessLogger<T> logger : loggerList){
            logger.writeResult(result);
        }
    }

    public List<DataItem<T>> getItemList(){
        return processedList;
    }

    private void writeLog(ProcessResult processResult) {
        for (ProcessLogger<T> logger : loggerList){
            logger.writeLog(processResult);
        }
    }
}
