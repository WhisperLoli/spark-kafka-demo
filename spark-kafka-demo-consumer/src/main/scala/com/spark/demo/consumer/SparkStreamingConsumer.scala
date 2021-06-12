package com.spark.demo.consumer

import com.bigdata.demo.enums.AutoOffsetResetEnum
import com.spark.demo.config.KafkaConf
import com.spark.demo.enums.AutoOffsetResetEnum
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
/**
  * Direct方式消费kafka消息
  *
  * 区别 Receiver 接收数据，这种方式定期地从 kafka 的 topic+partition 中查询最新的偏移量，再根据偏移量范围在每个 batch 里面处理数据，使用的
  * 是 Kafka 的低级消费者api
  * 优点:
  * A、简化并行，不需要多个 Kafka 输入流，该方法将会创建和 Kafka 分区一样的 rdd 个数，而且会从 Kafka 并行读取。
  * B、高效，这种方式并不需要 WAL，WAL 模式需要对数据复制两次，第一次是被 Kafka 复制，另一次是写到 WAL 中
  * C、恰好一次语义(Exactly-once-semantics)，传统的读取 Kafka 数据是通过 Kafka 高级 api 把偏移量写入 zookeeper 中，存在数据丢失的可能性
  * 是 zookeeper 和 ssc中的偏移量不一致。EOS 通过实现 Kafka 低级 api，偏移量仅仅被 ssc 保存在 checkpoint 中，消除了 zookeeper 和 ssc 偏移
  * 量不一致的问题。
  * 缺点:
  * A、无法使用基于 zookeeper 的 Kafka 监控工具;
  * B、spark中的executor的工作的个数就为kafka中的partition一致，设置再多的executor都不工作;
  */
object SparkStreamingConsumer {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .appName("spark streaming consumer")
      .master("local[*]")
      .getOrCreate()

    val ssc = new StreamingContext(spark.sparkContext, Seconds(KafkaConf.TIME_INTERVAL))

    // 配置基本参数
    val kafkaParams = Map(
      "auto.offset.reset" -> AutoOffsetResetEnum.Earliest.toString,
      "group.id" -> KafkaConf.CUSTOM_GROUP,
      "key.deserializer" -> KafkaConf.KEY_DESERIALIZER,
      "value.deserializer" -> KafkaConf.VALE_DESERIALIZER,
      "bootstrap.servers" -> KafkaConf.BOOTSTRAP_SERVERS,
      "enable.auto.commit" -> false.toString
    )

    // topic组
    val topicsSet = KafkaConf.TOPIC.split(",").toSet

    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))

    messages.foreachRDD(rdd => {
      val lines = rdd.map(_.value())
      val words = lines.flatMap(_.split(" "))
      val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)

      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

      wordCounts.foreach(println(_))
      //处理完睡眠10s
      
      // some time later, after outputs have completed
      messages.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)

    })

    ssc.start()
    ssc.awaitTermination()
  }
}
