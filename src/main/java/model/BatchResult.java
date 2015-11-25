package model;

import java.util.Date;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class BatchResult {
    private final int dataProcessed;
    private final Date startTime;
    private final Date endTime;
    private final boolean success;

    public BatchResult(int dataProcessed, Date startTime, Date endTime, boolean success) {
        this.dataProcessed = dataProcessed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.success = success;
    }

    public int getDataProcessed() {
        return dataProcessed;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isSuccess() {
        return success;
    }
}
