package com.xl.alm.app.service;

import com.xl.alm.app.entity.StatutoryReserveDetailEntity;
import com.xl.alm.app.query.StatutoryReserveDetailQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 法定准备金明细表 服务接口
 *
 * @author AI Assistant
 */
public interface IStatutoryReserveDetailService {
    /**
     * 查询法定准备金明细列表
     *
     * @param query 法定准备金明细查询条件
     * @return 法定准备金明细列表
     */
    List<StatutoryReserveDetailEntity> selectStatutoryReserveDetailList(StatutoryReserveDetailQuery query);

    /**
     * 根据ID查询法定准备金明细
     *
     * @param id 法定准备金明细ID
     * @return 法定准备金明细
     */
    StatutoryReserveDetailEntity selectStatutoryReserveDetailById(Long id);

    /**
     * 新增法定准备金明细
     *
     * @param statutoryReserveDetail 法定准备金明细
     * @return 结果
     */
    int insertStatutoryReserveDetail(StatutoryReserveDetailEntity statutoryReserveDetail);

    /**
     * 修改法定准备金明细
     *
     * @param statutoryReserveDetail 法定准备金明细
     * @return 结果
     */
    int updateStatutoryReserveDetail(StatutoryReserveDetailEntity statutoryReserveDetail);

    /**
     * 批量删除法定准备金明细
     *
     * @param ids 需要删除的法定准备金明细ID
     * @return 结果
     */
    int deleteStatutoryReserveDetailByIds(Long[] ids);

    /**
     * 删除法定准备金明细信息
     *
     * @param id 法定准备金明细ID
     * @return 结果
     */
    int deleteStatutoryReserveDetailById(Long id);

    /**
     * 导入法定准备金明细数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importStatutoryReserveDetail(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出法定准备金明细数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateStatutoryReserveDetail(HttpServletResponse response);
}
