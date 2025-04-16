package com.xl.alm.app.util;

import com.jd.lightning.common.utils.bean.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体与DTO转换工具类
 * 提供通用的实体与DTO之间的转换方法
 *
 * @author AI Assistant
 */
public class EntityDtoConvertUtil {

    /**
     * 将实体转换为DTO
     *
     * @param entity 实体对象
     * @param dtoClass DTO类
     * @param <T> DTO类型
     * @param <E> 实体类型
     * @return DTO对象
     */
    public static <T, E> T convertToDTO(E entity, Class<T> dtoClass) {
        if (entity == null) {
            return null;
        }
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("转换DTO失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将DTO转换为实体
     *
     * @param dto DTO对象
     * @param entityClass 实体类
     * @param <T> DTO类型
     * @param <E> 实体类型
     * @return 实体对象
     */
    public static <T, E> E convertToEntity(T dto, Class<E> entityClass) {
        if (dto == null) {
            return null;
        }
        try {
            E entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("转换实体失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将实体列表转换为DTO列表
     *
     * @param entityList 实体列表
     * @param dtoClass DTO类
     * @param <T> DTO类型
     * @param <E> 实体类型
     * @return DTO列表
     */
    public static <T, E> List<T> convertToDTOList(List<E> entityList, Class<T> dtoClass) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(entity -> convertToDTO(entity, dtoClass))
                .collect(Collectors.toList());
    }

    /**
     * 将DTO列表转换为实体列表
     *
     * @param dtoList DTO列表
     * @param entityClass 实体类
     * @param <T> DTO类型
     * @param <E> 实体类型
     * @return 实体列表
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
