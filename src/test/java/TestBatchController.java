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
import processor.ProcessFailException;
import reader.DataReader;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
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
    public void 프로세서가_없을때_리더_추가_작동() throws NoProcessorExistException {
        DataReader mockReader = mock(DataReader.class);

        sut.setDataReader(mockReader);
        sut.startProcess();
    }
    @Test
    public void 리더_설정후_작동_확인() throws NoProcessorExistException, ProcessFailException {
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
        verify(mockLogger, times(1)).writeResult(any(BatchResult.class));
    }
    @Test
    public void 실제로_여러_프로세서가_처리하는지_확인() throws NoProcessorExistException, ProcessFailException {
        sut.setDataReader(mockReader);
        sut.addProcessLogger(mockLogger);

        DataProcessor mockProcessor1 = getMockProcessor();
        DataProcessor mockProcessor2 = getMockProcessor();

        sut.addProcessor(mockProcessor1);
        sut.addProcessor(mockProcessor2);

        sut.startProcess();
        BatchResult result = sut.getResult();

        assertThat(result.isSuccess(), is(true));
        verify(mockProcessor1, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockProcessor2, times(result.getDataProcessed())).processItem(any(DataItem.class));
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

        try {
            when(mockProcessor.processItem(Mockito.any(DataItem.class))).thenReturn(
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor"),
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor"),
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor"),
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor"),
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor"),
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor"),
                    new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor")
            );
        } catch (ProcessFailException ignore) {}

        return mockProcessor;
    }
    private ProcessLogger getMockLogger(){
        ProcessLogger mockLogger = mock(ProcessLogger.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ProcessResult result = (ProcessResult) invocationOnMock.getArguments()[0];
                System.out.println("data:"+result.getData() + "sequence:"+result.getProcessorName());

                return null;
            }
        }).when(mockLogger).writeLog(Mockito.any(ProcessResult.class));

        return mockLogger;
    }
}
