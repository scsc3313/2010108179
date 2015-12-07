package controller.pipeline;

import controller.BatchController;
import controller.NoProcessorExistException;
import controller.pipeline.manager.OnPipelineFinish;
import controller.pipeline.manager.PipeManager;
import controller.pipeline.model.ThreadPipe;
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
 * Created by ghost9087 on 2015. 11. 28..
 */
public class PipelineBatchController<T> implements BatchController<T>, OnPipelineFinish<T> {
    private BatchResult result;
    private List<ProcessLogger<T>> loggerList;
    private DataReader<T> dataReader;
    private List<DataItem<T>> processedList;
    private DataWriter<T> dataWriter;
    final private PipeManager<T> pipeManager;
    private Date startTime;
    private int itemProcessed;
    private Thread mainThread;

    public PipelineBatchController() {
        processedList = new ArrayList<>();
        loggerList = new ArrayList<>();
        pipeManager = new PipeManager<>(this);
    }

    public void addProcessor(DataProcessor<T> processor) {
        if (processor == null)
            throw new IllegalArgumentException("processor must not be null");

        pipeManager.addPipe(new ThreadPipe<>(processor));
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
        mainThread = Thread.currentThread();
        if (pipeManager.getPipeNumber() == 0)
            throw new NoProcessorExistException();

        startTime = new Date();
        itemProcessed = 0;

        int itemRead = 0;
        while (dataReader.hasNext()){
            DataItem<T> item = dataReader.readNext();
            pipeManager.addItem(item);
            itemRead++;
        }
        itemProcessed = itemRead;

        while (itemProcessed != 0){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                if (result != null){
                    break;
                }
            }
        }

    }

    public List<DataItem<T>> getItemList() {
        return processedList;
    }

    public void onFinish(ProcessResult<T> result) {
        if (result.isSuccess()){
            processedList.add(result.getData());
            writeProcessLog(result);
        }
        else{
            pipeManager.stop();
            onBatchFinish(false);
        }

        if (isFinished()){
            onBatchFinish(true);
        }

    }

    private void writeProcessLog(ProcessResult<T> result) {
        for (ProcessLogger<T> logger : loggerList){
            logger.writeLog(result);
        }
    }

    private void onBatchFinish(boolean success) {
        result = new BatchResult(itemProcessed, startTime, new Date(), success);
        for (ProcessLogger<T> logger : loggerList){
            logger.writeResult(result);
        }

        if (success){
            for (DataItem<T> item : processedList){
                try {
                    dataWriter.writeData(item);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    break;
                }
            }
        }
        mainThread.interrupt();
    }

    private boolean isFinished(){
        return itemProcessed != 0 && itemProcessed == processedList.size();
    }
}
