package com.william.blog.dto;

import lombok.Data;

@Data
public class HotArticleDto {
    private Long id;
    private String title;
    private String summary;
    private Double click;
}
