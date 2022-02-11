package com.william.blog.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

//DTO和DAO 的数据一般都是对象 如果不存在变成null 在插入到数据库的时候会使用default的值
@Data
@ApiModel(value = "ArticleWithPictureDto")
public class ArticleWithPictureDto {
    //tbl_article_info
    private Long id;
    private String title;
    private String summary;
    private Boolean isTop;
    private Integer traffic;

    // tbl_article_picture
    private Long articlePictureId;
    private String pictureUrl;


}
