package com.pongr.jhead

import java.io._
import org.apache.commons.io._
import org.apache.commons.lang.StringUtils.isBlank

import Control._

object JHead {
  def apply(bytes: Array[Byte]) = {
    val file = File.createTempFile("image", ".jpg")

    using (new ByteArrayInputStream(bytes)) { input =>
      using (new FileOutputStream(file)) { output =>
        IOUtils.copy(input, output)
      }
    }
    new JHead(file)
  }
}

case class JHead(file: File) {

  def getBytes: Array[Byte] = {
    using (new FileInputStream(file)) { output =>
      IOUtils.toByteArray(output)
    }
  }

  def autorot = exec("jhead", "-autorot", file.getAbsolutePath)

  def purejpg = exec("jhead", "-purejpg", file.getAbsolutePath)

  def info: (ImageInfo, Seq[String])  = {
    val result = exec("jhead", "-v", file.getAbsolutePath)
    (ImageInfo(result._1), result._2)
  }


  /**
   * Auto rotates the image and removes all exif headers.
   * Returns ImageInfo with error messages and modified bytes.
   */
  def cleanImage : (ImageInfo, Seq[String], Array[Byte]) = {
    val result = exec("jhead", "-v", "-autorot", "-purejpg", file.getAbsolutePath)
    (ImageInfo(result._1), result._2, getBytes)
  }

}
