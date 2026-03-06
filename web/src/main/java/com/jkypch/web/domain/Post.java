package com.jkypch.web.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "posts")
public class Post {

    @Id
    private String id;
    private String title;
    private String excerpt;
    private String content;
    private String date;
    private List<String> tags;

    public Post() {}

    public Post(String title, String excerpt, String content, String date, List<String> tags) {
        this.title = title;
        this.excerpt = excerpt;
        this.content = content;
        this.date = date;
        this.tags = tags;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getExcerpt() { return excerpt; }
    public String getContent() { return content; }
    public String getDate() { return date; }
    public List<String> getTags() { return tags; }
}
