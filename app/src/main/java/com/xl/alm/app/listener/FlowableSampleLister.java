package com.xl.alm.app.listener;


import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 执行监听器：分录审批结束监听
 * @author: zhoushengbo
 * @create: 2023-05-19 13:12
 **/
@Component
@Slf4j
public class FlowableSampleLister implements ExecutionListener {
    @Override
    public void notify(DelegateExecution delegateExecution) {

    }

}
