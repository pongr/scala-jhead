package com.pongr.jhead

import java.io._

import scala.io.Source
import scala.sys.process.{ Process, ProcessIO, ProcessLogger }

object Control {

  def using[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
    try { f(closeable) } finally { closeable.close() }

  def exec(cmdWithArgs: String*)(func : String => Unit) : Unit = {

    val process = Process(cmdWithArgs)

    val pio = new ProcessIO(_ => (),
                            stdout => Source.fromInputStream(stdout).getLines.foreach(func),
                            _ => ())

    // process lines_! ProcessLogger(func)
    process.run(pio)
  }
}
