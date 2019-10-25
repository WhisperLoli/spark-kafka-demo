package com.bigdata.demo.web.service.impl;

import com.bigdata.demo.web.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author nengjun.hu
 * @description 写数据到kafka service实现
 * @date 2019-10-22
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Override
    public void saveKey(String key) {
        kafkaTemplate.send("demo", key);
        logger.info("kafka send message success, message is 【" +key+ "】");
    }
}
