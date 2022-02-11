package com.william.blog.service;

import com.william.blog.dto.ArticleCommentDto;
import com.william.blog.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);

    void addArticleComment(ArticleCommentDto articleCommentDto);

    void deleteCommentById(long id);

    void deleteArticleCommentById(long id);

    List<Comment> listAllComment();

    List<Comment> listAllArticleCommentById(long id);
}
