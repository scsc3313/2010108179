package logger;

import model.ProcessResult;

/**
 * Created by ghost9087 on 2015. 11. 27..
 */
public interface ProcessResultFormatter<T> {
    String convert(ProcessResult<T> item);
}
