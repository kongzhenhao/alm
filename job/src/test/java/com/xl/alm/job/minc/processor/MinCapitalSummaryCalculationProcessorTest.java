package com.xl.alm.job.minc.processor;

import com.xl.alm.job.minc.task.MinCapitalSummaryCalculationTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 最低资本明细汇总数据计算处理器测试类
 *
 * @author AI Assistant
 */
@RunWith(MockitoJUnitRunner.class)
public class MinCapitalSummaryCalculationProcessorTest {

    @Mock
    private MinCapitalSummaryCalculationTask minCapitalSummaryCalculationTask;

    @InjectMocks
    private MinCapitalSummaryCalculationProcessor processor;

    private TaskContext taskContext;

    @Before
    public void setUp() {
        taskContext = new TaskContext();
    }

    @Test
    public void testProcess_Success() {
        // 准备测试数据
        taskContext.setJobParams("{\"accountPeriod\":\"202306\"}");
        
        // 模拟Task方法调用
        when(minCapitalSummaryCalculationTask.execute("202306")).thenReturn(true);
        
        // 执行测试
        ProcessResult result = processor.process(taskContext);
        
        // 验证结果
        assertTrue("处理应该成功", result.isSuccess());
        assertEquals("最低资本明细汇总数据计算成功", result.getMsg());
        
        // 验证方法调用
        verify(minCapitalSummaryCalculationTask, times(1)).execute("202306");
    }

    @Test
    public void testProcess_TaskFailed() {
        // 准备测试数据
        taskContext.setJobParams("{\"accountPeriod\":\"202306\"}");
        
        // 模拟Task方法调用失败
        when(minCapitalSummaryCalculationTask.execute("202306")).thenReturn(false);
        
        // 执行测试
        ProcessResult result = processor.process(taskContext);
        
        // 验证结果
        assertFalse("处理应该失败", result.isSuccess());
        assertEquals("最低资本明细汇总数据计算失败", result.getMsg());
        
        // 验证方法调用
        verify(minCapitalSummaryCalculationTask, times(1)).execute("202306");
    }

    @Test
    public void testProcess_MissingAccountPeriod() {
        // 准备测试数据 - 缺少accountPeriod参数
        taskContext.setJobParams("{}");
        
        // 执行测试
        ProcessResult result = processor.process(taskContext);
        
        // 验证结果
        assertFalse("处理应该失败", result.isSuccess());
        assertEquals("任务参数错误，缺少accountPeriod参数", result.getMsg());
        
        // 验证没有调用Task方法
        verify(minCapitalSummaryCalculationTask, never()).execute(anyString());
    }

    @Test
    public void testProcess_EmptyAccountPeriod() {
        // 准备测试数据 - accountPeriod为空
        taskContext.setJobParams("{\"accountPeriod\":\"\"}");
        
        // 执行测试
        ProcessResult result = processor.process(taskContext);
        
        // 验证结果
        assertFalse("处理应该失败", result.isSuccess());
        assertEquals("任务参数错误，缺少accountPeriod参数", result.getMsg());
        
        // 验证没有调用Task方法
        verify(minCapitalSummaryCalculationTask, never()).execute(anyString());
    }

    @Test
    public void testProcess_InvalidJson() {
        // 准备测试数据 - 无效的JSON
        taskContext.setJobParams("invalid json");
        
        // 执行测试
        ProcessResult result = processor.process(taskContext);
        
        // 验证结果
        assertFalse("处理应该失败", result.isSuccess());
        assertTrue("错误消息应该包含异常信息", result.getMsg().contains("执行异常"));
        
        // 验证没有调用Task方法
        verify(minCapitalSummaryCalculationTask, never()).execute(anyString());
    }

    @Test
    public void testProcess_TaskException() {
        // 准备测试数据
        taskContext.setJobParams("{\"accountPeriod\":\"202306\"}");
        
        // 模拟Task方法抛出异常
        when(minCapitalSummaryCalculationTask.execute("202306"))
                .thenThrow(new RuntimeException("数据库连接失败"));
        
        // 执行测试
        ProcessResult result = processor.process(taskContext);
        
        // 验证结果
        assertFalse("处理应该失败", result.isSuccess());
        assertTrue("错误消息应该包含异常信息", result.getMsg().contains("执行异常"));
        assertTrue("错误消息应该包含具体异常", result.getMsg().contains("数据库连接失败"));
        
        // 验证方法调用
        verify(minCapitalSummaryCalculationTask, times(1)).execute("202306");
    }
}
