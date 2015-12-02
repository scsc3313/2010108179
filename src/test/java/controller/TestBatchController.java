package controller;

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
import writer.DataWriter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public abstract class TestBatchController {
    protected BatchController sut;
    protected DataReader mockReader;
    protected DataProcessor mockProcessor;
    protected ProcessLogger mockLogger;
    protected DataWriter mockWriter;

    @Before
    public void setUp() throws IOException {
        sut = getBatchController();
        mockReader = getMockReader();
        mockProcessor = getMockProcessor();
        mockLogger = getMockLogger();
        mockWriter = getMockWriter();

        sut.setDataReader(mockReader);
        sut.addProcessLogger(mockLogger);
        sut.addProcessor(mockProcessor);
        sut.setDataWriter(mockWriter);
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
    public void 프로세서가_없을때_리더_추가_작동() throws NoProcessorExistException, IOException {
        sut = new DefaultBatchController();

        DataReader mockReader = mock(DataReader.class);
        sut.setDataReader(mockReader);

        sut.startProcess();
    }
    @Test
    public void 리더_설정후_작동_확인() throws NoProcessorExistException, ProcessFailException, IOException {
        sut.setDataReader(mock(DataReader.class));
        sut.startProcess();

        BatchResult result = sut.getResult();
        assertThat(result, notNullValue());
        verify(mockProcessor, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockLogger, times(result.getDataProcessed())).writeLog(any(ProcessResult.class));
        verify(mockLogger, times(1)).writeResult(any(BatchResult.class));
        verify(mockWriter, times(result.getDataProcessed())).writeData(any());
    }
    @Test
    public void 실제로_여러_프로세서가_처리하는지_확인() throws NoProcessorExistException, ProcessFailException, IOException {
        //Given
        DataProcessor mockProcessor2 = getMockProcessor();
        sut.addProcessor(mockProcessor2);

        //When
        sut.startProcess();
        BatchResult result = sut.getResult();
        List<DataItem> itemList = sut.getItemList();

        //Then
        assertThat(result.isSuccess(), is(true));
        assertThat(itemList.size(), is(result.getDataProcessed()));
        verify(mockProcessor, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockProcessor2, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockWriter, times(result.getDataProcessed())).writeData(any());
    }
    @Test
    public void 처리_실패시_배치_처리가_중지되는지_확인() throws IOException, NoProcessorExistException, ProcessFailException {
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                DataItem item = (DataItem) invocationOnMock.getArguments()[0];
                System.out.println("처리중"+item);
                Integer data = (Integer) item.getData();
                if (data == 5 && item.getSeqeunce() ==4){
                    ProcessResult result = mock(ProcessResult.class);
                    throw new ProcessFailException(result);
                }

                return new ProcessResult(mock(DataItem.class), new Date(), new Date(),true, "mockProcessor");
            }
        }).when(mockProcessor).processItem(Mockito.any(DataItem.class));

        sut.startProcess();

        BatchResult result = sut.getResult();
        List itemList = sut.getItemList();

        System.out.println(itemList);
        assertThat(result.isSuccess(), is(false));
    }
    protected DataReader getMockReader() throws IOException {
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
    protected DataProcessor getMockProcessor(){
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
    protected ProcessLogger getMockLogger(){
        ProcessLogger mockLogger = mock(ProcessLogger.class);

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                ProcessResult result = (ProcessResult) invocationOnMock.getArguments()[0];

                StringBuilder builder = new StringBuilder();
                builder.append("log = ")
                        .append("data:")
                        .append(result.getData())
                        .append("sequence:")
                        .append(result.getProcessorName());


                System.out.println(builder.toString());

                return null;
            }
        }).when(mockLogger).writeLog(Mockito.any(ProcessResult.class));

        return mockLogger;
    }
    protected DataWriter getMockWriter(){
        DataWriter mockWriter = mock(DataWriter.class);

        try {
            doAnswer(new Answer() {
                public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                    Object o = invocationOnMock.getArguments()[0];

                    StringBuilder builder = new StringBuilder();
                    builder.append("write = ")
                            .append("data:")
                            .append(mock(Object.class));

                    System.out.println(builder.toString());

                    return null;
                }
            }).when(mockWriter).writeData(Mockito.anyObject());
        } catch (IOException e) {
            return null;
        }
        return mockWriter;
    }
    abstract protected BatchController getBatchController();
}
