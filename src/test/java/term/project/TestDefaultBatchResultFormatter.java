package term.project;

import model.BatchResult;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class TestDefaultBatchResultFormatter {
    @Test
    public void 로그_포맷이_정상적인지_검사() throws Exception {
        BatchResult result = new BatchResult(12, new Date(), new Date(), true);
        DefaultBatchResultFormatter sut = new DefaultBatchResultFormatter();

        String logMessage = sut.convert(result);

        assertThat(logMessage, notNullValue());
        assertThat("Wrong logMessage: "+logMessage,
                logMessage.matches("^start time : \\d{1,2}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}, end time : \\d{1,2}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}, processed item : \\d+, elapsed time : \\d+ms, result : true|false"),
                is(true)
        );
    }
}
