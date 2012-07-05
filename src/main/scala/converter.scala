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

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Parses jhead output and gets the appropriate scala data type for the given pattern.
 */
class Parser(info: Seq[String]) {

  def getString(name: String, separator: String = ": "): Option[String] = {
    val r = ("^" + name + """\s*""" + separator).r
    info.filter (e => !r.findFirstIn(e.trim).isEmpty).headOption match {
      case Some(value) => Some(value.split(separator)(1).trim)
      case _ => None
    }
  }

  def getInt(name: String, ind: Int = 0, separator: String= ": "): Option[Int] =
    getString(name, separator) match {
      case Some(value) =>
        val ints = "[0-9]+".r.findAllIn(value).toList
        try { Some(ints(ind).toInt) } catch { case e => None }
      case _ => None
    }

  def getDateTime(name: String): Option[DateTime] =
    getString(name) match {
      case Some(d) =>
        val formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss")
        Some(formatter.parseDateTime(d))
      case _ => None
    }

  def getBoolean(name: String): Option[Boolean] =
    getString(name) match {
      case Some(v) if (v.toLowerCase == "yes" || v.toLowerCase == "true") => Some(true)
      case Some(v) if (v.toLowerCase == "no" || v.toLowerCase == "false") => Some(false)
      case _ => None
    }

}
