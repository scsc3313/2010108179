package controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;

/**
 * Created by ghost9087 on 2015. 11. 23..
 */
public class TestDefaultBatchController extends TestBatchController{

    @Override
    protected BatchController getBatchController() {
        return new DefaultBatchController();
    }
}
