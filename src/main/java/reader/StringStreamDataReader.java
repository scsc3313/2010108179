package reader;

import model.DataItem;

import java.io.*;

/**
 * Created by ghost9087 on 2015. 11. 30..
 */
public class StringStreamDataReader implements DataReader<String> {
    private int sequence;
    private final InputStream inputStream;
    private int positionInByte;
    private int lastReadByte;

    public StringStreamDataReader(InputStream inputStream) {
        this.inputStream = inputStream;
        sequence = 0;
        positionInByte = 0;
        lastReadByte = 0;
    }

    public DataItem<String> readNext() throws IOException {
        int read;
        if (!hasNext())
            throw new IOException("input stream has reached end");

        positionInByte += lastReadByte;

        lastReadByte = 0;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while ((read = inputStream.read()) != '\n' && hasNext()){
            byteArrayOutputStream.write(read);
            lastReadByte++;
        }
        lastReadByte++;

        String stringFromStream = byteArrayOutputStream.toString("UTF-8");

        return new DataItem<String>(stringFromStream, sequence++);
    }

    public boolean hasNext() throws IOException {
        int availableByte = inputStream.available();
        return availableByte > 1;
    }

    public void rewindOnce() throws IOException {
        int skipByte = positionInByte;
        inputStream.reset();
        sequence--;
        long skipedByte = inputStream.skip(skipByte);
        if (skipedByte != positionInByte)
            throw new IOException("fail to reset stream pointer");
    }

    public void rewind() throws IOException {
        inputStream.reset();
        positionInByte = 0;
        lastReadByte = 0;
        sequence = 0;
    }
}
