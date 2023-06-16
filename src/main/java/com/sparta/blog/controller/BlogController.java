package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Blog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BlogController {

    private final JdbcTemplate jdbcTemplate;

    public BlogController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/blogs")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto) {
        // RequestDto -> Entity
        Blog blog = new Blog(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO blog (title, username, contents, date, password) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, blog.getTitle());
                    preparedStatement.setString(2, blog.getUsername());
                    preparedStatement.setString(3, blog.getContents());
                    preparedStatement.setDate(4, java.sql.Date.valueOf(blog.getDate()));
                    preparedStatement.setLong(5, blog.getPassword());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        blog.setId(id);

        // Entity -> ResponseDto
        BlogResponseDto blogResponseDto = new BlogResponseDto(blog);

        return blogResponseDto;
    }

    @GetMapping("/blogs")
    public List<BlogResponseDto> getBlogs() {
        // DB 조회
        String sql = "SELECT * FROM blog";

        return jdbcTemplate.query(sql, new RowMapper<BlogResponseDto>() {
            @Override
            public BlogResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 blog 데이터들을 blogResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                LocalDate date = rs.getDate("date").toLocalDate();
                Long password = rs.getLong("password");

                return new BlogResponseDto(id, title, username, contents, date, password);
            }
        });
    }

//    @PutMapping("/blogs/{id}")
//    public Long updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto) {
//        // 해당 메모가 DB에 존재하는지 확인
//        Blog blog = findById(id);
//        if(blog != null) {
//            // blog 내용 수정
//            String sql = "UPDATE blog SET username = ?, contents = ? WHERE id = ?";
//            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
//
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
//        }
//    }
//
//    @DeleteMapping("/blogs/{id}")
//    public Long deleteBlog(@PathVariable Long id) {
//        // 해당 메모가 DB에 존재하는지 확인
//        Blog blog = findById(id);
//        if(blog != null) {
//            // blog 삭제
//            String sql = "DELETE FROM blog WHERE id = ?";
//            jdbcTemplate.update(sql, id);
//
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
//        }
//    }
//
//    private Blog findById(Long id) {
//        // DB 조회
//        String sql = "SELECT * FROM blog WHERE id = ?";
//
//        return jdbcTemplate.query(sql, resultSet -> {
//            if(resultSet.next()) {
//                Blog blog = new Blog();
//                blog.setUsername(resultSet.getString("username"));
//                blog.setContents(resultSet.getString("contents"));
//                return blog;
//            } else {
//                return null;
//            }
//        }, id);
//    }
}