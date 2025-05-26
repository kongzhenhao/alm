package com.xl.alm.job.minc.service;

import com.xl.alm.job.minc.entity.MinCapitalSummaryCalculationEntity;
import com.xl.alm.job.minc.mapper.MinCapitalSummaryCalculationMapper;
import com.xl.alm.job.minc.service.impl.MinCapitalSummaryCalculationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 最低资本明细汇总数据计算服务测试类
 *
 * @author AI Assistant
 */
@RunWith(MockitoJUnitRunner.class)
public class MinCapitalSummaryCalculationServiceTest {

    @Mock
    private MinCapitalSummaryCalculationMapper minCapitalSummaryCalculationMapper;

    @InjectMocks
    private MinCapitalSummaryCalculationServiceImpl minCapitalSummaryCalculationService;

    private List<MinCapitalSummaryCalculationEntity> mockSummaryData;

    @Before
    public void setUp() {
        // 准备测试数据
        mockSummaryData = new ArrayList<>();
        
        MinCapitalSummaryCalculationEntity entity1 = new MinCapitalSummaryCalculationEntity();
        entity1.setAccountingPeriod("202306");
        entity1.setItemCode("MR001");
        entity1.setAccountCode("AC001");
        entity1.setAmount(new BigDecimal("1000000.00"));
        entity1.setIsDel(0);
        entity1.setCreateBy("system");
        entity1.setCreateTime(new Date());
        entity1.setUpdateBy("system");
        entity1.setUpdateTime(new Date());
        
        MinCapitalSummaryCalculationEntity entity2 = new MinCapitalSummaryCalculationEntity();
        entity2.setAccountingPeriod("202306");
        entity2.setItemCode("MR001");
        entity2.setAccountCode("AC002");
        entity2.setAmount(new BigDecimal("2000000.00"));
        entity2.setIsDel(0);
        entity2.setCreateBy("system");
        entity2.setCreateTime(new Date());
        entity2.setUpdateBy("system");
        entity2.setUpdateTime(new Date());
        
        mockSummaryData.add(entity1);
        mockSummaryData.add(entity2);
    }

    @Test
    public void testCalculateMinCapitalSummary_Success() {
        // 准备测试数据
        String accountPeriod = "202306";
        
        // 模拟Mapper方法调用
        when(minCapitalSummaryCalculationMapper.selectSummaryFromDeptMincapDetail(accountPeriod))
                .thenReturn(mockSummaryData);
        when(minCapitalSummaryCalculationMapper.deleteMinCapitalSummaryByPeriod(accountPeriod))
                .thenReturn(5);
        when(minCapitalSummaryCalculationMapper.batchInsertMinCapitalSummary(anyList()))
                .thenReturn(2);
        
        // 执行测试
        boolean result = minCapitalSummaryCalculationService.calculateMinCapitalSummary(accountPeriod);
        
        // 验证结果
        assertTrue("计算应该成功", result);
        
        // 验证方法调用
        verify(minCapitalSummaryCalculationMapper, times(1))
                .selectSummaryFromDeptMincapDetail(accountPeriod);
        verify(minCapitalSummaryCalculationMapper, times(1))
                .deleteMinCapitalSummaryByPeriod(accountPeriod);
        verify(minCapitalSummaryCalculationMapper, times(1))
                .batchInsertMinCapitalSummary(anyList());
    }

    @Test
    public void testCalculateMinCapitalSummary_EmptyAccountPeriod() {
        // 测试空账期
        boolean result = minCapitalSummaryCalculationService.calculateMinCapitalSummary("");
        assertFalse("空账期应该返回false", result);
        
        // 测试null账期
        result = minCapitalSummaryCalculationService.calculateMinCapitalSummary(null);
        assertFalse("null账期应该返回false", result);
    }

    @Test
    public void testCalculateMinCapitalSummary_InvalidAccountPeriodFormat() {
        // 测试错误的账期格式
        boolean result = minCapitalSummaryCalculationService.calculateMinCapitalSummary("2023");
        assertFalse("错误的账期格式应该返回false", result);
    }

    @Test
    public void testCalculateMinCapitalSummary_NoData() {
        // 准备测试数据
        String accountPeriod = "202306";
        
        // 模拟没有数据的情况
        when(minCapitalSummaryCalculationMapper.selectSummaryFromDeptMincapDetail(accountPeriod))
                .thenReturn(new ArrayList<>());
        
        // 执行测试
        boolean result = minCapitalSummaryCalculationService.calculateMinCapitalSummary(accountPeriod);
        
        // 验证结果
        assertTrue("没有数据不算失败", result);
        
        // 验证只调用了查询方法，没有调用删除和插入方法
        verify(minCapitalSummaryCalculationMapper, times(1))
                .selectSummaryFromDeptMincapDetail(accountPeriod);
        verify(minCapitalSummaryCalculationMapper, never())
                .deleteMinCapitalSummaryByPeriod(anyString());
        verify(minCapitalSummaryCalculationMapper, never())
                .batchInsertMinCapitalSummary(anyList());
    }
}
