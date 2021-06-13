package com.flink.demo.transform

import com.flink.demo.app.SocketWindowWordCount.WordWithCount
import org.apache.flink.api.common.functions.ReduceFunction

/**
  * 只能对当前窗口中的数据聚合统计，不能使用历史累加
  * keyBy时需要使用reduce统计的key分区计算，由于flink没有reduceByKey函数，需要注意这点
  */
class TextReduceFunction extends ReduceFunction[WordWithCount]{
  override def reduce(value1: WordWithCount, value2: WordWithCount): WordWithCount = {
    WordWithCount(value1.word, value1.count + value2.count)
  }
}
