package logger;

import model.DataItem;
import model.ProcessResult;

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
                .append(dateToString(result.getEndTime()))
                .append(", result : ")
                .append(result.isSuccess());
        return builder.toString();
    }

    public String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd hh:mm:ss");

        return formatter.format(date);
    }
}
