package com.jkypch.web.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;
    private String title;
    private String excerpt;
    private String content;
    private String date;       // 표시용 (예: "2026-03-10")
    private Instant createdAt; // 정렬용 — 실제 등록 시각
    private List<String> tags;

    public Post() {}

    public Post(String id, String title, String excerpt, String content,
                String date, Instant createdAt, List<String> tags) {
        this.id = id;
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.date = date;
        this.createdAt = createdAt;
        this.tags = List.copyOf(tags);
    }

    public Post(String title, String excerpt, String content,
                String date, Instant createdAt, List<String> tags) {
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.date = date;
        this.createdAt = createdAt;
        this.tags = List.copyOf(tags);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getExcerpt() { return excerpt; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public Instant getCreatedAt() { return createdAt; }
    public List<String> getTags() { return List.copyOf(tags); }
}
