package term.project;

import model.DataItem;
import org.junit.Before;
import org.junit.Test;
import reader.DataReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;

/**
 * Created by ghost9087 on 2015. 11. 30..
 */
public class TestStringStreamReader {
    private DataReader sut;
    private InputStream mockInputStream;

    @Before
    public void setUp() throws Exception {
        mockInputStream = getMockInputStream();
        sut = new StringStreamDataReader(mockInputStream);
    }
    @Test
    public void 다음값이_있는지_검사() throws IOException {
        boolean first = sut.hasNext();

        sut.readNext();

        boolean middle = sut.hasNext();

        sut.readNext();

        boolean last = sut.hasNext();

        assertThat(first, is(true));
        assertThat(middle, is(true));
        assertThat(last, is(false));
    }

    @Test
    public void 스트림으로_부터_읽는지_확인() throws Exception {
        DataItem item = sut.readNext();

        byte[] bytes = ((String) item.getData()).getBytes();


        assertThat(item, notNullValue());
        verify(mockInputStream, times(bytes.length+1)).read();
    }

    @Test
    public void 바이트로부터_스트링을_읽는지_확인() throws Exception {
        DataItem item = sut.readNext();

        String data = (String) item.getData();

        assertThat(item, notNullValue());
        assertThat(data, notNullValue());
        assertThat(data, is("테스트1"));
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
    public InputStream getMockInputStream() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("테스트1".getBytes());
        byteArrayOutputStream.write("\n".getBytes());
        byteArrayOutputStream.write("테스트2".getBytes());

        final byte[] stringBytes = byteArrayOutputStream.toByteArray();


        InputStream inputStream = new InputStream() {
            byte[] byteArray = stringBytes;
            int currentPosition = 0;

            @Override
            public int read() throws IOException {
                if (available() == 0)
                    throw new IOException();
                return byteArray[currentPosition++];
            }

            @Override
            public long skip(long n) throws IOException {
                return super.skip(n);
            }

            @Override
            public int available() throws IOException {
                System.out.println("available : "+(byteArray.length-currentPosition));
                return byteArray.length-currentPosition-1;
            }

            @Override
            public synchronized void reset() throws IOException {
                currentPosition = 0;
            }
        };
        InputStream spy = spy(inputStream);

        return spy;
    }
}
