package com.william.blog.controller;

import com.william.blog.entity.SysLog;
import com.william.blog.entity.SysView;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@Controller
@RestController //自动将POJO对象转换成json对象
//AutowiredAnnotationBeanPostProcessor.AutowiredFieldElement.inject会对一个类的本身的字段和其所有父类的字段进行遍历，凡是含有@Autowired的字段都会被注入。
public class SysController  extends BaseController{

//    @RequestMapping("/index")
//    public String index(Model model){
//        model.addAttribute("title", "用户列表");
//        model.addAttribute("hello","Hello, Spring Boot!");
//        List<User> userList = new ArrayList<>();
//        userList.add(new User("小明", 25));
//        userList.add(new User("小黄", 23));
//        model.addAttribute("userList", userList);
//        return "index";
//    }

    @ApiOperation("返回所有的SysLog信息")
    @GetMapping("/sys/log")
    public List<SysLog> listAllLog(){
        return sysService.listAllLog();
    }

    @ApiOperation("返回所有的Sysview的信息")
    @GetMapping("/sys/view")
    public List<SysView> listAllView(){
        return sysService.listAllView();
    }

    @ApiOperation("插入访问记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ip", value = "访问ip", required = true, dataType = "String"),
    })
    @PostMapping("/sys/view")
    public String addView(@RequestParam String ip)
    {
        SysView sysView = new SysView();
        sysView.setIp(ip);
        sysService.addView(sysView);
        return "index";
    }


}

//@Data
//class User {
//
//    private String name;
//    private int age;
//
//    public User(String name, int age) {
//        super();
//        this.name = name;
//        this.age = age;
//    }
//
//}