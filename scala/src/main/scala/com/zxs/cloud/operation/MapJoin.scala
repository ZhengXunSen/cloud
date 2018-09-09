package com.zxs.cloud.operation

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.{KryoRegistrator, KryoSerializer}
import org.apache.spark.{SparkConf, SparkContext}
class MyRegistrator extends KryoRegistrator{
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(Class.forName("org.apache.hadoop.io.NullWritable",false,getClass.getClassLoader()))
  }
}
class MapJoin {
}
object MapJoin{
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("mapjoin").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val fileRead = sc.textFile("C:\\Users\\lenovo\\Desktop\\笔记\\nexus docker安装命令.txt")
    fileRead.foreach(str => print(str))
    val results = fileRead.flatMap(x => x.split(" ")).map(x => (x, 1)).reduceByKey((x,y) => x + y)
    print(results.foreach(result => println("词是：" + result._1 + "出现次数统计为：" + result._2)))
  }
}
