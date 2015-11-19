import model.BatchProcessResult;
import processor.DataProcessor;
import reader.DataReader;

/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class BatchProcessor {
    private BatchProcessResult result;
    private DataReader<?> dataReader;
    private DataProcessor<?> dataProcessor;

    public BatchProcessor(DataReader<?> dataReader, DataProcessor<?> dataProcessor) {
        this.dataReader = dataReader;
        this.dataProcessor = dataProcessor;

        init();

    }

    private void init() {

    }

    public void doBatch() {

    }

    public BatchProcessResult getResult() {
        return result;
    }
}
