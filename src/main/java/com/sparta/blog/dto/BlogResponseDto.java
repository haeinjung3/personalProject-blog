package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BlogResponseDto {
    private Long id;
    private String title;
    private String username;
    private String contents;
    private LocalDate date;
    private Long password;

    public BlogResponseDto(Blog blog){
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.username = blog.getUsername();
        this.contents = blog.getContents();
        this.date = blog.getDate();
        this.password = blog.getPassword();
    }

    public BlogResponseDto(Long id, String title, String username, String contents, LocalDate date, Long password) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.contents = contents;
        this.date = date;
        this.password = password;
    }
}
