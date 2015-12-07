package controller.pipeline;

import controller.BatchController;
import controller.TestBatchController;

/**
 * Created by ghost9087 on 2015. 11. 28..
 */
public class TestPipelineBatchController extends TestBatchController {

    @Override
    protected BatchController getBatchController() {
        return new PipelineBatchController();
    }
}
