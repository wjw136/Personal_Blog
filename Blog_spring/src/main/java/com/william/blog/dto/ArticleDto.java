package com.william.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "ArticleDto")
@Data
public class ArticleDto {
    // tbl_article_info
    private Long id;
    private String title;
    private String summary;
    private Boolean isTop;
    private Integer traffic;
    private Date createBy;

    //tbl_article_content
    private Long articleContentId;
    private String content;

    //tbl_category_info
    private Long categoryId;
    private String categoryName;
    private Integer categoryNumber;

    //tbl_article_category
    private Long articleCategoryId;

    //tbl_article_picture
    private Long articlePictureId;
    private String pictureUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateBy() {
        return createBy;
    }

}
