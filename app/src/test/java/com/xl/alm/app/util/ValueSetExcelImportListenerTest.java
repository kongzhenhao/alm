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
 * ValueSetExcelImportListener 测试类
 */
public class ValueSetExcelImportListenerTest {

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
        
        // 验证JSON中包含序号和日期、值
        for (String key : cashValSet.keySet()) {
            JSONObject item = cashValSet.getJSONObject(key);
            assertNotNull(item);
            assertNotNull(item.getString("date"));
            assertNotNull(item.getBigDecimal("value"));
        }
        
        // 打印第一条数据的cashValSet字段，用于手动检查
        System.out.println("CashValSet: " + dto.getCashValSet());
    }
}
