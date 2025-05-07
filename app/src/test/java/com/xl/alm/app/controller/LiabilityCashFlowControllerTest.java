package com.xl.alm.app.controller;

import com.jd.lightning.common.core.domain.Result;
import com.xl.alm.app.dto.LiabilityCashFlowDTO;
import com.xl.alm.app.service.LiabilityCashFlowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 负债现金流Controller测试类
 */
public class LiabilityCashFlowControllerTest {

    @InjectMocks
    private LiabilityCashFlowController controller;

    @Mock
    private LiabilityCashFlowService liabilityCashFlowService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testImportData() throws Exception {
        // 准备测试数据
        File file = ResourceUtils.getFile("classpath:test-data/liability_cash_flow_template.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", 
                "liability_cash_flow_template.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                inputStream);
        
        // 模拟service方法
        when(liabilityCashFlowService.importLiabilityCashFlowDto(anyList(), anyBoolean(), anyString()))
                .thenReturn("成功导入10条数据");
        
        // 执行导入方法
        Result result = controller.importData(multipartFile, true);
        
        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("成功导入10条数据", result.getMsg());
        
        // 验证service方法被调用
        verify(liabilityCashFlowService, times(1))
                .importLiabilityCashFlowDto(anyList(), eq(true), anyString());
    }
}
