package logger;

import model.BatchResult;

/**
 * Created by ghost9087 on 2015. 11. 27..
 */
public interface BatchResultFormatter {
    String convert(BatchResult result);
}
