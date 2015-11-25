package model;

import java.util.Date;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class ProcessResult<T> {
    private final DataItem<T> data;
    private final Date startTime;
    private final Date endTime;
    private String processorName;
    private final boolean success;

    public ProcessResult(DataItem<T> data, Date startTime, Date endTime, boolean success, String processorName) {
        this.data = data;
        this.startTime = startTime;
        this.endTime = endTime;
        this.success = success;
        this.processorName = processorName;
    }

    public DataItem<T> getData() {
        return data;
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

    public String getProcessorName() {
        return processorName;
    }
}
