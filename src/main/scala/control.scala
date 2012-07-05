package com.pongr.jhead

import java.io._

import scala.io.Source
import scala.sys.process.{ Process, ProcessIO, ProcessLogger }

object Control {

  def using[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
    try { f(closeable) } finally { closeable.close() }


  def exec(cmdWithArgs: String*): Either[Seq[String], Seq[String]] = {

    val process = Process(cmdWithArgs)

    var errorLines: Seq[String] = Nil
    var normalLines: Seq[String] = Nil

    process ! ProcessLogger (
      line => normalLines +:= line,
      line => errorLines  +:= line
    )

    if (errorLines.isEmpty) Left(normalLines) else Right(errorLines)
  }
}
