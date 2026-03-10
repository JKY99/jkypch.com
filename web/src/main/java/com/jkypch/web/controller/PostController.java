package com.jkypch.web.controller;

import com.jkypch.web.domain.Post;
import com.jkypch.web.repository.mongo.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    record PostSummary(String id, String title, String date, List<String> tags, String excerpt) {}

    @GetMapping
    public List<PostSummary> list() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(p -> new PostSummary(p.getId(), p.getTitle(), p.getDate(), p.getTags(), p.getExcerpt()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable String id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
