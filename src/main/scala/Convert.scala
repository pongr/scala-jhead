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

import Util._

/** Resize and convert images using [[http://www.imagemagick.org/script/convert.php ImageMagick convert tool]]. */
object Convert extends ImageResizer {

  def apply(bytes: Array[Byte]): Either[Seq[String], Array[Byte]] = {
    val file = createTempFile(bytes)
    val result = exec("convert", file.getAbsolutePath, file.getAbsolutePath)
    if (result._2.size > 0) Left(result._2) else Right(getBytes(file))
  }

  def resizeToWidth(bytes: Array[Byte], width: Int): Array[Byte] = {
    val file = createTempFile(bytes)
    val thumbFile = File.createTempFile("image-%s-" format width, ".jpg")
    exec("convert", "-thumbnail", "%sx" format width,
                    "-gravity", "center",
                    "-quality", "90",
                    "-interpolate", "bicubic",
                    "+repage",
                    file.getAbsolutePath, thumbFile.getAbsolutePath)

    IOUtils.toByteArray(new FileInputStream(thumbFile))
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
    IOUtils.toByteArray(new FileInputStream(thumbFile))
  }

}
