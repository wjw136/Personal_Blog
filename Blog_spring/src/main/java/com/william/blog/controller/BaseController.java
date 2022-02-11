package com.william.blog.controller;

import com.william.blog.service.ArticleService;
import com.william.blog.service.CategoryService;
import com.william.blog.service.CommentService;
import com.william.blog.service.SysService;
import com.william.blog.service.impl.ArticleServiceImp;
import com.william.blog.service.impl.CategoryServiceImp;
import com.william.blog.service.impl.CommentServiceImp;
import com.william.blog.service.impl.SysServiceImp;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    @Autowired
    SysServiceImp sysService;
    @Autowired
    ArticleServiceImp articleService;
    @Autowired
    CategoryServiceImp categoryService;
    @Autowired
    CommentServiceImp commentService;
}
