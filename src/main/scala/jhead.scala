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

  def autorot = exec("jhead", "-autorot", file.getAbsolutePath) { _ => Unit }

  def purejpg = exec("jhead", "-purejpg", file.getAbsolutePath) { _ => Unit }

  def info = {
    var result: Seq[String] = Nil
    exec("jhead", "-v", file.getAbsolutePath) { out => if (!isBlank(out)) result +:= out }
    ImageInfo(result)
  }

  def version = {
    var result =  ""
    exec("jhead", "-V") { out => if (!isBlank(out)) result += out + "\n" }
    result.trim
  }

}
