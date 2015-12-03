import config.ControllerConfig;
import controller.BatchController;
import controller.NoProcessorExistException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by ghost9087 on 2015. 12. 2..
 */
public class StringSortBatchProcess {
    private AnnotationConfigApplicationContext context;
    public static void main(String args[]) throws IOException, NoProcessorExistException {
        StringSortBatchProcess process = new StringSortBatchProcess();
        process.init();
        process.start();
    }

    BatchController<String> controller;

    private void start() throws IOException, NoProcessorExistException {
        controller.startProcess();
    }

    private void init() {
        context = new AnnotationConfigApplicationContext(ControllerConfig.class);
        controller = context.getBean(BatchController.class);
    }
}
