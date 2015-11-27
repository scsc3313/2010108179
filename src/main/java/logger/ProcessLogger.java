package logger;

import model.BatchResult;
import model.ProcessResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public abstract class ProcessLogger<T> {
    private ProcessResultFormatter processResultFormatter;
    private BatchResultFormatter batchResultFormatter;

    public ProcessLogger(ProcessResultFormatter processResultFormatter, BatchResultFormatter batchResultFormatter) {
        this.processResultFormatter = processResultFormatter;
        this.batchResultFormatter = batchResultFormatter;
    }

    public void writeLog(ProcessResult<T> result) {
        String message = processResultFormatter.convert(result);
        writeMesaage(message);
    }
    public void writeResult(BatchResult result){
        String message = batchResultFormatter.convert(result);
        writeMesaage(message);
    }

    abstract protected void writeMesaage(String message);
}
