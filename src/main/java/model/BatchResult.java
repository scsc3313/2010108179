package model;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class BatchResult {
    private final int dataProcessed;

    public BatchResult(int dataProcessed) {
        this.dataProcessed = dataProcessed;
    }

    public int getDataProcessed() {
        return dataProcessed;
    }

}
