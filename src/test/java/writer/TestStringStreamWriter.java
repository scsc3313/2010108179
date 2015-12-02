package writer;

import model.DataItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class TestStringStreamWriter {
    private StringStreamWriter sut;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = mock(OutputStream.class);
        sut = new StringStreamWriter(outputStream);
    }

    @Test
    public void 데이터를_실제로_출력하는지_검사() throws IOException {
        DataItem<String> item = new DataItem<>("test data", 1);

        sut.writeData(item);

        verify(outputStream, times(1)).write(Mockito.any(byte[].class));
    }
    @Test(expected = IOException.class)
    public void 잘못된_데이터를_출력_할때_예외_발생() throws IOException {
        DataItem<String> item = new DataItem<>(null, 0);

        sut.writeData(item);
    }
}
