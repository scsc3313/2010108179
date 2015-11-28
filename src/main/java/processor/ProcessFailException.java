package processor;

import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 25..
 */
public class ProcessFailException extends Exception{
    final private ProcessResult result;
    public ProcessFailException(ProcessResult<?> result) {
        this.result = result;
    }

    public ProcessResult getFailResult() {
        return result;
    }
}
