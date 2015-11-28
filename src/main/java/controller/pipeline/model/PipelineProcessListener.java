package controller.pipeline.model;

import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public interface PipelineProcessListener<T> {
    void onProcess(ProcessResult<T> result);
}
