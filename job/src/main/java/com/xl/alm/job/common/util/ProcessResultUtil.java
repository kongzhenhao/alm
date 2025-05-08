package com.xl.alm.job.common.util;

import tech.powerjob.worker.core.processor.ProcessResult;

/**
 * 处理结果工具类
 *
 * @author AI
 */
public class ProcessResultUtil {

    /**
     * 创建成功结果
     *
     * @param msg 成功消息
     * @return 处理结果
     */
    public static ProcessResult success(String msg) {
        return new ProcessResult(true, msg);
    }

    /**
     * 创建失败结果
     *
     * @param msg 失败消息
     * @return 处理结果
     */
    public static ProcessResult fail(String msg) {
        return new ProcessResult(false, msg);
    }
}
