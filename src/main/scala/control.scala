package com.pongr.jhead

import java.io._

object Control {

  def using[A, B <: {def close(): Unit}] (closeable: B) (f: B => A): A =
    try { f(closeable) } finally { closeable.close() }

  def exec(cmdWithArgs: String*)(func : String => Unit) : Unit = {

    val process = new ProcessBuilder(cmdWithArgs: _*).redirectErrorStream(true).start
    val inputReader = new BufferedReader(new InputStreamReader(process.getInputStream))

    val outputReaderThread = new Thread(new Runnable {
      def run : Unit = {
        var ln : String = null
        while({ln = inputReader.readLine; ln != null})
          func(ln)
      }
    })

    outputReaderThread.start()

    process.waitFor

    outputReaderThread.join()

    inputReader.close()

  }
}
