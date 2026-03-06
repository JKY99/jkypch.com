package com.jkypch.web.dto;

import java.util.List;

public record VisitStatsDto(
        long total,
        long today,
        long uniqueIps,
        List<PageStatDto> topPages,
        List<DailyStatDto> dailyTrend
) {}
