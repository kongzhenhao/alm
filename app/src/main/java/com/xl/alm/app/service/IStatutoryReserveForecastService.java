package com.xl.alm.app.service;

import com.xl.alm.app.entity.StatutoryReserveForecastEntity;
import com.xl.alm.app.query.StatutoryReserveForecastQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 法定准备金预测表 服务接口
 *
 * @author AI Assistant
 */
public interface IStatutoryReserveForecastService {
    /**
     * 查询法定准备金预测列表
     *
     * @param query 法定准备金预测查询条件
     * @return 法定准备金预测列表
     */
    List<StatutoryReserveForecastEntity> selectStatutoryReserveForecastList(StatutoryReserveForecastQuery query);

    /**
     * 根据ID查询法定准备金预测
     *
     * @param id 法定准备金预测ID
     * @return 法定准备金预测
     */
    StatutoryReserveForecastEntity selectStatutoryReserveForecastById(Long id);

    /**
     * 新增法定准备金预测
     *
     * @param statutoryReserveForecast 法定准备金预测
     * @return 结果
     */
    int insertStatutoryReserveForecast(StatutoryReserveForecastEntity statutoryReserveForecast);

    /**
     * 修改法定准备金预测
     *
     * @param statutoryReserveForecast 法定准备金预测
     * @return 结果
     */
    int updateStatutoryReserveForecast(StatutoryReserveForecastEntity statutoryReserveForecast);

    /**
     * 批量删除法定准备金预测
     *
     * @param ids 需要删除的法定准备金预测ID
     * @return 结果
     */
    int deleteStatutoryReserveForecastByIds(Long[] ids);

    /**
     * 删除法定准备金预测信息
     *
     * @param id 法定准备金预测ID
     * @return 结果
     */
    int deleteStatutoryReserveForecastById(Long id);

    /**
     * 导入法定准备金预测数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importStatutoryReserveForecast(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出法定准备金预测数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateStatutoryReserveForecast(HttpServletResponse response);
}
