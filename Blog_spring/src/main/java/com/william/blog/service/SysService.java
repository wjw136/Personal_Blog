package com.william.blog.service;

import com.william.blog.entity.SysLog;
import com.william.blog.entity.SysView;

import java.util.List;

/*
日志/访问统计的相关Service
 */
public interface SysService {
    void addLog(SysLog sysLog);
    void addView(SysView sysview);
    int getLogCount();
    int getViewCount();
    List<SysLog> listAllLog();
    List<SysView> listAllView();
}
