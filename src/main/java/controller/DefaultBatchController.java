package controller;

import logger.ProcessLogger;
import model.BatchResult;
import model.DataItem;
import model.ProcessResult;
import processor.DataProcessor;
import processor.ProcessFailException;
import reader.DataReader;
import writer.DataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class DefaultBatchController<T> implements BatchController<T> {
    private BatchResult result;
    private List<DataProcessor<T>> processorList;
    private List<ProcessLogger<T>> loggerList;
    private DataReader<T> dataReader = null;
    private List<DataItem<T>> processedList;
    private DataWriter<T> dataWriter;

    public DefaultBatchController() {
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

    public void setDataWriter(DataWriter<T> dataWriter) {
        this.dataWriter = dataWriter;
    }

    public BatchResult getResult() {
        return result;
    }

    public void startProcess() throws NoProcessorExistException, IOException {
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
                processItem(item);
                processCount++;
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

        writeResultLog(result);
        writeResult();
    }

    private void processItem(DataItem<T> item) throws ProcessFailException {
        for (DataProcessor<T> processor : processorList){
            ProcessResult<T> processResult = processor.processItem(item);
            item = processResult.getData();

            writeResult(processResult);
        }

        processedList.add(item);
    }

    private void writeResult() throws IOException {
        for (DataItem<T> item : processedList){
            dataWriter.writeData(item);
        }
    }

    private void writeResultLog(BatchResult result) {
        for (ProcessLogger<T> logger : loggerList){
            logger.writeResult(result);
        }
    }

    public List<DataItem<T>> getItemList(){
        return processedList;
    }

    private void writeResult(ProcessResult processResult) {
        for (ProcessLogger<T> logger : loggerList){
            logger.writeLog(processResult);
        }
    }
}
