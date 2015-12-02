package config;

import controller.BatchController;
import controller.pipeline.PipelineBatchController;
import logger.ProcessLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import processor.DataProcessor;
import reader.DataReader;
import writer.DataWriter;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
@Configuration
@Import(ProcessorConfig.class)
public class ControllerConfig {
    @Autowired
    DataProcessor<String> dataProcessor;
    @Autowired
    DataReader<String> dataReader;
    @Autowired
    ProcessLogger<String> processLogger;
    @Autowired
    DataWriter<String> dataWriter;

    @Bean
    public BatchController<String> batchController(){
        BatchController<String> controller= new PipelineBatchController<>();

        controller.addProcessor(dataProcessor);
        controller.addProcessLogger(processLogger);
        controller.setDataReader(dataReader);
        controller.setDataWriter(dataWriter);

        return controller;
    }
}
