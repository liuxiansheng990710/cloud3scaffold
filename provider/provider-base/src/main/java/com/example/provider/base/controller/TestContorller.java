package com.example.provider.base.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commons.web.servlet.model.SuperController;
import com.example.commons.web.servlet.response.Wrapper;
import com.example.mysql.provider.stater.mp.properties.MyBatisPlusConfigProperties;
import com.example.provider.base.model.App;
import com.example.provider.base.model.AppServices;
import com.example.provider.base.model.MongoTestService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestContorller extends SuperController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoTestService mongoTestService;

    @Autowired
    private AppServices appServices;

    @Autowired
    private MyBatisPlusConfigProperties configProperties;

    @GetMapping("/v1")
    @Wrapper(wrapper = true, httpStatus = HttpStatus.ACCEPTED)
    public List<String> test(@RequestParam("date") Date date, @RequestParam("name") String name) {
        App app = new App();
        app.setName("21");
        App app1 = appServices.get(name);
        Page<App> page = defaultPage(Page.class);
//        appServices.save(app);
        return Lists.newArrayList("21", "666");
    }

}
