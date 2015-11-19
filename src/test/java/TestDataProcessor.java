import model.DataItem;
import org.junit.Before;
import org.junit.Test;
import processor.DataProcessor;
import processor.ProcessFailException;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ghost9087 on 2015. 11. 18..
 */
public class TestDataProcessor {
    DataProcessor<DataItem> sut;
    List<DataItem> testList;

    @Before
    public void setUp(){
        sut = mock(DataProcessor.class);

    }

    @Test
    public void 아이템_처리_확인() throws ProcessFailException {
        sut.startProcess(testList.get(0));

        DataItem processed = sut.getDataAtIndex(0);

        assertThat(processed, notNullValue());
        assertThat(processed.getData(), notNullValue());
    }

    @Test(expected = ProcessFailException.class)
    public void 잘못된_데이터_처리시_오류확인() throws ProcessFailException {
        DataItem mock = mockData(0);
        when(mock.getData()).thenReturn(null);

        sut.startProcess(mock);
    }

    private DataItem mockData(int seq) {
        DataItem mockItem = mock(DataItem.class);
        when(mockItem.getData()).thenReturn(Math.random()*100);
        when(mockItem.getSeq()).thenReturn(seq);
        when(mockItem.getType()).thenReturn(mockItem.getClass().getName());

        return mockItem;
    }
}
