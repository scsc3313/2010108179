package reader;

import model.DataItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by ghost9087 on 2015. 11. 30..
 */
public class StringStreamDataReader implements DataReader<String> {
    private int sequence;
    private final InputStream inputStream;

    public StringStreamDataReader(InputStream inputStream) {
        this.inputStream = inputStream;
        sequence = 0;

    }

    public DataItem<String> readNext() throws IOException {

    }

    public boolean hasNext() {

    }

    public void rewindOnce() throws IOException {
        rewind();
        //FIXME
    }

    public void rewind() throws IOException {
        inputStream.reset();
    }
}
