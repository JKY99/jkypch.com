package com.jkypch.web.repository.mybatis;

import com.jkypch.web.domain.VisitLog;
import com.jkypch.web.dto.DailyStatDto;
import com.jkypch.web.dto.PageStatDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VisitLogMapper {
    List<VisitLog> findRecent(@Param("limit") int limit);
    List<VisitLog> findByPath(@Param("path") String path, @Param("limit") int limit);
    List<VisitLog> findPage(@Param("offset") int offset, @Param("limit") int limit);
    long countTotal();
    long countToday();
    long countUniqueIps();
    List<PageStatDto> topPages(@Param("limit") int limit);
    List<DailyStatDto> dailyTrend(@Param("days") int days);
}
