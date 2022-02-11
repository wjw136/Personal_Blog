package com.william.blog.service.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.william.blog.dao.*;
import com.william.blog.dto.ArticleDto;
import com.william.blog.dto.ArticleWithPictureDto;
import com.william.blog.dto.HotArticleDto;
import com.william.blog.entity.*;
import com.william.blog.service.ArticleService;
import com.william.blog.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ArticleServiceImp implements ArticleService {

    @Autowired
    ArticleInfoMapper articleInfoMapper;
    @Resource
    ArticleContentMapper articleContentMapper;
    @Resource
    ArticlePictureMapper articlePictureMapper;
    @Resource
    ArticleCategoryMapper articleCategoryMapper;
    @Resource
    CategoryInfoMapper categoryInfoMapper;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public void addArticle(ArticleDto articleDto) {
        ArticleInfo articleInfo=new ArticleInfo();
        articleInfo.setTitle(articleDto.getTitle());
        articleInfo.setIsTop(articleDto.getIsTop());
        articleInfo.setSummary(articleDto.getSummary());
        articleInfoMapper.insertSelective(articleInfo);
        // 获取刚才插入文章信息的ID
        Long articleId = getArticleLastestId();

        ArticleContent articleContent=new ArticleContent();
        articleContent.setArticleId(articleId);
        articleContent.setContent(articleDto.getContent());
        articleContentMapper.insertSelective(articleContent);

        ArticlePicture articlePicture=new ArticlePicture();
        articlePicture.setArticleId(articleId);
        articlePicture.setPictureUrl(articleDto.getPictureUrl());
        articlePictureMapper.insertSelective(articlePicture);

//        System.out.println("aaa");

        //增加文章分类信息
        ArticleCategory articleCategory= new ArticleCategory();
        articleCategory.setArticleId(articleId);
        articleCategory.setCategoryId(articleDto.getCategoryId());
        articleCategoryMapper.insertSelective(articleCategory);
        //增加文章对应分类类型的数量
        CategoryInfo categoryInfo = categoryInfoMapper.selectByPrimaryKey(articleDto.getCategoryId());
        categoryInfo.setNumber((byte)(categoryInfo.getNumber()+1));
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);

    }

    private Long getArticleLastestId(){
        ArticleInfoExample example= new ArticleInfoExample();
        example.setOrderByClause("id desc");
        //selectbyample返回list
        return articleInfoMapper.selectByExample(example).get(0).getId();
    }

    @Override
    public void deleteArticleById(Long id) {
        // 获取对应的文章信息
        ArticleDto articleDto = getOneById(id);
        // 删除文章信息中的数据
        articleInfoMapper.deleteByPrimaryKey(articleDto.getId());
        // 删除文章题图信息数据
        articlePictureMapper.deleteByPrimaryKey(articleDto.getArticlePictureId());
        // 删除文章内容信息表
        articleContentMapper.deleteByPrimaryKey(articleDto.getArticleContentId());
        // 删除文章分类信息表
        articleCategoryMapper.deleteByPrimaryKey(articleDto.getArticleCategoryId());

        decreCategory(articleDto.getCategoryId());
    }

    @Override
    public void updateArticle(ArticleDto articleDto) {
        // 更新文章信息中的数据
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.setId(articleDto.getId());
        articleInfo.setTitle(articleDto.getTitle());
        articleInfo.setSummary(articleDto.getSummary());
        articleInfo.setIsTop(articleDto.getIsTop());
        articleInfo.setTraffic(articleDto.getTraffic());
        articleInfoMapper.updateByPrimaryKeySelective(articleInfo);
        // 更新文章题图信息数据
        ArticlePictureExample pictureExample = new ArticlePictureExample();
        pictureExample.or().andArticleIdEqualTo(articleDto.getId());
        ArticlePicture articlePicture = articlePictureMapper.selectByExample(pictureExample).get(0);
//        articlePicture.setId(articleDto.getArticlePictureId());
        articlePicture.setPictureUrl(articleDto.getPictureUrl());
        articlePictureMapper.updateByPrimaryKeySelective(articlePicture);
        // 更新文章内容信息数据
        ArticleContentExample contentExample = new ArticleContentExample();
        contentExample.or().andArticleIdEqualTo(articleDto.getId());
        ArticleContent articleContent = articleContentMapper.selectByExample(contentExample).get(0);
//		articleContent.setArticleId(articleDto.getId());
//		articleContent.setId(articleDto.getArticleContentId());
        articleContent.setContent(articleDto.getContent());
        articleContentMapper.updateByPrimaryKeySelective(articleContent);
        // 更新文章分类信息表
        ArticleCategoryExample categoryExample = new ArticleCategoryExample();
        categoryExample.or().andArticleIdEqualTo(articleDto.getId());
        ArticleCategory articleCategory = articleCategoryMapper.selectByExample(categoryExample).get(0);
//        articleCategory.setId(articleDto.getArticleCategoryId());

        decreCategory(articleCategory.getCategoryId());
        increCategory(articleDto.getCategoryId());

        articleCategory.setCategoryId(articleDto.getCategoryId());
        articleCategoryMapper.updateByPrimaryKeySelective(articleCategory);
    }


    @Override
    public void updateArticleCategory(Long articleId, Long categoryId) {

    }

    @Override
    public ArticleDto getOneById(Long id) {
        //访问量++的逻辑放到controller判断

        ArticleDto articleDto=new ArticleDto();
        //info
        ArticleInfo articleInfo= articleInfoMapper.selectByPrimaryKey(id);
        articleDto.setId(id);
        articleDto.setTitle(articleInfo.getTitle());
        articleDto.setSummary(articleInfo.getSummary());
        articleDto.setIsTop(articleInfo.getIsTop());
        articleDto.setCreateBy(articleInfo.getCreateBy());
        //content
        ArticleContentExample example = new ArticleContentExample();
        example.or().andArticleIdEqualTo(id);
        ArticleContent articleContent = articleContentMapper.selectByExample(example).get(0);
        articleDto.setContent(articleContent.getContent());
        articleDto.setArticleContentId(articleContent.getId());
        // 填充文章题图信息
        ArticlePictureExample example1 = new ArticlePictureExample();
        example1.or().andArticleIdEqualTo(id);
        ArticlePicture articlePicture = articlePictureMapper.selectByExample(example1).get(0);
        articleDto.setPictureUrl(articlePicture.getPictureUrl());
        articleDto.setArticlePictureId(articlePicture.getId());
        // 填充文章分类信息
        ArticleCategoryExample example2 = new ArticleCategoryExample();
        example2.or().andArticleIdEqualTo(id);
        ArticleCategory articleCategory = articleCategoryMapper.selectByExample(example2).get(0);
        articleDto.setArticleCategoryId(articleCategory.getId());
        // 填充文章分类基础信息
        CategoryInfoExample example3 = new CategoryInfoExample();
        example3.or().andIdEqualTo(articleCategory.getCategoryId());
        CategoryInfo categoryInfo = categoryInfoMapper.selectByExample(example3).get(0);
        articleDto.setCategoryId(categoryInfo.getId());
        articleDto.setCategoryName(categoryInfo.getName());
        articleDto.setCategoryNumber(categoryInfo.getNumber().intValue());
        return articleDto;


    }

    @Override
    public void increTraffic(Long id){
        //增加文章访问量
        ArticleInfo articleInfo = articleInfoMapper.selectByPrimaryKey(id);
        articleInfo.setTraffic(articleInfo.getTraffic() + 1);
        articleInfoMapper.updateByPrimaryKey(articleInfo);

        long hour=System.currentTimeMillis()/(1000*60*60*24);
        redisUtil.zIncreamentScore(String.valueOf(hour), articleInfo.getId(),1);
        redisUtil.expire(String.valueOf(hour), 40, TimeUnit.DAYS);
    }


    @Override
    public ArticlePicture getPictureByArticleId(Long id) {
        return null;
    }

    @Override
    public PageInfo listAll(Long pageIndex,Long pageSize) {
        //only for next one
        PageHelper.startPage(pageIndex.intValue(), pageSize.intValue(),true);

        ArticleInfoExample example1= new ArticleInfoExample();
        example1.setOrderByClause("id desc");
        List<ArticleInfo> res=articleInfoMapper.selectByExample(example1);

        List<ArticleWithPictureDto> res1= new ArrayList<>();

        for(int i=0;i<res.size();++i){
            ArticleInfo articleInfo=res.get(i);
            Long articleId=articleInfo.getId();

            ArticleWithPictureDto tmp_res=new ArticleWithPictureDto();
            tmp_res.setId(articleId);
            tmp_res.setTitle(articleInfo.getTitle());
            tmp_res.setSummary(articleInfo.getSummary());
            tmp_res.setTraffic(articleInfo.getTraffic());
            tmp_res.setIsTop(articleInfo.getIsTop());

            ArticlePictureExample articlePictureExample=new ArticlePictureExample();
            articlePictureExample.or().andArticleIdEqualTo(articleId);
            ArticlePicture articlePicture=articlePictureMapper.selectByExample(articlePictureExample).get(0);
            tmp_res.setArticlePictureId(articlePicture.getId());
            tmp_res.setPictureUrl(articlePicture.getPictureUrl());

            res1.add(tmp_res);

        }

        PageInfo pageInfo=new PageInfo(res);
        List<ArticleWithPictureDto> T=reOrderByTop(res1);
        pageInfo.setList(reOrderByTop(res1));
        return pageInfo;
//        return reOrderByTop(res1);
    }

    @Override
    public PageInfo listByCategoryId(Long id,Long pageNum, Long pageSize) {

        PageHelper.startPage(pageNum.intValue(), pageSize.intValue(),true);

        log.info(String.format("查询类别为 %d 的文章",id));

        ArticleCategoryExample example= new ArticleCategoryExample();
        example.or().andCategoryIdEqualTo(id);
        List<ArticleCategory> articleCategoryList=articleCategoryMapper.selectByExample(example);

        List<ArticleWithPictureDto> articleWithPictureDtos= new ArrayList<>();
        for(int i=0;i<articleCategoryList.size();++i){
            Long articleId=articleCategoryList.get(i).getArticleId();
            ArticleWithPictureDto articleWithPictureDto = new ArticleWithPictureDto();

            ArticleInfoExample example1= new ArticleInfoExample();
            example1.or().andIdEqualTo(articleId);
            ArticleInfo articleInfo= articleInfoMapper.selectByExample(example1).get(0);
            articleWithPictureDto.setId(articleId);
            articleWithPictureDto.setSummary(articleInfo.getSummary());
            articleWithPictureDto.setTitle(articleInfo.getTitle());
            articleWithPictureDto.setIsTop(articleInfo.getIsTop());
            articleWithPictureDto.setTraffic(articleInfo.getTraffic());

            ArticlePictureExample articlePictureExample= new ArticlePictureExample();
            articlePictureExample.or().andArticleIdEqualTo(articleId);
            ArticlePicture articlePicture=articlePictureMapper.selectByExample(articlePictureExample).get(0);
            articleWithPictureDto.setArticlePictureId(articlePicture.getId());
            articleWithPictureDto.setPictureUrl(articlePicture.getPictureUrl());

            articleWithPictureDtos.add(articleWithPictureDto);
        }

//        LinkedList<ArticleWithPictureDto> list= new LinkedList<>();
//        for (ArticleWithPictureDto articleWithPictureDto : articleWithPictureDtos) {
//            if (articleWithPictureDto.getIsTop())
//                list.addFirst(articleWithPictureDto);
//            else
//                list.add(articleWithPictureDto);
//        }
//        articleWithPictureDtos = new ArrayList<>(list);

        PageInfo pageInfo=new PageInfo(articleCategoryList);
        pageInfo.setList(reOrderByTop(articleWithPictureDtos));
        return pageInfo;

//        return reOrderByTop(articleWithPictureDtos);
    }

    private List<ArticleWithPictureDto> reOrderByTop(List<ArticleWithPictureDto> articleWithPictureDtos){
        LinkedList<ArticleWithPictureDto> list= new LinkedList<>();
        for (ArticleWithPictureDto articleWithPictureDto : articleWithPictureDtos) {
            if (articleWithPictureDto.getIsTop())
                list.addFirst(articleWithPictureDto);
            else
                list.add(articleWithPictureDto);
        }
        articleWithPictureDtos = new ArrayList<>(list);
        return articleWithPictureDtos;
    }

    @Override
    public List<ArticleWithPictureDto> listLastest() {
        //最新其实就是跟进表id越大就越新来判断
        ArticleInfoExample example1= new ArticleInfoExample();
        example1.setOrderByClause("id desc");
        List<ArticleInfo> res=articleInfoMapper.selectByExample(example1);

        List<ArticleWithPictureDto> res1= new ArrayList<>();

        for(int i=0;i<Math.min(res.size(),5);++i){
            ArticleInfo articleInfo=res.get(i);
            Long articleId=articleInfo.getId();

            ArticleWithPictureDto tmp_res=new ArticleWithPictureDto();
            tmp_res.setId(articleId);
            tmp_res.setTitle(articleInfo.getTitle());
            tmp_res.setSummary(articleInfo.getSummary());
            tmp_res.setTraffic(articleInfo.getTraffic());
            tmp_res.setIsTop(articleInfo.getIsTop());

            ArticlePictureExample articlePictureExample=new ArticlePictureExample();
            articlePictureExample.or().andArticleIdEqualTo(articleId);
            ArticlePicture articlePicture=articlePictureMapper.selectByExample(articlePictureExample).get(0);
            tmp_res.setArticlePictureId(articlePicture.getId());
            tmp_res.setPictureUrl(articlePicture.getPictureUrl());

            res1.add(tmp_res);

        }
        return res1;
    }

    private void increCategory(Long categoryId){
        CategoryInfo categoryInfo = categoryInfoMapper.selectByPrimaryKey(categoryId);
        categoryInfo.setNumber((byte) (categoryInfo.getNumber() + 1));
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
    }

    private void decreCategory(Long categoryId){
        CategoryInfo categoryInfo = categoryInfoMapper.selectByPrimaryKey(categoryId);
        categoryInfo.setNumber((byte) (categoryInfo.getNumber() - 1));
        categoryInfoMapper.updateByPrimaryKeySelective(categoryInfo);
    }


    public List<HotArticleDto> findHotArticle(){
        long hour=System.currentTimeMillis()/(1000*60*60*24);
        Set<ZSetOperations.TypedTuple<Object>> res= redisUtil.zReverseRangeWithScore(String.valueOf(hour));
//        log.warn(String.valueOf(res.size()));
        List<HotArticleDto> hot= new ArrayList<>();

//        int cnt=0;

        for (ZSetOperations.TypedTuple<Object> item :
             res) {
            if(hot.size()>6){
                break;
            }
            //Integer 和 Long 相互转换要通过 String
            Long id= Long.valueOf(item.getValue().toString());
            ArticleDto articleDto= getOneById(id);

            HotArticleDto hotArticleDto= new HotArticleDto();

            hotArticleDto.setId(id);
////            hotArticleDto.setPictureUrl(articleDto.getPictureUrl());
//            hotArticleDto.setTraffic(articleDto.getTraffic());
            hotArticleDto.setSummary(articleDto.getSummary());
            hotArticleDto.setTitle(articleDto.getTitle());
//            articleWithPictureDto.setIsTop(articleDto.getIsTop());
//            articleWithPictureDto.setArticlePictureId(articleDto.getArticlePictureId());
            hotArticleDto.setClick(item.getScore());
            hot.add(hotArticleDto);
        }
        return hot;
    }
}
