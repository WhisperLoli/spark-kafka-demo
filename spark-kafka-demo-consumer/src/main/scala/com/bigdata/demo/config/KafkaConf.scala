package com.bigdata.demo.config

/**
  * @author nengjun.hu
  * @description kafka相关配置信息
  * @date 2019-10-24
  */
object KafkaConf {
  /**
    * brokers节点，IP:PORT,IP:PORT...
    */
  val KAFKA_BROKER = "127.0.0.1:2081"

  /**
    * 消费主题
    */
  val TOPIC = "test"

  /**
    * 消费者组
    */
  val CUSTOM_GROUP = "testGroup"

  /**
    * 周期读取kafka时间间隔
    */
  val TIME_INTERVAL = 10

  /**
    * key反序列化
    */
  val KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringSerializer"

  /**
    * value反序列化
    */
  val VALE_DESERIALIZER = "org.apache.kafka.common.serialization.StringSerializer"
}
