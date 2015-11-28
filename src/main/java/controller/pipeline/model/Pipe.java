package controller.pipeline.model;

import model.DataItem;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public interface Pipe<T> {
    void finish();

    void addItem(DataItem<T> dataItem);

    void setListener(PipelineProcessListener<T> listener);
}
