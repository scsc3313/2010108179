package controller.pipeline;

import controller.BatchController;
import controller.NoProcessorExistException;
import controller.TestBatchController;
import logger.ProcessLogger;
import model.BatchResult;
import model.DataItem;
import model.ProcessResult;
import net.jodah.concurrentunit.Waiter;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import processor.DataProcessor;
import processor.ProcessFailException;
import reader.DataReader;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class TestPipelineBatchController extends TestBatchController {
    private Waiter waiter;

    @Override
    @Before
    public void setUp() {
        sut = getBatchController();
        mockReader = getMockReader();
        mockProcessor = getMockProcessor();
        mockLogger = getMockLogger();
        mockWriter = getMockWriter();

        sut.setDataReader(mockReader);
        sut.addProcessLogger(mockLogger);
        sut.addProcessor(mockProcessor);
        sut.setDataWriter(mockWriter);
        waiter = new Waiter();
    }

    @Override
    protected BatchController getBatchController() {
        return new PipelineBatchController();
    }

    @Override
    public void 리더_설정후_작동_확인() throws NoProcessorExistException, ProcessFailException, IOException{
        sut.setDataReader(mockReader);
        sut.startProcess();

        try {
            waiter.await(3000, 1);
        } catch (TimeoutException e) {
            assertThat(e.getMessage(), false);
        }

        BatchResult result = sut.getResult();
        assertThat(result, notNullValue());
        verify(mockProcessor, times(result.getDataProcessed())).processItem(any(DataItem.class));
        //FIXME : verify만 하면 invocation에러... 뭐가문제지.....
//        verify(mockLogger, times(result.getDataProcessed())).writeLog(Mockito.any(ProcessResult.class));
//        verify(mockLogger, times(1)).writeResult(Mockito.any(BatchResult.class));
        verify(mockWriter, times(result.getDataProcessed())).writeData(any());
    }

    @Override
    public void 실제로_여러_프로세서가_처리하는지_확인() throws NoProcessorExistException, ProcessFailException, IOException {
        //Given
        DataProcessor mockProcessor2 = getMockProcessor();
        sut.addProcessor(mockProcessor2);

        //When
        sut.startProcess();

        try {
            waiter.await(2000, 1);
        } catch (TimeoutException e) {
            assertThat(e.getMessage(), false);
        }

        BatchResult result = sut.getResult();
        List<DataItem> itemList = sut.getItemList();

        //Then
        assertThat(result.isSuccess(), is(true));
        assertThat(itemList.size(), is(result.getDataProcessed()));
        verify(mockProcessor, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockProcessor2, times(result.getDataProcessed())).processItem(any(DataItem.class));
        verify(mockWriter, times(result.getDataProcessed())).writeData(any());
    }

    @Override
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

        try {
            waiter.await(2000, 1);
        } catch (TimeoutException e) {
            assertThat(e.getMessage(), false);
        }

        BatchResult result = sut.getResult();
        List itemList = sut.getItemList();

        System.out.println(itemList);
        assertThat(result.isSuccess(), is(false));
    }

    @Override
    public void 프로세서가_없을때_리더_추가_작동() throws NoProcessorExistException, IOException {
        sut = new PipelineBatchController();
        sut.setDataReader(getMockReader());
        sut.addProcessLogger(getMockLogger());

        sut.startProcess();
    }

    @Override
    protected ProcessLogger getMockLogger() {
        ProcessLogger mock = super.getMockLogger();

        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                waiter.resume();
                return null;
            }
        }).when(mock).writeResult(Mockito.any(BatchResult.class));

        return mock;
    }
}
