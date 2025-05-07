package com.xl.alm.app.util;

import com.xl.alm.app.dto.LiabilityCashFlowDTO;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * ValueSetExcelExporter 测试类
 */
public class ValueSetExcelExporterTest {

    @Test
    public void testExportTemplateExcel() throws Exception {
        // 创建模拟的 HttpServletResponse
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        // 调用导出模板方法
        ValueSetExcelExporter.exportTemplateExcel(LiabilityCashFlowDTO.class, "负债现金流模板测试", response, "cashValSet");
        
        // 验证响应头
        System.out.println("Content-Type: " + response.getContentType());
        System.out.println("Content-Disposition: " + response.getHeader("Content-disposition"));
        System.out.println("Content-Length: " + response.getContentAsByteArray().length);
        
        // 如果响应内容长度大于0，说明生成了Excel文件
        assert response.getContentAsByteArray().length > 0;
    }
}
