package com.pongr.jhead

import Util._
import java.io.File

object Identify {
  //acer.jpg JPEG 2560x1440 2560x1440+0+0 8-bit DirectClass 599KB 0.000u 0:00.000
  //mini.jpg JPEG 2048x1152 2048x1152+0+0 8-bit DirectClass 572KB 0.000u 0:00.000
  val size = """\s+(\d+)x(\d+)\s+""".r
  def size(file: File): Option[(Int, Int)] = {
    val (out, errors) = exec("identify", file.getAbsolutePath)
    size findFirstIn out.mkString("\n") match {
      case Some(size(width, height)) => Some((width.toInt, height.toInt))
      case None => None //TODO log file, out & errors
    }
  }
}