package com.william.blog.service;

import com.william.blog.entity.ArticleCategory;
import com.william.blog.entity.CategoryInfo;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryInfo categoryInfo);

    void deleteCategoryById(long id);

    void updateCategory(CategoryInfo categoryInfo);

    void updateArticleCategory(ArticleCategory articleCategory);

    CategoryInfo getOneById(Long id);

    List<CategoryInfo> listAllCategory();

//    ArticleCategoryDto getCategoryByArticleId(Long id);

}
