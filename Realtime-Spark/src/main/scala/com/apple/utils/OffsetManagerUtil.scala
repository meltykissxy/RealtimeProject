package com.apple.utils

import java.util

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange

object OffsetManagerUtil {
  /*  读取redis中的偏移量*/
  def getOffset(topic: String, groupid: String): Map[TopicPartition,Long] = {
    val jedis = RedisUtil.getJedisClient
    val offsetKey = topic + ":" + groupid
    val offsetMapOrigin: util.Map[String, String] = jedis.hgetAll(offsetKey)
    jedis.close()

    if (offsetMapOrigin != null && offsetMapOrigin.size() > 0) {
      import collection.JavaConverters._
      // 转换结构把从redis中取出的结构 转换成 kafka要求的结构
      val offsetMapForKafka = offsetMapOrigin.asScala.map {
        case (partition, offset) => {
          val topicPartition = new TopicPartition(topic, partition.toInt)
          (topicPartition, offset.toLong)
        }
      }.toMap
      offsetMapForKafka
    } else {
      null
    }
  }
  //把偏移量写入redis
  /**
   * class OffsetRange private(
    val topic: String,
    val partition: Int,
    val fromOffset: Long,
    val untilOffset: Long)
   */
  def saveOffset(topic: String, groupId: String, offsetRanges: Array[OffsetRange]) = {
    val jedis = RedisUtil.getJedisClient
    val offsetKey = topic + ":" + groupId
    val offsetMapForRedis = new util.HashMap[String, String]()
    for (offsetRange <- offsetRanges) {
      val partition = offsetRange.partition
      val offset = offsetRange.untilOffset
      offsetMapForRedis.put(partition.toString, offset.toString)
    }
    jedis.hmset(offsetKey, offsetMapForRedis)
    jedis.close()
  }
}
