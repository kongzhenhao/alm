package com.xl.alm.job.common.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一异常管理
 * enum：业务类型_错误码(errorCode, errorMsg)
 * errorCode：前三位是业务类型，后两位是该业务的异常序号，★不可重复★
 * errorCode（8位）：前三位是系统类型，中间两位是业务类型，后面三位是异常序号，★不可重复★
 * common: 000 + 00(公共)+XXX
 * ★每个错误码的注释描述解决方案，便于整理异常码查询文档★
 */
public enum InsErrorEnum {

    /* 公共异常 start */
    RES_ERROR_CODE("00000001", "系统开小差了，请稍后再试试吧~~"),
    PARAMS_ERROR_CHECK_DATE("0000101", "日期格式不对，请按照yyyyMM格式输入！"),
    PARAMS_ERROR_CHECK_MAX_THREAD_COUNT("0000102", "单次最大线程数不能超过50t条！"),
    PARAMS_ERROR_CHECK_MAX_LIMT("0000103", "单次最大获取数据不能超过1,000,000条！"),
    INCOME_ALLOCATION_ERROR("0000104", "收益分摊结果异常！"),
    ;

    @Getter
    private final String errorCode;
    @Setter
    @Getter
    private String errorMsg;

    InsErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
