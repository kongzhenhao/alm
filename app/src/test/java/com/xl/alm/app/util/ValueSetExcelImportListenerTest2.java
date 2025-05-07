package com.xl.alm.app.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.app.dto.LiabilityCashFlowDTO;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValueSetExcelImportListener 测试类 - 增强版
 * 测试从Excel导入数据并转换为JSON值集
 */
public class ValueSetExcelImportListenerTest2 {

    @Test
    public void testImportExcel() throws Exception {
        // 创建监听器
        ValueSetExcelImportListener<LiabilityCashFlowDTO> listener = 
                new ValueSetExcelImportListener<>(LiabilityCashFlowDTO.class, "cashValSet");
        
        // 加载测试Excel文件
        File file = ResourceUtils.getFile("classpath:test-data/liability_cash_flow_template.xlsx");
        
        // 读取Excel文件
        EasyExcel.read(file.getPath())
                .registerReadListener(listener)
                .sheet()
                .doRead();
        
        // 获取处理后的数据列表
        List<LiabilityCashFlowDTO> resultList = listener.getResultList();
        
        // 验证结果
        assertNotNull(resultList);
        assertFalse(resultList.isEmpty());
        
        // 验证第一条数据
        LiabilityCashFlowDTO dto = resultList.get(0);
        assertNotNull(dto);
        assertNotNull(dto.getCashValSet());
        
        // 验证cashValSet字段的JSON格式
        JSONObject cashValSet = JSON.parseObject(dto.getCashValSet());
        assertNotNull(cashValSet);
        
        // 验证JSON中包含从0到1272的所有索引
        for (int i = 0; i < 1273; i++) {
            String key = String.valueOf(i);
            assertTrue(cashValSet.containsKey(key), "JSON应包含索引" + i);
            
            JSONObject item = cashValSet.getJSONObject(key);
            assertNotNull(item, "索引" + i + "的值不应为null");
            assertNotNull(item.getString("date"), "索引" + i + "的日期不应为null");
            assertNotNull(item.getBigDecimal("value"), "索引" + i + "的值不应为null");
        }
        
        // 打印部分数据用于手动检查
        System.out.println("CashValSet部分数据:");
        for (int i = 0; i < 5; i++) {
            System.out.println("索引" + i + ": " + cashValSet.getJSONObject(String.valueOf(i)));
        }
        System.out.println("...");
        for (int i = 1268; i < 1273; i++) {
            System.out.println("索引" + i + ": " + cashValSet.getJSONObject(String.valueOf(i)));
        }
    }
}
