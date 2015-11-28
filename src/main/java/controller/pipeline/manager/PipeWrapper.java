package controller.pipeline.manager;

import controller.pipeline.model.Pipe;
import controller.pipeline.model.PipelineProcessListener;
import model.DataItem;
import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class PipeWrapper<T> implements PipelineProcessListener<T> {
    final private Pipe<T> pipe;
    final private OnPipelineFinish<T> onFinish;
    private PipeWrapper<T> nextWrapper;

    public PipeWrapper(Pipe<T> pipe, OnPipelineFinish<T> onFinish) {
        this.pipe = pipe;
        pipe.setListener(this);
        this.onFinish = onFinish;
    }

    public void onProcess(ProcessResult<T> result) {
        DataItem<T> data = result.getData();
        if (nextWrapper != null && result.isSuccess())
            nextWrapper.addItem(data);
        else
            onFinish.onFinish(result);

    }

    public void addItem(DataItem<T> item){
        pipe.addItem(item);
    }

    public void setNextWrapper(PipeWrapper<T> nextWrapper) {
        this.nextWrapper = nextWrapper;
    }

    public PipeWrapper<T> getLastWrapper() {
        if (nextWrapper == null)
            return this;
        else
            return nextWrapper;
    }

}
