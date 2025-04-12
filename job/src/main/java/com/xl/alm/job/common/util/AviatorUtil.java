package com.xl.alm.job.common.util;

import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class AviatorUtil {
    /**
     * Executes the Aviator expression with the given boolean data.
     *
     * @param aviatorExpression the Aviator expression to be executed
     * @param data              the data used in the expression execution
     * @return true if the expression execution is successful, false otherwise
     */
    public static boolean evaluateBoolean(Expression aviatorExpression, Map<String, Object> data) {
        if (aviatorExpression == null) {
            return false;
        }
        try {
            Object execute = aviatorExpression.execute(data);
            return (boolean) execute;
        } catch (Exception e) {
            log.error("aviatorExpression-error, expression = {}, data = {}", aviatorExpression, data);
            return false;
        }
    }


    /**
     * Executes the Aviator expression with the given data.
     *
     * @param aviatorExpression the Aviator expression to be executed
     * @param data              the data used in the expression execution
     * @return the result of the expression execution
     */
    public static Object evaluateObject(Expression aviatorExpression, Map<String, Object> data) {
        if (aviatorExpression == null) {
            return null;
        }
        try {
            return aviatorExpression.execute(data);
        } catch (Exception e) {
            log.error("aviatorExpression-error, expression = {}, data = {}", aviatorExpression, data, e);
            return null;
        }
    }


    /**
     * Executes the Aviator expression with the given data.
     *
     * @param aviatorExpression the Aviator expression to be executed
     * @param data              the data used in the expression execution
     * @return the result of the expression execution
     */
    public static <T> T evaluateObject(Expression aviatorExpression, Map<String, Object> data, Class<T> clz) {
        if (aviatorExpression == null) {
            return null;
        }
        try {
            Object result = aviatorExpression.execute(data);
            return clz.cast(result);
        } catch (Exception e) {
            log.error("aviatorExpression-error, expression = {}, data = {}", aviatorExpression, data, e);
            return null;
        }
    }
}
