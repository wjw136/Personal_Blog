package com.william.blog.service.impl;

import com.william.blog.dao.SysLogMapper;
import com.william.blog.dao.SysViewMapper;
import com.william.blog.entity.SysLog;
import com.william.blog.entity.SysLogExample;
import com.william.blog.entity.SysView;
import com.william.blog.entity.SysViewExample;
import com.william.blog.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class SysServiceImp implements SysService {

    @Autowired
    SysViewMapper sysViewMapper;
    @Autowired
    SysLogMapper sysLogMapper;

    @Override
    public void addLog(SysLog sysLog) {
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void addView(SysView sysview) {
        sysViewMapper.insertSelective(sysview);
    }

    @Override
    public int getLogCount() {
        return 0;
    }

    @Override
    public int getViewCount() {
        return 0;
    }

    @Override
    public List<SysLog> listAllLog() {
        SysLogExample example=new SysLogExample();
        return sysLogMapper.selectByExample(example);
    }

    @Override
    public List<SysView> listAllView() {
       SysViewExample example= new SysViewExample();
       return sysViewMapper.selectByExample(example);
    }
}
