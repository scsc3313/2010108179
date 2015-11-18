import model.DataItem;
import org.junit.Before;
import org.junit.Test;
import reader.DataReader;

import java.io.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ghost9087 on 2015. 11. 18..
 */
public class TestDataReader {
    private DataReader<DataItem> sut;

    @Before
    public void setUp() throws IOException {
        InputStream mockStream = new ByteArrayInputStream(new byte[]{0, 0, 0, -1});
        sut = new DataReader<DataItem>(mockStream) {
            @Override
            protected DataItem readFromStream() throws IOException {
                if (inputStream.read() == -1) {
                    return null;
                }
                return mock(DataItem.class);
            }
        };
    }

    @Test
    public void 데이터_읽기가_정상적인지_검사() throws IOException {
        DataItem testData = sut.readNext();

        assertThat(testData, notNullValue());
    }

    @Test
    public void 데이터를_끝까지_읽는지_검사() throws IOException {
        List<DataItem> testDataList;

        testDataList = sut.readFully();

        assertThat(testDataList.size(), is(4));
    }

}
