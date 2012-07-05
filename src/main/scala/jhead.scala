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

}
