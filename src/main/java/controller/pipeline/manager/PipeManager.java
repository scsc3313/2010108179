package controller.pipeline.manager;

import controller.pipeline.model.Pipe;
import model.DataItem;
import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class PipeManager<T> implements OnPipelineFinish<T> {
    private PipeWrapper<T> head;
    private int pipeCount = 0;
    final private OnPipelineFinish<T> finishListener;

    public PipeManager(OnPipelineFinish<T> finishListener) {
        this.finishListener = finishListener;
    }

    public void addPipe(Pipe<T> pipe) {
        if (head == null)
            head = new PipeWrapper<T>(pipe, this);
        else {
            PipeWrapper<T> last = head.getLastWrapper();
            last.setNextWrapper(new PipeWrapper<T>(pipe, this));
        }
        pipeCount++;
    }

    public int getPipeNumber() {
        return pipeCount;
    }

    public void addItem(DataItem<T> item) {
        if (item == null)
            throw new IllegalArgumentException("data item for processor must not be null");
        head.addItem(item);
    }

    public void onFinish(ProcessResult<T> result) {
        finishListener.onFinish(result);
    }
}
