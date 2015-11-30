package reader;

import model.DataItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.omg.CORBA.portable.InputStream;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 30..
 */
public class TestStringStreamReader {
    private DataReader sut;
    private InputStream mockInputStream;

    @Before
    public void setUp() throws Exception {
        mockInputStream = mock(InputStream.class);
        sut = new StringStreamDataReader(mockInputStream);
    }
    @Test
    public void 다음값이_있는지_검사() throws IOException {
        boolean first = sut.hasNext();

        sut.readNext();

        boolean last = sut.hasNext();

        assertThat(first, is(true));
        assertThat(last, is(false));
    }

    @Test
    public void 스트림으로_부터_읽는지_확인() throws Exception {
        DataItem item = sut.readNext();

        assertThat(item, notNullValue());
        verify(mockInputStream, times(1)).read(Mockito.any(byte[].class));
    }

    @Test
    public void 바이트로부터_스트링을_읽는지_확인() throws Exception {
        DataItem item = sut.readNext();

        String data = (String) item.getData();

        assertThat(item, notNullValue());
        assertThat(data, notNullValue());
        assertThat(data, is(""));
    }
    @Test
    public void 한번_되감기() throws IOException {
        DataItem<String> firstItem = sut.readNext();
        sut.rewindOnce();
        DataItem<String> secondItem = sut.readNext();

        assertThat(firstItem.getData(), is(secondItem.getData()));
        assertThat(firstItem.getSeqeunce(), is(secondItem.getSeqeunce()));
        assertThat(firstItem.getData(), is(secondItem.getData()));
    }
    @Test
    public void 처음으로_되감기() throws IOException {
        DataItem<String> firstItem = sut.readNext();
        sut.readNext();

        sut.rewind();

        DataItem<String> secondItem = sut.readNext();

        assertThat(firstItem.getData(), is(secondItem.getData()));
        assertThat(firstItem.getSeqeunce(), is(secondItem.getSeqeunce()));
        assertThat(firstItem.getData(), is(secondItem.getData()));
    }
    @Test(expected = IOException.class)
    public void 끝에_도달한_후_읽기_시도() throws IOException {
        while (sut.hasNext()){
            sut.readNext();
        }

        sut.readNext();
    }
}
