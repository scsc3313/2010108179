package logger;

import model.DataItem;
import model.ProcessResult;
import util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class DefaultProcessResultFormatter<T> implements ProcessResultFormatter<T>{
    @Override
    public String convert(ProcessResult<T> result) {
        DataItem<T> data = result.getData();
        StringBuilder builder = new StringBuilder();

        builder.append("type : ")
                .append(data.getType())
                .append(", seq : ")
                .append(data.getSeqeunce())
                .append(", processor : ")
                .append(result.getProcessorName())
                .append(", end time : ")
                .append(DateUtil.dateToString(result.getEndTime()))
                .append(", result : ")
                .append(result.isSuccess());
        return builder.toString();
    }

}
