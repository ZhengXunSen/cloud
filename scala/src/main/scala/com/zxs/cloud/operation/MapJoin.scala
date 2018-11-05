package com.zxs.cloud.operation

import com.esotericsoftware.kryo.Kryo
import org.apache.spark.serializer.KryoRegistrator
import org.apache.spark.sql.SparkSession

case class User(id:Int,name:String)
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

    val sparkSession = SparkSession.builder().appName("HiveCaseJob").master("local[2]").enableHiveSupport().getOrCreate()
    val sc = sparkSession.sparkContext
    val fileRead = sc.textFile("hdfs://zxs-1:9000/app/hadoop-3.1.0/dataDir/hdfs/data/hdfs-site.xml")
    fileRead.foreach(str => print(str))
    val results = fileRead.flatMap(x => x.split(" ")).map(x => (x, 1)).reduceByKey((x,y) => x + y)
    print(results.foreach(result => println("词是：" + result._1 + "出现次数统计为：" + result._2)))
    /*val data = sparkSession.sql("select * from default.test").rdd;
    val userRdd: RDD[User] = data.map(row => User(row.get(0).asInstanceOf[Int], row.get(1).toString))
    import sparkSession.implicits._
    val userDF: DataFrame = userRdd.toDF
//    userDF.createOrReplaceTempView("userTemp")
    val prop =new Properties()
    prop.setProperty("user","zxs")
    prop.setProperty("password","jfz123456")
    userDF.write.mode("append").jdbc("jdbc:mysql://zxs-1:3306/dc","user",prop)*/

  }

}
