package com.zxs.cloud.operation

import scala.math.random

import org.apache.spark._

/**
  * Created by bill.zheng in 2018/10/8
  */
object SparkPi {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark Pi")
      .setMaster("spark://zxs-1:7077")
      .setExecutorEnv("spark.executor.memory", "100m")

    val spark = new SparkContext(conf)
    val slices = if (args.length > 0) args(0).toInt else 2
    val n = 100000 * slices
    val count = spark.parallelize(1 to n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / n)
    spark.stop()
  }
}
