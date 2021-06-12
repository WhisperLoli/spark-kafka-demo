package com.spark.demo.config

/**
  * @author nengjun.hu
  * @description kafka相关配置信息
  * @date 2019-10-24
  */
object KafkaConf {
  /**
    * brokers节点，IP:PORT,IP:PORT...
    */
  val KAFKA_BROKER = "127.0.0.1:9092"

  /**
    * servers节点信息
    */
  val BOOTSTRAP_SERVERS = "127.0.0.1:9092"

  /**
    * 消费主题
    */
  val TOPIC = "demo"

  /**
    * 消费者组
    */
  val CUSTOM_GROUP = "testGroup_hnj124"

  /**
    * 周期读取kafka时间间隔
    */
  val TIME_INTERVAL = 20

  /**
    * key反序列化
    */
  val KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer"

  /**
    * value反序列化
    */
  val VALE_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer"
}
