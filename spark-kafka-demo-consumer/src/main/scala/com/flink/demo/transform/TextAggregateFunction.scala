package com.flink.demo.transform

import com.flink.demo.app.SocketWindowWordCount.WordWithCount
import org.apache.flink.api.common.functions.AggregateFunction
import scala.collection.mutable.Map

/**
  * 同ReduceFunction,只能实现窗口内聚合，但不需要考虑keyBy的字段
  */
class TextAggregateFunction extends AggregateFunction[WordWithCount, Map[String, Long], Map[String, Long]]{
  override def createAccumulator(): Map[String, Long] = Map()

  override def add(value: WordWithCount, accumulator: Map[String, Long]): Map[String, Long] = {
    val older = accumulator.getOrElse(value.word, 0L)
    val newer  = older + value.count
    accumulator.updated(value.word, newer)
  }

  override def getResult(accumulator: Map[String, Long]): Map[String, Long] = {
    accumulator
  }

  override def merge(a: Map[String, Long], b: Map[String, Long]): Map[String, Long] = {
     a ++ b.map(x => {
       if (a.contains(x._1)) {
         x._1 -> (x._2 + a.get(x._1).get)
       } else {
         x._1 -> x._2
       }
     })
  }
}
