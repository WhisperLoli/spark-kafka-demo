package com.spark.demo.web.controller;

import com.spark.demo.web.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nengjun.hu
 * @description controller示范
 * @date 2019-10-21
 */
@RestController
@Scope("prototype")
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private DemoService demoService;

    @RequestMapping(value = "/searchKey", method = {RequestMethod.GET, RequestMethod.POST})
    public void search(String data) {
        logger.info("request message is 【" +data+ "】");
        demoService.saveKey(data);
    }
}
