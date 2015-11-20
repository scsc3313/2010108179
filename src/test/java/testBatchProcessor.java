import model.DataItem;
import model.BatchProcessResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.portable.InputStream;
import processor.DataProcessor;
import processor.ProcessFailException;
import processor.TypeNotMatchedException;
import reader.DataItemConverter;
import reader.DataReader;
import reader.StreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * Created by ghost9087 on 2015. 11. 6..
 */
public class TestBatchProcessor {
    private DataReader dataReader;
    private DataProcessor dataProcessor;
    private ProcessLogger logger;
    private BatchProcessor sut;


    @Before
    public void setUp(){
        dataReader = mock(DataReader.class);
        dataProcessor = mock(DataProcessor.class);
        sut = new BatchProcessor(dataReader, dataProcessor);
    }
    @Test
    public void 배치를_실행한다(){
        sut.doBatch();
        BatchProcessResult result = sut.getResult();

        assertThat(result.getErrorCode(), is(0));
    }

    @Ignore("Generic Type을 얻어낼 방법을 알아낼 때 까지 무시한다")
    @Test(expected = TypeNotMatchedException.class)
    public void 잘못된_리더와_프로세서_연결(){
        dataReader = new DataReader<DataItem<String>>(mock(StreamReader.class), mock(DataItemConverter.class));
        dataProcessor = mock(DataProcessor.class);
        sut = new BatchProcessor(dataReader, dataProcessor);
    }
}
