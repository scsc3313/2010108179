package term.project;

import logger.BatchResultFormatter;
import logger.ProcessLogger;
import logger.ProcessResultFormatter;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class ConsoleProcessLogger extends ProcessLogger<String> {
    public ConsoleProcessLogger(ProcessResultFormatter processResultFormatter, BatchResultFormatter batchResultFormatter) {
        super(processResultFormatter, batchResultFormatter);
    }

    @Override
    protected void writeMesaage(String message) {
        System.out.println(message);
    }
}
