package controller.pipeline;

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
public class Pipe<T> implements Runnable{
    final private DataProcessor<T> processor;
    final private BlockingQueue<DataItem<T>> pipeQueue;
    final private Thread thread;
    final PipelineProcessListener<T> listener;
    private boolean work;

    public Pipe(DataProcessor<T> processor, PipelineProcessListener<T> listener) {
        work = true;
        this.listener = listener;
        pipeQueue = new LinkedBlockingQueue<DataItem<T>>();
        this.processor = processor;
        thread = new Thread(this);
        thread.start();
    }

    public void finish(){
        work = false;
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
            }catch (InterruptedException e){
                continue;
            }
        }
    }


    public void addItem(DataItem<T> dataItem) {
        pipeQueue.add(dataItem);
        if (pipeQueue.size() > 0){
            thread.interrupt();
        }
    }
}
