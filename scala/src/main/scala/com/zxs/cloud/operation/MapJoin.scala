package com.zxs.cloud.operation

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.sql.SparkSession

class MyRegistrator extends KryoRegistrator{
  override def registerClasses(kryo: Kryo): Unit = {
    kryo.register(Class.forName("org.apache.hadoop.io.NullWritable",false,getClass.getClassLoader()))
  }
}
class MapJoin {
}
object MapJoin{
  def main(args: Array[String]): Unit = {
    /*val sparkConf: SparkConf = new SparkConf().setAppName("mapjoin").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val fileRead = sc.textFile("C:\\Users\\bill.zheng\\Desktop\\笔记文档\\笔记.txt")
    fileRead.foreach(str => print(str))
    val results = fileRead.flatMap(x => x.split(" ")).map(x => (x, 1)).reduceByKey((x,y) => x + y)
    print(results.foreach(result => println("词是：" + result._1 + "出现次数统计为：" + result._2)))*/

    val sparkSession = SparkSession.builder().appName("HiveCaseJob").master("local[*]").enableHiveSupport().getOrCreate()

    sparkSession.sql("drop table if exists users")
    sparkSession.sql("show tables").show()
    sparkSession.sql("create table if not exists users(id int,name string) row format delimited fields terminated by ' ' stored as textfile")
    sparkSession.sql("show tables").show()
    sparkSession.sql("select * from users").show()
    sparkSession.sql("load data local inpath 'src/main/resources/a.txt' overwrite into table users")
    sparkSession.sql("select * from users").show()

  }

}
