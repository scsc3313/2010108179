package term.project;

import logger.BatchResultFormatter;
import model.BatchResult;
import util.DateUtil;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class DefaultBatchResultFormatter implements BatchResultFormatter {
    @Override
    public String convert(BatchResult result) {
        StringBuilder builder = new StringBuilder();
        builder.append("start time : ")
                .append(DateUtil.dateToString(result.getStartTime()))
                .append(", end time : ")
                .append(DateUtil.dateToString(result.getEndTime()))
                .append(", processed item : ")
                .append(result.getDataProcessed())
                .append(", elapsed time : ")
                .append(DateUtil.elapsedTime(result.getStartTime(), result.getEndTime()))
                .append("ms")
                .append(", result : ")
                .append(result.isSuccess());

        return builder.toString();
    }
}
