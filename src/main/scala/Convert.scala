/*
 * Copyright (c) 2012 Pongr, Inc.
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pongr.jhead

import java.io._
import org.apache.commons.io._
import org.apache.commons.lang.StringUtils.isBlank
import akka.dispatch.{Future, ExecutionContext}
import java.util.concurrent.Executors

import Util._

/** Resize and convert images using [[http://www.imagemagick.org/script/convert.php ImageMagick convert tool]]. */
object Convert extends ImageResizer {

  //TODO use pipes instead of files to communicate with convert

  def apply(bytes: Array[Byte]): Either[Seq[String], Array[Byte]] = {
    val file = createTempFile(bytes)
    val result = exec("convert", file.getAbsolutePath, file.getAbsolutePath)
    if (result._2.size > 0) Left(result._2) else Right(getBytes(file))
  }

  def resizeToWidths(bytes: Array[Byte], widths: Int*): Seq[Future[(Array[Byte], Int, Int)]] = {
    val file = createTempFile(bytes)
    implicit val context = ExecutionContext.fromExecutor(Executors.newCachedThreadPool()) //will this leak?
    widths.toSeq.map { width => 
      Future { 
        val thumbFile = File.createTempFile("image-%s-" format width, ".jpg")
        runConvert(file, thumbFile, width, 90)
        //val bytes = IOUtils.toByteArray(new FileInputStream(thumbFile))
        val bytes = getBytes(thumbFile)
        val (w, h) = Identify.size(thumbFile).right getOrElse (-1, -1) //dangerous...
        (bytes, w, h)
      }
    }
  }

  def runConvert(file: File, thumbFile: File, width: Int, quality: Int = 90) {
    exec("convert", "-thumbnail", "%sx" format width,
                    "-gravity", "center",
                    "-quality", quality.toString,
                    "-interpolate", "bicubic",
                    "+repage",
                    file.getAbsolutePath, thumbFile.getAbsolutePath)
  }

  def resizeToWidth(bytes: Array[Byte], width: Int): Array[Byte] = {
    val file = createTempFile(bytes)
    val thumbFile = File.createTempFile("image-%s-" format width, ".jpg")
    runConvert(file, thumbFile, width)
    //IOUtils.toByteArray(new FileInputStream(thumbFile))
    getBytes(thumbFile)
  }
  
  def resizeToSquare(bytes: Array[Byte], size: Int): Array[Byte] = {
    val file = createTempFile(bytes)
    val thumbGeometry = "%sx%s^" format (size, size)
    val cropGeometry  = "%sx%s+0+0" format (size, size)

    val thumbFile = File.createTempFile("image-%sx%s-" format (size, size), ".jpg")
    exec("convert", "-thumbnail", thumbGeometry,
                    "-crop", cropGeometry,
                    "-gravity", "center",
                    "-quality", "90",
                    "-interpolate", "bicubic",
                    "+repage",
                    file.getAbsolutePath, thumbFile.getAbsolutePath)
    //IOUtils.toByteArray(new FileInputStream(thumbFile))
    getBytes(thumbFile)
  }

}
