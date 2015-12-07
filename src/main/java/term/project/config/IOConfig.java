package term.project.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import java.io.*;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
@Configurable
public class IOConfig {
    @Bean
    public InputStream dataInputStream() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("input.txt");
        return inputStream;
    }
    @Bean
    public OutputStream dataOutputStream() throws FileNotFoundException {
        OutputStream outputStream = new FileOutputStream("output.txt");
        return outputStream;
    }
}
