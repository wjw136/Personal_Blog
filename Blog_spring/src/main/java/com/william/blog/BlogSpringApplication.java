package com.william.blog;

import com.william.blog.dao.SysViewMapper;
import com.william.blog.entity.SysView;
import org.apache.catalina.core.ApplicationContext;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
/*
SpringBoot的注解扫描的默认规则是从SpringBoot的项目入口类。若入口类所在的包是com.example.demo那么自动扫描包的范围是com.example.demo包及其下面的子包，
 */
@SpringBootApplication
// Mapper因为是interface+没有注解 无法被扫描到
//MapperScan: 指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
//@Mapper注解： 作用：在接口类上添加了@Mapper，在编译之后会生成相应的接口实现类
@MapperScan("com.william.blog.dao")
public class BlogSpringApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BlogSpringApplication.class);
        //Banner.Mode.OFF 关闭
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);

    }

}
