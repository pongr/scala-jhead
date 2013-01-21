package com.pongr.jhead

import Util._
import java.io.File
import grizzled.slf4j.Logging

/** Wrapper around the [[http://www.imagemagick.org/script/identify.php identify]] tool. */
object Identify extends Logging {
  //acer.jpg JPEG 2560x1440 2560x1440+0+0 8-bit DirectClass 599KB 0.000u 0:00.000
  //mini.jpg JPEG 2048x1152 2048x1152+0+0 8-bit DirectClass 572KB 0.000u 0:00.000
  val size = """\s+(\d+)x(\d+)\s+""".r

  /** Returns the width and height of the specified image file, or an error. */
  def size(file: File): Either[String, (Int, Int)] = {
    val (out, errors) = exec("identify", file.getAbsolutePath)
    size findFirstIn out.mkString("\n") match {
      case Some(size(width, height)) => Right((width.toInt, height.toInt))
      case None => 
        error("Unable to get width/height of %s: out=%s, errors=%s %s" format (
          file.getAbsolutePath, 
          out.mkString("\n"), 
          errors.mkString("\n"),
          Thread.currentThread.getStackTrace.mkString("\n", "\n", "")
        ))
        Left(errors.mkString("\n"))
    }
  }

  //TODO use in-memory pipes
  /** Returns the width and height of the specified image, or an error. */
  def size(photo: Array[Byte]): Either[String, (Int, Int)] = size(createTempFile(photo))
}