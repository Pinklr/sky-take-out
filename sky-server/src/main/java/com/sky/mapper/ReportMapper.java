package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ReportMapper {
    Double getTurnover(Map map);

    Integer getTotalUser(Map map);

    Integer getNewUser(Map map);
}
