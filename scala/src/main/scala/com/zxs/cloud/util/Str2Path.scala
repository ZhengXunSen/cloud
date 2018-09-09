package com.zxs.cloud.util

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

class Str2Path(val path:String){
  def deletePath(): Unit ={
    val conf = new Configuration()
    val fs: FileSystem = FileSystem.get(conf)
    val p1 = new Path(path)
    if(fs.exists(p1)){
      fs.delete(p1,true)
    }
  }
}
