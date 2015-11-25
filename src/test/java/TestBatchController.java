import controller.BatchController;
import controller.NoProcessorExistException;
import logger.ProcessLogger;
import model.BatchResult;
import model.DataItem;
import model.ProcessResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import processor.DataProcessor;
import reader.DataReader;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class TestBatchController {
    private BatchController sut;
    private DataReader mockReader;
    private DataProcessor mockProcessor;
    private ProcessLogger mockLogger;

    @Before
    public void setUp(){
        sut = new BatchController();
        mockReader = getMockReader();
        mockProcessor = getMockProcessor();
        mockLogger = getMockLogger();
    }
    @Test(expected = IllegalArgumentException.class)
    public void 잘못된_리더_설정(){
        DataReader wrongReader = null;

        sut.setDataReader(wrongReader);
    }
    @Test(expected = IllegalArgumentException.class)
    public void 잘못된_프로세서_추가(){
        DataProcessor wrongProcessor = null;

        sut.addProcessor(wrongProcessor);
    }
    @Test(expected = IllegalArgumentException.class)
    public void 잘못된_로거_설정(){
        ProcessLogger wrongLogger = null;

        sut.addProcessLogger(wrongLogger);
    }
    @Test(expected = NoProcessorExistException.class)
    public void 프로세서가_없을때_리더_추가_작동(){
        DataReader mockReader = mock(DataReader.class);

        sut.setDataReader(mockReader);
        sut.startProcess();
    }
    @Test
    public void 리더_설정후_작동_확인(){
        //Given
        sut.setDataReader(mockReader);
        sut.addProcessor(mockProcessor);
        sut.addProcessLogger(mockLogger);

        //When
        sut.setDataReader(mock(DataReader.class));
        sut.startProcess();

        //Then
        BatchResult result = sut.getResult();
        assertThat(result, notNullValue());
        verify(mockProcessor, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockLogger, times(result.getDataProcessed())).writeLog(any(ProcessResult.class));
    }
    private DataReader getMockReader(){
        DataReader mock = mock(DataReader.class);

        when(mock.hasNext()).thenReturn(
                true, true, true, true,
                true, true, true, false
        );

        when(mock.readNext()).thenReturn(
                new DataItem(1, 0),
                new DataItem(2, 1),
                new DataItem(3, 2),
                new DataItem(4, 3),
                new DataItem(5, 4),
                new DataItem(6, 5),
                new DataItem(7, 6));

        return mock;
    }
    private DataProcessor getMockProcessor(){
        DataProcessor mockProcessor = mock(DataProcessor.class);

        when(mockProcessor.processItem(Mockito.any(DataItem.class))).thenReturn(
                new ProcessResult(),
                new ProcessResult(),
                new ProcessResult(),
                new ProcessResult(),
                new ProcessResult(),
                new ProcessResult(),
                new ProcessResult()
        );

        return mockProcessor;
    }
    private ProcessLogger getMockLogger(){
        ProcessLogger mockLogger = mock(ProcessLogger.class);

        ProcessResult anyResult = Mockito.any(ProcessResult.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                System.out.println(arguments);
                return null;
            }
        }).when(mockLogger).writeLog(anyResult);

        return mockLogger;
    }
}
