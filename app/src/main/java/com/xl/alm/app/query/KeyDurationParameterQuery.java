package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 关键久期参数表查询对象
 *
 * @author AI Assistant
 */
@Data
public class KeyDurationParameterQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期,格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 关键期限点
     */
    private String keyDuration;
}
