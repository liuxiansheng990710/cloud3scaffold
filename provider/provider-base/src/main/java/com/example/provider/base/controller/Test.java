package com.example.provider.base.controller;

import java.util.Date;

import com.alibaba.fastjson2.JSONObject;
import com.example.quartz.provider.stater.support.IExecuteQuartzJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class Test implements IExecuteQuartzJob {

    @Override
    public void execute(Long jobId, JSONObject parm) {
      log.info("打印" + new Date());
    }
}
