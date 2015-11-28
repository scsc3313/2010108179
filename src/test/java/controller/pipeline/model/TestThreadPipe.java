package controller.pipeline.model;

import model.DataItem;
import model.ProcessResult;
import net.jodah.concurrentunit.Waiter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import processor.DataProcessor;
import processor.ProcessFailException;

import java.util.concurrent.TimeoutException;

import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class TestThreadPipe {
    private ThreadPipe sut;
    private PipelineProcessListener mockListener;
    private Waiter waiter;

    @Before
    public void setUp() throws ProcessFailException {
        waiter = new Waiter();

        DataProcessor mockProcessor = mock(DataProcessor.class);
        when(mockProcessor.processItem(Mockito.any(DataItem.class))).thenReturn(mock(ProcessResult.class));

        mockListener = mock(PipelineProcessListener.class);
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                waiter.resume();
                return null;
            }
        }).when(mockListener).onProcess(Mockito.any(ProcessResult.class));

        sut = new ThreadPipe(mockProcessor);
        sut.setListener(mockListener);
    }

    @Test
    public void 항목이_추가되었을때_작동() throws TimeoutException {
        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));

        waiter.await(2000, 5);
        verify(mockListener, times(5)).onProcess(Mockito.any(ProcessResult.class));
    }
    @Test
    public void 항목이_일정_시간_후_추가되었을때() throws InterruptedException, TimeoutException {
        sut.addItem(mock(DataItem.class));
        Thread.sleep(3000);
        sut.addItem(mock(DataItem.class));

        waiter.await(10000, 2);
        verify(mockListener, times(2)).onProcess(Mockito.any(ProcessResult.class));
    }

    @Test
    public void 작업이_중지되는지_검사() throws InterruptedException, TimeoutException {
        sut.addItem(mock(DataItem.class));
        Thread.sleep(1000);
        sut.addItem(mock(DataItem.class));
        Thread.sleep(1000);
        sut.finish();
        Thread.sleep(1000);
        sut.addItem(mock(DataItem.class));

        waiter.await(3000, 2);
        verify(mockListener, times(2)).onProcess(Mockito.any(ProcessResult.class));
    }
}