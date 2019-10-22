package com.bigdata.demo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nengjun.hu
 * @description controller示范
 * @date 2019-10-21
 */
@RestController
public class DemoController {

    private static Logger logger = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping(value = "/searchKey", method = {RequestMethod.GET, RequestMethod.POST})
    public void search(String data) {
        logger.info("hello");
    }
}
