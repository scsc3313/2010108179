package config;

import logger.BatchResultFormatter;
import logger.DefaultBatchResultFormatter;
import logger.DefaultProcessResultFormatter;
import logger.ProcessResultFormatter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
@Configurable
public class LogConfig {
    @Bean
    public ProcessResultFormatter<String> processResultFormatter(){
        return new DefaultProcessResultFormatter<>();
    }
    @Bean
    public BatchResultFormatter batchResultFormatter(){
        return new DefaultBatchResultFormatter();
    }
}
