package com.zxs.cloud

import com.zxs.cloud.operation.MapJoin
import org.apache.hadoop.util.ProgramDriver

object JobApplication {
  def main(args: Array[String]): Unit = {
    val driver = new ProgramDriver
    driver.addClass("mapjoin",classOf[MapJoin],"mapjoin任务")
    driver.run(args)
  }
}
