package com.bigdata.demo.enums

/**
  * @author nengjun.hu
  * @description auto.offset.reset值枚举
  * @date 2019-10-24
  */
object AutoOffsetResetEnum extends Enumeration{
  type AutoOffsetResetEnum = Value
  /**
    * 使用新的消费者组时，从头开始消费
    * 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
    */
  val Earliest = Value("earliest")

  /**
    * topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
    */
  val None = Value("none")

  /**
    * 使用新的消费者组时，不会从头开始消费，只消费新生产的数据
    * 当分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
    */
  val Latest = Value("latest")
}
