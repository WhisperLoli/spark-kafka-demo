package com.bigdata.demo.consumer

import com.bigdata.demo.config.KafkaConf
import com.bigdata.demo.enums.AutoOffsetResetEnum
import org.apache.spark.sql.SparkSession

/**
  * @author nengjun.hu
  * @date 2019-10-30
  * @description 体验下structured streaming, high level api
  */
object StructuredStreamingConsumer {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("structured streaming consumer")
      .getOrCreate()

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", KafkaConf.BOOTSTRAP_SERVERS)
      .option("subscribe", KafkaConf.TOPIC)
      .option("group.id", KafkaConf.CUSTOM_GROUP)
      .option("key.deserializer", KafkaConf.KEY_DESERIALIZER)
      .option("value.deserializer", KafkaConf.VALE_DESERIALIZER)
      .option("enable.auto.commit", true)
      .option("auto.offset.reset", AutoOffsetResetEnum.Earliest.toString)
      .load()

    val result = df.writeStream
      .outputMode("append")
      .format("console")
      .start()

    result.awaitTermination()


  }
}
