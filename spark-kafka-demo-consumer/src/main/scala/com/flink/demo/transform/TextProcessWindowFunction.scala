package com.flink.demo.transform

import com.flink.demo.app.SocketWindowWordCount.WordWithCount
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor, StateTtlConfig}
import org.apache.flink.api.common.time.Time
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * 窗口函数聚合，可以使用历史状态累加，也可以只计算当前窗口中的结果
  */
class TextProcessWindowFunction extends ProcessWindowFunction[WordWithCount, (String, Long), String, TimeWindow]{
  var result: MapState[String, Long] = _
  override def process(key: String, context: Context, elements: Iterable[WordWithCount], out: Collector[(String, Long)]): Unit = {
    val res = elements
      .map(x => (x.word, x.count))
      .groupBy(_._1)
      .mapValues(x => x.map(_._2).sum)
        .map(x => {
          // 累加历史所有相同key的数据，如果不累加，则为当前窗口中累加的结果
          if (result.contains(x._1)) {
            (x._1, x._2 + result.get((x._1)))
          } else {
            (x._1, x._2)
          }
        })

    res.foreach(x => {
      out.collect(x)
      result.put(x._1, x._2)
    })
  }

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    val mapStateDescriptor =
      new MapStateDescriptor[String, Long]("result", classOf[String], classOf[Long])

    mapStateDescriptor.enableTimeToLive(
      StateTtlConfig.newBuilder(Time.days(30))
        .updateTtlOnCreateAndWrite()
        .neverReturnExpired()
        .build()
    )

    result = getRuntimeContext.getMapState(mapStateDescriptor)
  }
}
