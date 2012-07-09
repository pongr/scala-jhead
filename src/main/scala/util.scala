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

import scala.io.Source
import scala.sys.process.{ Process, ProcessIO, ProcessLogger }

object Util {

  def using[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
    try { f(closeable) } finally { closeable.close() }


  /**
   * Executes external processes and returns the normal output and error output
   */
  def exec(cmdWithArgs: String*): (Seq[String], Seq[String]) = {

    val process = Process(cmdWithArgs)

    var errorLines: Seq[String] = Nil
    var normalLines: Seq[String] = Nil

    process ! ProcessLogger (
      line => normalLines +:= line,
      line => errorLines  +:= line
    )

    (normalLines, errorLines)
  }

  def createFile(bytes: Array[Byte]) = {
    val file = File.createTempFile("image", ".jpg")
    using (new ByteArrayInputStream(bytes)) { input =>
      using (new FileOutputStream(file)) { output =>
        IOUtils.copy(input, output)
      }
    }
    file
  }
}
