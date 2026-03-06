package com.jkypch.web.repository.mybatis;

import com.jkypch.web.domain.VisitLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VisitLogMapper {
    List<VisitLog> findRecent(@Param("limit") int limit);
    List<VisitLog> findByPath(@Param("path") String path, @Param("limit") int limit);
}
