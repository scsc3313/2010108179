package writer;

import model.DataItem;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class StringStreamWriter implements DataWriter<String> {
    private OutputStream outputStream;

    public StringStreamWriter(OutputStream outputStream) {

        this.outputStream = outputStream;
    }

    @Override
    public void writeData(DataItem<String> data) throws IOException {
        if (data != null && data.getData() != null && !data.getData().isEmpty()){
            String stringData = data.getData();

            byte[] rawData = stringData.getBytes();

            outputStream.write(rawData);
            outputStream.write("\n".getBytes());
        }
        else
            throw new IOException("Attempt to write invalid data");

    }
}
