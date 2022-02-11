package com.william.blog.interceptor;

import com.william.blog.dto.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BackInterceptor implements HandlerInterceptor {
    private static String username = "wjwilliam";
    private static String password = "123456";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = true;
//        System.out.println("进入成功！");
        //getsession获取sessionid 从而定位储存在server的session
        //默认失效时间是30min
        // 客户端 会保留cokkie sessionid
        //Access-Control-Allow-Credentials 这个头的作用=>可以携带允许的头，还可以携带验证信息的头
        //sessionId 存储在浏览器的cokkie中 默认的expire=session 当浏览器全部关闭后重置
        User user = (User) request.getSession().getAttribute("user");
        if (null == user) {
            // 如果用户为空则跳转到error页面
            // 2018年6月29日20:23:05：修改sendRedirect方法为getRequestDispatcher，
            // 目的是保证地址栏不改变（与前台错误页面响应一致），这样用户就不知道后台页面的地址=> 转发
            request.getRequestDispatcher(request.getContextPath() + "/page/login").forward(request, response);
//            response.sendRedirect(request.getContextPath() + "/error.html");
            flag = false;
        } else {
            // 对用户账号进行验证,是否正确
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
//                System.out.println("访问后台API");
                flag = true;
            } else {
                request.getRequestDispatcher(request.getContextPath() + "/page/login").forward(request, response);

                flag = false;
            }
        }
        return flag;
    }
}
