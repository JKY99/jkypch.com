package com.jkypch.web.controller;

import com.jkypch.web.domain.VisitLog;
import com.jkypch.web.repository.mybatis.VisitLogMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/visit-logs")
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
public class VisitLogController {

    private final VisitLogMapper visitLogMapper;

    public VisitLogController(VisitLogMapper visitLogMapper) {
        this.visitLogMapper = visitLogMapper;
    }

    @GetMapping("/recent")
    public List<VisitLog> recent(@RequestParam(defaultValue = "50") int limit) {
        return visitLogMapper.findRecent(Math.min(limit, 500));
    }

    @GetMapping("/by-path")
    public List<VisitLog> byPath(@RequestParam String path,
                                  @RequestParam(defaultValue = "50") int limit) {
        return visitLogMapper.findByPath(path, Math.min(limit, 500));
    }
}
