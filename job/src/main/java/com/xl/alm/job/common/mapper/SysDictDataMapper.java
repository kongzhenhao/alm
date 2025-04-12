package com.xl.alm.job.common.mapper;

import com.xl.alm.job.common.entity.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName SysDictDataMapper
 * @Author luyu
 * @date 2023.09.15 22:01
 * @Description
 */
public interface SysDictDataMapper {

    List<SysDictData> selectDictDataList(SysDictData sysDictData);

    List<SysDictData> selectDictDataByType(String type);

    String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    SysDictData selectDictDataById(Long type);

    int countDictDataByType(String type);

    int deleteDictDataById(Long id);

    int deleteDictDataByIds(Long[] ids);

    int insertDictData(SysDictData sysDictData);

    int updateDictData(SysDictData sysDictData);

    int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
