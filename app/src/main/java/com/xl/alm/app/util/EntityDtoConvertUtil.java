package com.xl.alm.app.util;

import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity和DTO转换工具类
 *
 * @author AI Assistant
 */
public class EntityDtoConvertUtil {

    /**
     * 将Entity转换为DTO
     *
     * @param entity Entity对象
     * @param dtoClass DTO类
     * @param <T> DTO类型
     * @param <E> Entity类型
     * @return DTO对象
     */
    public static <T, E> T convertToDTO(E entity, Class<T> dtoClass) {
        if (entity == null) {
            return null;
        }
        try {
            T dto = dtoClass.newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Entity转换DTO失败", e);
        }
    }

    /**
     * 将DTO转换为Entity
     *
     * @param dto DTO对象
     * @param entityClass Entity类
     * @param <T> DTO类型
     * @param <E> Entity类型
     * @return Entity对象
     */
    public static <T, E> E convertToEntity(T dto, Class<E> entityClass) {
        if (dto == null) {
            return null;
        }
        try {
            E entity = entityClass.newInstance();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("DTO转换Entity失败", e);
        }
    }

    /**
     * 将Entity列表转换为DTO列表
     *
     * @param entityList Entity列表
     * @param dtoClass DTO类
     * @param <T> DTO类型
     * @param <E> Entity类型
     * @return DTO列表
     */
    public static <T, E> List<T> convertToDTOList(List<E> entityList, Class<T> dtoClass) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        if (null == dtoClass) {
            return null;
        }

        if (entityList instanceof Page) {
            Page<T> page = new Page();
            com.jd.lightning.common.utils.bean.BeanUtils.copyProperties(entityList, page);
            page.addAll(JsonUtil.parseArray(JsonUtil.toJSONString(entityList, false), dtoClass));
            return page;
        } else {
            return JsonUtil.parseArray(JsonUtil.toJSONString(entityList, false), dtoClass);
        }
    }

    /**
     * 将DTO列表转换为Entity列表
     *
     * @param dtoList DTO列表
     * @param entityClass Entity类
     * @param <T> DTO类型
     * @param <E> Entity类型
     * @return Entity列表
     */
    public static <T, E> List<E> convertToEntityList(List<T> dtoList, Class<E> entityClass) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }
        return dtoList.stream()
                .map(dto -> convertToEntity(dto, entityClass))
                .collect(Collectors.toList());
    }
}
