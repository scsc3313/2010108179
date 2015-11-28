package controller.pipeline.manager;

import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public interface OnPipelineFinish<T> {
    void onFinish(ProcessResult<T> result);
}
