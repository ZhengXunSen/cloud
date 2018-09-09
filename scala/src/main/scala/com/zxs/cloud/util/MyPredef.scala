package com.zxs.cloud.util

object MyPredef {
  implicit def str2Path(str:String)=new Str2Path(str)
}
