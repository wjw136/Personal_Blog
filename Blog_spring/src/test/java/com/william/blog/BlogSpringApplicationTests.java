package com.william.blog;

import com.william.blog.dao.SysViewMapper;
import com.william.blog.entity.SysView;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import javax.annotation.Resource;
import java.util.Date;

//@RunWith是Junit4提供的注解，将Spring和Junit链接了起来。
//@RunWith(SpringRunner.class)

/*
@SpringBootTest替代了spring-test中的@ContextConfiguration注解，目的是加载ApplicationContext，启动spring容器。
使用@SpringBootTest时并没有像@ContextConfiguration一样显示指定locations或classes属性，原因在于@SpringBootTest注解会自动检索程序的配置文件，检索顺序是从当前包开始，逐级向上查找被@SpringBootApplication或@SpringBootConfiguration注解的类
 */

@SpringBootTest(classes = BlogSpringApplication.class)
class BlogSpringApplicationTests {

//    @Autowired
//    private SysViewMapper sysViewMapper;


    @Test
    void mapperTest() {
//        SysView sysView= new SysView();
//        sysView.setIp("127.0.0.1");
//        sysView.setCreateBy(new Date());
//        sysViewMapper.insert(sysView);

    }

}
