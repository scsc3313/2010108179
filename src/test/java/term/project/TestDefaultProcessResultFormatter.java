package term.project;

import model.DataItem;
import model.ProcessResult;
import org.junit.Before;
import org.junit.Test;
import term.project.DefaultProcessResultFormatter;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class TestDefaultProcessResultFormatter {
    private DefaultProcessResultFormatter sut;

    @Before
    public void setUp() throws Exception {
        sut = new DefaultProcessResultFormatter();
    }

    @Test
    public void 로그_포맷이_정상적인지_검사() throws Exception {
        ProcessResult result = new ProcessResult(
                mock(DataItem.class),
                new Date(),
                new Date(),
                true,
                "mockProcessor"
        );

        String logMessage = sut.convert(result);

        assertThat(logMessage, notNullValue());

        assertThat("logMessage was"+logMessage, logMessage.matches("^type : \\w+, seq : \\d+, processor : \\w+, end time : \\d{1,2}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}, result : true|false$"), is(true));
    }
}
