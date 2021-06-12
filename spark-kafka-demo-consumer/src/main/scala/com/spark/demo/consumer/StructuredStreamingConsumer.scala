package com.spark.demo.consumer

import com.spark.demo.config.KafkaConf
import com.spark.demo.enums.AutoOffsetResetEnum
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{StringType, StructType}

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

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._

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

    val schema = new StructType().add("value", StringType, true)

    df.select($"value".cast(StringType).alias("value"), $"timestamp")
      .withWatermark("timestamp","1 minute")
      .groupBy(window($"timestamp", "1 minutes", "1 minutes"), $"value")
      .count()
      .writeStream
      .outputMode(OutputMode.Complete())
      .format("console")
      .start()
      .awaitTermination()
  }
}
