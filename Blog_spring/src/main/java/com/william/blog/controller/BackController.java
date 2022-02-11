package com.william.blog.controller;

import com.william.blog.dto.ArticleDto;
import com.william.blog.dto.User;
import com.william.blog.entity.CategoryInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/admin")
public class BackController extends BaseController{
    private static String username="wjwilliam";
    private static String password="123456";

    @PostMapping("/login")
    // post的表达形式=> @rP
    // post json
    //get=> @rp
    //使用 @RequestBody 注解接收表单提交的参数抛出异常。
    //如果使用的是POST请求，会采用类似GET的字符串拼接的方式，将拼接的key-value字段放到body里面，而非url的后面,所以POST请求的url长度是没有限制的，因为拼接的url请求参数都存放在body里面,
    public String login(User user, HttpServletRequest request, HttpServletResponse response) throws Exception{
//        log.warn(request.getContextPath());
        if(user.getUsername().equals(username) && user.getPassword().equals(password)){
            request.getSession().setAttribute("user",user);
//            log.warn("aaaa");
            //访问数据库，增删改查使用重定向，查询使用转发。
            //转发或重定向后续的代码还会执行
            response.sendRedirect(request.getContextPath()+"/page/index");
            //request.getRequestDispatcher("页面路径").forward(request, response); 请求转发
        }
        else{
            //request.getContextPath()拿到的是你的web项目的根路径
//            log.warn(request.getContextPath()+"/login");
            response.sendRedirect(request.getContextPath()+"/page/login");
        }
        return null;
    }

    @ApiOperation("删除评论")
    @ApiImplicitParam(name="id",value = "评论ID", required = true, dataType = "Long")
    @DeleteMapping("comment/article/{id}")
    public String deleteArticleComment(@PathVariable Long id){
        commentService.deleteCommentById(id);
        return null;
    }

    @ApiOperation("删除一篇文章")
    @ApiImplicitParam(name = "id", value = "文章id", required = true, dataType = "Long")
    @DeleteMapping("article/{id}")
    public String deleteArticle(@PathVariable Long id){
        log.info(String.format("删除文章 %d",id));
        articleService.deleteArticleById(id);
        return null;
    }

    @ApiOperation("新增一篇文章")
    @ApiImplicitParam(name = "articleDto", value = "文章信息", required = true, dataType = "ArticleDto")
    @PostMapping("article/")
    public String addArticle(@RequestBody ArticleDto articleDto){
        articleService.addArticle(articleDto);
        return null;
    }

    @ApiOperation("获取一篇文章")
    @ApiImplicitParam(name = "id", value="文章id", required = true, dataType = "Long")
    @GetMapping("article/{id}")
    public ArticleDto getArticle(@PathVariable Long id){
        return articleService.getOneById(id);
    }

    @ApiOperation("更行一篇文章的信息")
    @ApiImplicitParam(name = "articleDto", value = "文章内容", required = true,dataType = "ArticleDto")
    @PutMapping("article/{id}")
    public String updateArticle(@RequestBody ArticleDto articleDto){
        articleService.updateArticle(articleDto);
        return null;
    }

    @ApiOperation("删除分类信息")
    @ApiImplicitParam(name = "id", value = "分类ID", required = true,dataType = "Long")
    @DeleteMapping("category/{id}")
    public String deleteCategoryInfo(@PathVariable Long id){
//        System.out.println(11111);
        categoryService.deleteCategoryById(id);
        return null;
    }

    @ApiOperation("增加分类信息")
    @ApiImplicitParam(name = "categoryInfo",value = "分类信息", required = true,dataType = "CategoryInfo")
    @PostMapping("category")
    public String addCategoryInfo(@RequestBody CategoryInfo categoryInfo){
        categoryService.addCategory(categoryInfo);
        return null;
    }

    @ApiOperation("更新分类信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryInfo", value = "文章分类信息",required = true, dataType = "CategoryInfo")
    })
    @PutMapping("category/{id}")
    public String updateCategoryInfo(@RequestBody CategoryInfo categoryInfo){
        categoryService.updateCategory(categoryInfo);
        return null;
    }

//    @ApiOperation("删除评论")
//    @ApiImplicitParam(name = "id",value = "评论id",required = true, dataType = "Long")
//    @DeleteMapping("comment/article/{id}")
//    public String deleteCommentById(@PathVariable Long id){
//        commentService.deleteCommentById(id);
//        return null;
//    }
}
