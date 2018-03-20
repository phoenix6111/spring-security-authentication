package com.phoenix.web.async;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class AsyncController {

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockQueue mMockQueue;

    @Autowired
    private DeferredResultHolder mDeferredResultHolder;

    @RequestMapping("/order")
    public String order() throws Exception {

        mLogger.info("主线程开始");
        Thread.sleep(1000);
        mLogger.info("主线程返回");

        return "success";
    }

    @RequestMapping("/async_order")
    public DeferredResult<String> asyncOrder() throws Exception {
        mLogger.info("主线程开始");

        String orderNum = RandomStringUtils.randomNumeric(8);
        mMockQueue.setPlaceOrder(orderNum);

        DeferredResult<String> result = new DeferredResult<>();
        mDeferredResultHolder.getMap().put(orderNum,result);

        /*Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                mLogger.info("副线程开始");

                Thread.sleep(1000);

                mLogger.info("副线程开始");

                return "success";
            }
        };*/

        mLogger.info("主线程返回");

        return result;
    }

}
