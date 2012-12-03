package com.pongr.jhead

import org.specs2.mutable._
import java.io.File

class IdentifySpec extends Specification {
  "The Identify wrapper" should {
    "return width and height" in {
      Identify.size(new File("src/test/resources/mini.jpg")) must beSome(2048, 1152)
      Identify.size(new File("src/test/resources/acer.jpg")) must beSome(2560, 1440)
    }

    "handle error" in {
      todo
    }
  }
}