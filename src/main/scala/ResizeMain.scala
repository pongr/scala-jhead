package com.pongr.jhead

import java.io._
import org.apache.commons.io._
import scala.collection.JavaConversions._
import akka.dispatch.Futures

object ResizeMain {
  def main(args: Array[String]) {
    run(args(0))
  }

  def run(dir: String, widths: Seq[Int] = Seq(220, 440, 520, 640, 1040)) {
    for (file <- new File(dir).listFiles; if file.getName.toLowerCase.endsWith(".jpg")) {
      val photo = FileUtils.readFileToByteArray(file)
      val baseName = file.getName.replace(".jpg", "")
      //for ((bytes, width, height) <- Futures.sequence(Convert.resizeToWidths(photo, widths: _*))) {
      for {
        future <- Convert.resizeToWidths(photo, widths: _*)
        (bytes, width, height) <- future
      } {
        FileUtils.writeByteArrayToFile(new File(dir, "%s-%dx%d.jpg" format (baseName, width, height)), bytes)
      }
    }
  }
}