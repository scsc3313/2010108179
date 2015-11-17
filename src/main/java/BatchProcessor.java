/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class BatchProcessor {
    private ProcessResult result;
    private DataReader dataReader;
    private DataProcessor dataProcessor;

    public BatchProcessor(DataReader dataReader, DataProcessor dataProcessor) {
        this.dataReader = dataReader;
        this.dataProcessor = dataProcessor;
    }

    public void doBatch() {

    }

    public ProcessResult getResult() {
        return result;
    }
}
