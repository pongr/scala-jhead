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

import Control._

object JHead {

  def apply(bytes: Array[Byte], convert: Boolean = false): JHead = {
    val file = File.createTempFile("image", ".jpg")

    using (new ByteArrayInputStream(bytes)) { input =>
      using (new FileOutputStream(file)) { output =>
        IOUtils.copy(input, output)
      }
    }

    /**
     * Convert non jpeg image to jpeg image using imagemagick convert.
     */
    if (convert) exec("convert", file.getAbsolutePath, file.getAbsolutePath)

    new JHead(file)
  }


}

class JHead(file: File) {

  /**
   * Returns the modified bytes.
   */
  def getBytes: Array[Byte] = {
    using (new FileInputStream(file)) { output =>
      IOUtils.toByteArray(output)
    }
  }

  /**
   * Returns the modified bytes as InputStream
   */
  def getInputStream: InputStream = new ByteArrayInputStream(getBytes)


  /**
   * Using the 'Orientation' tag of the Exif header, rotate the image so that it is upright.
   * Executes "jhead -autorot"
   */
  def autorot = exec("jhead", "-autorot", file.getAbsolutePath)

  /**
   * Delete all JPEG sections that aren't necessary for rendering the image.
   * Executes "jhead -purejpg"
   */
  def purejpg = exec("jhead", "-purejpg", file.getAbsolutePath)

  
  /**
   * Returns ImageInfo with error messages.
   */
  def info: (ImageInfo, Seq[String]) = {
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
  
  /** Resize image to be maximum width of specified value, height adjusted to keep aspect ratio constant. Specified width must be greater than image width. 
    * Returns resized image and new height.
    */
  def downsizeToWidth(width: Int): (Array[Byte], Int) = null
  
  
  /** Resize image to be maximum of specified size on each side. If image is portrait then remove top & bottom. If image is landsape then remove left & right. 
    * Specified size must be greater than image width and height. Returns resized image.
    */
  def downsizeToSquare(size: Int): Array[Byte] = null


  /**
   * Generate thumbnails for the image
   */
  def generateThumbnail(width: Int, height: Int): Array[Byte] = {
    val cropGeometry  = "%sx%s+0+0" format (width, height)
    val thumbGeometry = "%sx%s^" format (width, height)
    val thumbFile = File.createTempFile("thumb-%sx%s-" format (width, height), ".jpg")
    exec("convert", "-thumbnail", thumbGeometry,
                    "-gravity", "center",
                    "-quality", "90",
                    "-crop", cropGeometry,
                    "-interpolate", "bicubic",
                    "+repage",
                    file.getAbsolutePath, thumbFile.getAbsolutePath)
    IOUtils.toByteArray(new FileInputStream(thumbFile))
  }

}
