package com.zxs.cloud.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.io.{NullWritable, Writable}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class ReadOrc {
}

object ReadOrc {
  /*def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("readorc")
    val sc = new SparkContext(conf)
    val hadoopConf = new Configuration
    //    val path = "D:\\tmp\\input\\orcFile"
    val path = "D:\\tmp\\outputTotal\\part-r-00000"
    val fileRDD: RDD[(NullWritable, OrcStruct)] = sc.newAPIHadoopFile(path, classOf[OrcNewInputFormat], classOf[NullWritable], classOf[OrcStruct], hadoopConf)
    val value: RDD[String] = fileRDD.mapPartitions(f => {
      val orcUtil = new ORCUtil
      orcUtil.setORCtype("struct<country:string,countryname:string>")
      val r1 = mutable.ListBuffer[String]()
      f.foreach(ff => {
        orcUtil.setRecord(ff._2)
        val countryCode: String = orcUtil.getData("country")
        print(countryCode)
        val countryName: String = orcUtil.getData("countryname")
        println(countryName)
      })
      r1.toIterator
    }
    )*/
    //    val value: RDD[String] = fileRDD.mapPartitions(f => {
    //      ListBuffer[String]().iterator
    //    })
    /*val result: RDD[String] = value
    println(value.count())*/
}
