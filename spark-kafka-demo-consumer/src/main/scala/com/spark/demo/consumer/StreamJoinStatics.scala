package com.spark.demo.consumer

import com.bigdata.demo.enums.AutoOffsetResetEnum
import com.spark.demo.config.KafkaConf
import com.spark.demo.enums.AutoOffsetResetEnum
import org.apache.spark.sql.SparkSession

object StreamJoinStatics {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("test")
      .master("local")
      .getOrCreate()
    import spark.implicits._

    val staticDF = List(Seq("20"), Seq("30")).toDF("payment_customer_id")
    //val staticDF = spark.sql("select customer_id as payment_customer_id from dw.dim_customer_department_info limit 100")

    val streamDF = spark
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
      .selectExpr("CAST(offset AS BIGINT) as payment_customer_id")

    val df = streamDF.join(staticDF, "payment_customer_id")
    val query = df.writeStream
      .format("console")
      .outputMode("append")
      .start()

    query.awaitTermination()
  }

}