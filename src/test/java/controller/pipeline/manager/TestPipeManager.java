package controller.pipeline.manager;

import controller.pipeline.model.PipelineProcessListener;
import controller.pipeline.model.Pipe;
import model.DataItem;
import model.ProcessResult;
import net.jodah.concurrentunit.Waiter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class TestPipeManager {
    private PipeManager sut;
    private Waiter waiter;
    private OnPipelineFinish mockListener;

    @Before
    public void setUp(){
        mockListener = mock(OnPipelineFinish.class);

        doAnswer(invocationOnMock -> {
            waiter.resume();
            return null;
        }).when(mockListener).onFinish(Mockito.any(ProcessResult.class));

        sut = new PipeManager(mockListener);
        waiter = new Waiter();
    }
    @Test
    public void 파이프를_등록한다(){
        sut.addPipe(mock(Pipe.class));

        assertThat(sut.getPipeNumber(), is(1));
    }
    @Test
    public void 파이프가_순차적으로_처리되는지_확인() throws TimeoutException {
        TestPipe mock1 = new TestPipe();
        TestPipe mock2 = new TestPipe();
        TestPipe mock3 = new TestPipe();
        TestPipe mock4 = new TestPipe();
        TestPipe mock5 = new TestPipe();
        sut.addPipe(mock1);
        sut.addPipe(mock2);
        sut.addPipe(mock3);
        sut.addPipe(mock4);
        sut.addPipe(mock5);

        sut.addItem(mock(DataItem.class));

        waiter.await(2000, 1);
    }

    @Test
    public void 작업_중지가_되는지_검사() throws Exception {
        Pipe mock1 = mock(Pipe.class);
        Pipe mock2 = mock(Pipe.class);

        sut.addPipe(mock1);
        sut.addPipe(mock2);

        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));
        sut.addItem(mock(DataItem.class));

        sut.stop();

        verify(mock1, times(1)).finish();
        verify(mock2, times(1)).finish();
    }

    private class TestPipe implements Pipe, Runnable{
        private DataItem item;
        private PipelineProcessListener listener;


        //안씀
        public void finish() {}

        public void addItem(DataItem dataItem) {
            this.item = dataItem;
            new Thread(this).start();
        }

        public void setListener(PipelineProcessListener listener) {
            this.listener = listener;
        }

        public void run() {
            ProcessResult result = new ProcessResult(item, new Date(), new Date(), true, getClass().getName());
            listener.onProcess(result);
        }
    }
}
