package com.william.blog.dao;

import com.william.blog.entity.SysView;
import com.william.blog.entity.SysViewExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysViewMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysView record);

    int insertSelective(SysView record);

    List<SysView> selectByExample(SysViewExample example);

    SysView selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysView record);

    int updateByPrimaryKey(SysView record);
}