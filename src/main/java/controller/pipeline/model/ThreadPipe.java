package controller.pipeline.model;

import model.DataItem;
import model.ProcessResult;
import processor.DataProcessor;
import processor.ProcessFailException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class ThreadPipe<T> implements Runnable, Pipe<T> {
    final private DataProcessor<T> processor;
    final private BlockingQueue<DataItem<T>> pipeQueue;
    final private Thread thread;
    private PipelineProcessListener<T> listener;
    private boolean work;

    public ThreadPipe(DataProcessor<T> processor) {
        work = true;
        pipeQueue = new LinkedBlockingQueue<>();
        this.processor = processor;
        thread = new Thread(this);
        thread.start();
    }

    public void finish(){
        work = false;
        pipeQueue.clear();
    }

    public void run() {
        while (work){
            DataItem<T> item;
            try {
                while ((item=pipeQueue.poll(500, TimeUnit.MICROSECONDS)) != null){
                    ProcessResult<T> result;
                    try {
                        result = processor.processItem(item);
                    } catch (ProcessFailException e) {
                        result = e.getFailResult();
                    }
                    listener.onProcess(result);
                }
            }catch (InterruptedException ignore){}
        }
    }


    public void addItem(DataItem<T> dataItem) {
        pipeQueue.add(dataItem);
        if (!work){
            work = true;
            thread.run();
        }
    }

    public void setListener(PipelineProcessListener<T> listener) {
        this.listener = listener;
    }
}
