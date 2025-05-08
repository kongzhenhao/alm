package com.xl.alm.app.service;

import com.xl.alm.app.dto.SubAccountLiabilityDurationDTO;
import com.xl.alm.app.query.SubAccountLiabilityDurationQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 分账户负债久期汇总表 Service 测试类
 *
 * @author AI Assistant
 */
@SpringBootTest
public class SubAccountLiabilityDurationServiceTest {

    @Autowired
    private SubAccountLiabilityDurationService subAccountLiabilityDurationService;

    /**
     * 测试新增和查询
     */
    @Test
    @Transactional
    public void testInsertAndSelect() {
        // 创建测试数据
        SubAccountLiabilityDurationDTO dto = createTestDTO();
        
        // 新增
        int result = subAccountLiabilityDurationService.insertSubAccountLiabilityDurationDto(dto);
        assertTrue(result > 0);
        assertNotNull(dto.getId());
        
        // 查询
        SubAccountLiabilityDurationDTO queryResult = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoById(dto.getId());
        assertNotNull(queryResult);
        assertEquals(dto.getAccountPeriod(), queryResult.getAccountPeriod());
        assertEquals(dto.getCashFlowType(), queryResult.getCashFlowType());
        assertEquals(dto.getDurationType(), queryResult.getDurationType());
        assertEquals(dto.getDesignType(), queryResult.getDesignType());
        assertEquals(dto.getDurationValSet(), queryResult.getDurationValSet());
    }

    /**
     * 测试条件查询
     */
    @Test
    @Transactional
    public void testSelectByCondition() {
        // 创建测试数据
        SubAccountLiabilityDurationDTO dto = createTestDTO();
        
        // 新增
        int result = subAccountLiabilityDurationService.insertSubAccountLiabilityDurationDto(dto);
        assertTrue(result > 0);
        
        // 条件查询
        SubAccountLiabilityDurationDTO queryResult = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoByCondition(
                dto.getAccountPeriod(), dto.getCashFlowType(), dto.getDurationType(), dto.getDesignType());
        assertNotNull(queryResult);
        assertEquals(dto.getAccountPeriod(), queryResult.getAccountPeriod());
    }

    /**
     * 测试更新
     */
    @Test
    @Transactional
    public void testUpdate() {
        // 创建测试数据
        SubAccountLiabilityDurationDTO dto = createTestDTO();
        
        // 新增
        int result = subAccountLiabilityDurationService.insertSubAccountLiabilityDurationDto(dto);
        assertTrue(result > 0);
        
        // 修改
        String newDesignType = "新设计类型";
        dto.setDesignType(newDesignType);
        result = subAccountLiabilityDurationService.updateSubAccountLiabilityDurationDto(dto);
        assertTrue(result > 0);
        
        // 查询验证
        SubAccountLiabilityDurationDTO queryResult = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoById(dto.getId());
        assertNotNull(queryResult);
        assertEquals(newDesignType, queryResult.getDesignType());
    }

    /**
     * 测试删除
     */
    @Test
    @Transactional
    public void testDelete() {
        // 创建测试数据
        SubAccountLiabilityDurationDTO dto = createTestDTO();
        
        // 新增
        int result = subAccountLiabilityDurationService.insertSubAccountLiabilityDurationDto(dto);
        assertTrue(result > 0);
        
        // 删除
        result = subAccountLiabilityDurationService.deleteSubAccountLiabilityDurationDtoById(dto.getId());
        assertTrue(result > 0);
        
        // 查询验证
        SubAccountLiabilityDurationDTO queryResult = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoById(dto.getId());
        assertNull(queryResult);
    }

    /**
     * 测试批量新增
     */
    @Test
    @Transactional
    public void testBatchInsert() {
        // 创建测试数据
        List<SubAccountLiabilityDurationDTO> dtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SubAccountLiabilityDurationDTO dto = createTestDTO();
            dto.setAccountPeriod("20230" + (i + 1));
            dtoList.add(dto);
        }
        
        // 批量新增
        int result = subAccountLiabilityDurationService.batchInsertSubAccountLiabilityDurationDto(dtoList);
        assertTrue(result > 0);
        
        // 查询验证
        SubAccountLiabilityDurationQuery query = new SubAccountLiabilityDurationQuery();
        query.setAccountPeriod("202301");
        List<SubAccountLiabilityDurationDTO> queryResult = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoList(query);
        assertFalse(queryResult.isEmpty());
    }

    /**
     * 创建测试DTO
     */
    private SubAccountLiabilityDurationDTO createTestDTO() {
        SubAccountLiabilityDurationDTO dto = new SubAccountLiabilityDurationDTO();
        dto.setAccountPeriod("202301");
        dto.setCashFlowType("01");
        dto.setDurationType("01");
        dto.setDesignType("测试设计类型");
        dto.setDurationValSet("{\"1\":{\"date\":\"2025-01-01\",\"val\":0.25},\"2\":{\"date\":\"2025-01-02\",\"val\":0.35}}");
        dto.setCreateBy("test");
        dto.setUpdateBy("test");
        return dto;
    }
}
