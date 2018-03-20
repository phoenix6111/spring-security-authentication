package com.phoenix.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockQueue mMockQueue;
    @Autowired
    private DeferredResultHolder mDeferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(()-> {
            while(true) {

                if(StringUtils.isNotBlank(mMockQueue.getCompleteOrder())) {
                    String orderNum = mMockQueue.getCompleteOrder();

                    mLogger.info("返回订单处理结果："+orderNum);
                    //关键就是这句：返回给浏览器订单处理结果
                    mDeferredResultHolder.getMap().get(orderNum).setResult("place order success");

                    mMockQueue.setCompleteOrder(null);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
