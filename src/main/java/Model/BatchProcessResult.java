package model;

import java.util.List;

/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class BatchProcessResult<T extends DataItem> {
    private int errorCode;
    private List<DataItem<T>> resultData;

    public int getErrorCode() {
        return errorCode;
    }
}
