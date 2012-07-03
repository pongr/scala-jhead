package com.pongr.jhead

import java.io._

object Control {

  def using[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
    try { f(closeable) } finally { closeable.close() }

  def exec(cmdWithArgs: String*)(func : String => Unit) : Unit = {

    val proc = new ProcessBuilder(cmdWithArgs: _*).redirectErrorStream(true).start
    val ins = new BufferedReader(new InputStreamReader(proc.getInputStream))

    val outputReaderThread = new Thread(new Runnable {
      def run : Unit = {
        var ln : String = null
        while({ln = ins.readLine; ln != null})
          func(ln)
      }
    })

    outputReaderThread.start()

    proc.waitFor

    outputReaderThread.join()

    ins.close()

  }
}
