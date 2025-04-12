package com.xl.alm.job.common.exception;

import com.xl.alm.job.common.constant.InsErrorEnum;

public class BaseBusinessException extends RuntimeException {

    //业务错误编码
    private String code;

    //业务错误信息明细
    private final String messageDesc;

    public BaseBusinessException(String messageDesc) {
        super(messageDesc);
        this.messageDesc = messageDesc;
    }

    public BaseBusinessException(String code, String message, String messageDesc) {
        super(message);
        this.code = code;
        this.messageDesc = messageDesc;
    }

    public BaseBusinessException(InsErrorEnum insError) {
        super(insError.getErrorMsg());
        this.code = insError.getErrorCode();
        this.messageDesc = insError.getErrorMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMessageDesc() {
        return messageDesc;
    }


}
