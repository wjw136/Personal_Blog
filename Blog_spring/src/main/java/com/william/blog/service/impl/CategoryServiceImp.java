package com.william.blog.service.impl;

import com.william.blog.dao.ArticleCategoryMapper;
import com.william.blog.dao.CategoryInfoMapper;
import com.william.blog.entity.ArticleCategory;
import com.william.blog.entity.ArticleCategoryExample;
import com.william.blog.entity.CategoryInfo;
import com.william.blog.entity.CategoryInfoExample;
import com.william.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    @Resource
    CategoryInfoMapper categoryInfoMapper;//默认按照类型注入
    @Resource
    ArticleCategoryMapper articleCategoryMapper;

    @Override
    public void addCategory(CategoryInfo categoryInfo) {
        categoryInfoMapper.insertSelective(categoryInfo);//null的字段会自动填充default的值
        // 类中的字段 Integer会填充null int会=0
    }

    @Override
    public void deleteCategoryById(long id) {
        // 删除分类=> 先删除该分类下文章的分类关联表
        ArticleCategoryExample example= new ArticleCategoryExample();
        example.or().andCategoryIdEqualTo(id);
        List<ArticleCategory> res= articleCategoryMapper.selectByExample(example);
        for(ArticleCategory articleCategory : res){
            articleCategoryMapper.deleteByPrimaryKey(articleCategory.getId());
        }
        categoryInfoMapper.deleteByPrimaryKey(id);

    }

    @Override
    public void updateCategory(CategoryInfo categoryInfo) {
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
    }

    @Override
    public void updateArticleCategory(ArticleCategory articleCategory) {

    }

    @Override
    public CategoryInfo getOneById(Long id) {
        return null;
    }

    @Override
    public List<CategoryInfo> listAllCategory() {
        CategoryInfoExample categoryInfoExample=new CategoryInfoExample();
        return categoryInfoMapper.selectByExample(categoryInfoExample);
    }
}
