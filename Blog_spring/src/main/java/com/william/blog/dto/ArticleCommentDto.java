package com.william.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@ApiModel(value = "ArticleCommentDto")
@Data
public class ArticleCommentDto {
    // tbl-comment
    private Long id;
    private String content;
    private String name;
    private String email;
    private String ip;
    private Date createBy;

    //tbl_article_comment
    private Long articleCommentId;
    private Long articleId;

    //@JsonFormat注解的作用就是将Date转换成String 后台传前台
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Date getCreateBy() {
        return createBy;
    }
}
