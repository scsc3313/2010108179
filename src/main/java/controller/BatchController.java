package controller;

import logger.ProcessLogger;
import model.BatchResult;
import model.DataItem;
import model.ProcessResult;
import processor.DataProcessor;
import processor.ProcessFailException;
import reader.DataReader;
import writter.DataWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class BatchController<T> implements Runnable{
    private BatchResult result;
    private List<DataProcessor<T>> processorList;
    private List<ProcessLogger<T>> loggerList;
    private DataReader<T> dataReader = null;
    private List<DataItem<T>> processedList;
    private DataWriter<T> dataWriter;
    private Thread readThread;
    final private BlockingQueue<DataItem<T>> itemQueue;

    public BatchController() {
        processorList = new ArrayList<DataProcessor<T>>();
        loggerList = new ArrayList<ProcessLogger<T>>();
        readThread = new Thread(this);
        itemQueue = new LinkedBlockingQueue<DataItem<T>>();
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

        readThread.start();

        while (!itemQueue.isEmpty()){
            try{
                DataItem<T> item = itemQueue.poll();
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

            writeLog(processResult);
        }

        processedList.add(item);
    }

    private void writeResult() throws IOException {
        for (DataItem<T> item : processedList){
            dataWriter.writeData(item.getData());
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

    private void writeLog(ProcessResult processResult) {
        for (ProcessLogger<T> logger : loggerList){
            logger.writeLog(processResult);
        }
    }

    public void run() {
        DataItem<T> failToPut = null;
        while (dataReader.hasNext()){
            DataItem<T> item;
            if (failToPut != null)  item = failToPut;
            else                    item = dataReader.readNext();

            try {
                itemQueue.put(item);
                Thread.sleep(10);
            }
            // FIXME : Exception을 무시하지 말고 처리할 것
            catch (InterruptedException e) {
                failToPut = item;
            }
        }
    }
}
