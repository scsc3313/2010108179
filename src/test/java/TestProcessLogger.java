import logger.BatchResultFormatter;
import logger.ProcessLogger;
import logger.ProcessResultFormatter;
import model.BatchResult;
import model.DataItem;
import model.ProcessResult;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 27..
 */
public class TestProcessLogger {
    private ProcessLogger sut;
    private ProcessResultFormatter mockProcessResultFormatter;
    private BatchResultFormatter mockBatchResultFormatter;

    @Before
    public void setUp(){
        mockProcessResultFormatter = mock(ProcessResultFormatter.class);
        mockBatchResultFormatter = mock(BatchResultFormatter.class);
        sut = new ProcessLogger(mockProcessResultFormatter, mockBatchResultFormatter) {
            @Override
            protected void writeMesaage(String message) {}
        };
    }
    @Test
    public void 프로세스_로그가_출력되는지_검사(){
        ProcessResult mockResult = mock(ProcessResult.class);
        when(mockResult.getData()).thenReturn(mock(DataItem.class));

        sut.writeLog(mockResult);

        verify(mockProcessResultFormatter, times(1)).convert(mockResult);
    }
    @Test
    public void 배치_결과_로그가_출력되는지_검사(){
        BatchResult result = mock(BatchResult.class);

        sut.writeResult(result);

        verify(mockBatchResultFormatter, times(1)).convert(result);
    }
}
