package com.william.blog.controller;


import com.github.pagehelper.PageInfo;
import com.william.blog.dto.ArticleCommentDto;
import com.william.blog.dto.ArticleDto;
import com.william.blog.dto.ArticleWithPictureDto;
import com.william.blog.dto.HotArticleDto;
import com.william.blog.entity.CategoryInfo;
import com.william.blog.entity.Comment;
import com.william.blog.service.CategoryService;
import com.william.blog.util.Markdown2HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/*
后端api的设计规范
查找 get=>list+{id}
增加 删除 修改 post
 */

@RestController
@RequestMapping("/api")
public class ForeController extends BaseController{
    //获取文章列表
    @ApiOperation("获取所有文章")
    @GetMapping("article/list")
    @ApiImplicitParams (
            {
                    @ApiImplicitParam(name ="pageIndex", value="页数", required = true, dataType = "Long", defaultValue = "1"),
                    @ApiImplicitParam(name ="pageSize", value="页大小", required = true, dataType = "Long", defaultValue = "6")
            }
    )
    public PageInfo listAllArticleInfo( @RequestParam Long pageIndex, @RequestParam Long pageSize){
        return articleService.listAll(pageIndex,pageSize);
    }

    @ApiOperation("获取某一分类下的所有文章")
    @ApiImplicitParams (
            {@ApiImplicitParam(name = "id", value = "分类id", required = true, dataType = "Long", defaultValue = "1"),
        @ApiImplicitParam(name ="pageIndex", value="页数", required = true, dataType = "Long", defaultValue = "1"),
            @ApiImplicitParam(name ="pageSize", value="页大小", required = true, dataType = "Long", defaultValue = "6")
            }
    )
    @GetMapping("article/list/sort/{id}")
    public PageInfo listArticleInfo(@PathVariable Long id, @RequestParam Long pageIndex, @RequestParam Long pageSize){
//        return null;
        return articleService.listByCategoryId(id,pageIndex,pageSize);
    }


    @ApiOperation("通过文章id获取文章信息")
    @GetMapping("article/{id}")
    public ArticleDto getArticleById(@PathVariable Long id){
        articleService.increTraffic(id);
        ArticleDto articleDto = articleService.getOneById(id);
        articleDto.setContent(Markdown2HtmlUtil.markdown2html(articleDto.getContent()));
        return articleDto;
    }

    @ApiOperation("新增一篇文章")
    @PostMapping("article")
    @ApiImplicitParams(
            {
//                    @ApiImplicitParam(name = "title", value = "文章标题", required = true, dataType = "String"),
//                    @ApiImplicitParam(name = "summary", value="文章简介", required = true, dataType = "String"),
//                    @ApiImplicitParam(name = "isTop" , value="文章是否置顶", required = true, dataType = "String"),
//                    @ApiImplicitParam(name="content",value="文章内容",required = true, dataType = "String"),
//                    @ApiImplicitParam(name = "pictureUrl",value="文章题图",required = true, dataType = "String"),
//                    @ApiImplicitParam(name = "categoryId" ,value="分类ID", required = true, dataType = "Long"),
                    @ApiImplicitParam(name = "articleDto" , value="新增文章的基本信息", required = true, dataType = "ArticleDto")
            }
    )
    public String addArticle(@RequestBody ArticleDto articleDto){
        articleService.addArticle(articleDto);
        return null;
    }

    @ApiOperation("列举最新的几篇文章")
    @GetMapping("article/list/latest")
    public List<ArticleWithPictureDto> listLastestArticle(){
        return articleService.listLastest();
    }

    @ApiOperation("获取所有的分类信息")
    @GetMapping("category/list")
    public List<CategoryInfo> listAllCategoryInfo(){
        System.out.println("11111");
        return categoryService.listAllCategory();
    }

    @ApiOperation("获取所有的评论")//可能用不到 我没有设置留言页
    @GetMapping("comment/list")
    public List<Comment> listAllComment(){
        return commentService.listAllComment();
    }

    @ApiOperation("获取某篇文章的所有的评论信息")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "Long")
    @GetMapping("comment/article/{id}")
    public List<Comment> listCommentByArticleId(@PathVariable(name = "id") Long id){
//        System.out.println(commentService.listAllArticleCommentById(id).size());
        return commentService.listAllArticleCommentById(id);
    }

    @ApiOperation("增加某一篇文章的评论信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "文章id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "articleCommentDto", value = "文章评论", required = true, dataType = "ArticleCommentDto")
    })
    @PostMapping("comment/{id}")
    public String addArticleComment(@PathVariable Long id, @RequestBody ArticleCommentDto articleCommentDto , HttpServletRequest request){
        articleCommentDto.setIp(request.getRemoteAddr());
        articleCommentDto.setArticleId(id);
        commentService.addArticleComment(articleCommentDto);
        return null;
    }

    @ApiOperation("获取当天点击量最多的前六的文章")
    @GetMapping("article/hot")
    public List<HotArticleDto> listHotArticle(){
        return articleService.findHotArticle();
    }

}
