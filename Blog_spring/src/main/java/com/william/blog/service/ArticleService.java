package com.william.blog.service;
import com.github.pagehelper.PageInfo;
import com.william.blog.dao.ArticleInfoMapper;
import com.william.blog.dto.ArticleDto;
import com.william.blog.dto.ArticleWithPictureDto;
import com.william.blog.entity.ArticlePicture;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 文章Service
 * 说明：ArticleInfo里面封装了picture/content/category等信息
 */
public interface ArticleService {

    void addArticle(ArticleDto articleDto);

    void deleteArticleById(Long id);

    void updateArticle(ArticleDto articleDto);

    void updateArticleCategory(Long articleId, Long categoryId);

    ArticleDto getOneById(Long id);

    void increTraffic(Long id);

    ArticlePicture getPictureByArticleId(Long id);

    PageInfo listAll(Long pageIndex,Long pageSize);

    PageInfo listByCategoryId(Long id, Long pageNum, Long pageSize);

    List<ArticleWithPictureDto> listLastest();

}