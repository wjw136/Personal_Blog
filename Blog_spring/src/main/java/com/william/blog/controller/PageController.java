package com.william.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {

    @GetMapping("/login")
    public String loginPage(){
        return "/login";
    }

    @GetMapping("/index")
    public String indexPage(){
        return "/index";
    }

    @GetMapping("/article")
    public String articlePage(){
        return "/article";
    }

    @GetMapping("/comment")
    public String commentPage(){
        return "/comment";
    }

    @GetMapping("/category")
    public String categoryPage(){
        return "/category";
    }
}
