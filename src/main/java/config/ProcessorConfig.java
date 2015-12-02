package config;

import logger.BatchResultFormatter;
import logger.ConsoleProcessLogger;
import logger.ProcessLogger;
import logger.ProcessResultFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import processor.DataProcessor;
import processor.StringLineSortProcessor;
import reader.DataReader;
import reader.StringStreamDataReader;
import writer.DataWriter;
import writer.StringStreamWriter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
@Configurable
@Import({IOConfig.class, LogConfig.class})
public class ProcessorConfig {
    @Autowired
    InputStream dataInputStream;
    @Autowired
    OutputStream dataOutputStream;
    @Autowired
    ProcessResultFormatter<String> processResultFormatter;
    @Autowired
    BatchResultFormatter batchResultFormatter;

    @Bean
    public DataProcessor<String> dataProcessor(){
        return new StringLineSortProcessor();
    }
    @Bean
    public DataReader<String> dataReader(){
        return new StringStreamDataReader(dataInputStream);
    }
    @Bean
    public ProcessLogger<String> processLogger(){
        return new ConsoleProcessLogger(processResultFormatter, batchResultFormatter);
    }
    @Bean
    public DataWriter<String> dataWriter(){
        return new StringStreamWriter(dataOutputStream);
    }
}
