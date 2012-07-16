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

import Util._

trait ImageResizer {

  /** Resize image to be maximum width of specified value, height adjusted to keep aspect ratio constant. Specified width must be greater than image width. 
    * Returns resized image and new height.
    */
  def resizeToWidth(bytes: Array[Byte], width: Int): Array[Byte]

  /** Resize image to be maximum of specified size on each side. If image is portrait then remove top & bottom. If image is landsape then remove left & right.
    * Specified size must be greater than image width and height. Returns resized image.
    */
  def resizeToSquare(bytes: Array[Byte], size: Int): Array[Byte]

}
