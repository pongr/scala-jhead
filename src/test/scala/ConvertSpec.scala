package com.pongr.jhead

import org.specs2.mutable._

import java.io._
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class ConvertSpec extends Specification  {

  "ConvertSpec" should {

    "convert bmp" in {
      val bytes = Convert(IOUtils.toByteArray(getClass.getResourceAsStream("/passat.bmp"))).right.get
      IOUtils.contentEquals(new ByteArrayInputStream(bytes), getClass.getResourceAsStream("/passat-bmp.jpg")) must_== true
    }

    "convert gif" in {
      val bytes = Convert(IOUtils.toByteArray(getClass.getResourceAsStream("/passat.gif"))).right.get
      IOUtils.contentEquals(new ByteArrayInputStream(bytes), getClass.getResourceAsStream("/passat-gif.jpg")) must_== true
    }

    "convert png" in {
      val bytes = Convert(IOUtils.toByteArray(getClass.getResourceAsStream("/passat.png"))).right.get
      IOUtils.contentEquals(new ByteArrayInputStream(bytes), getClass.getResourceAsStream("/passat-png.jpg")) must_== true
    }

    "resize image to square" in {
      val bytes = IOUtils.toByteArray(getClass.getResourceAsStream("/mini.jpg"))

      val thumb150 = Convert.resizeToSquare(bytes, 150)
      val thumb200 = Convert.resizeToSquare(bytes, 200)
      val thumb425 = Convert.resizeToSquare(bytes, 425)

      IOUtils.contentEquals(new ByteArrayInputStream(thumb150), getClass.getResourceAsStream("/mini-150x150.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb200), getClass.getResourceAsStream("/mini-200x200.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb425), getClass.getResourceAsStream("/mini-425x425.jpg")) must_== true
    }

    "resize image width" in {
      val bytes = IOUtils.toByteArray(getClass.getResourceAsStream("/acer-rotated-cleaned.jpg"))

      val thumb150 = Convert.resizeToWidth(bytes, 150)
      val thumb200 = Convert.resizeToWidth(bytes, 200)
      val thumb425 = Convert.resizeToWidth(bytes, 425)

      IOUtils.contentEquals(new ByteArrayInputStream(thumb150), getClass.getResourceAsStream("/acer-150x267.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb200), getClass.getResourceAsStream("/acer-200x356.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb425), getClass.getResourceAsStream("/acer-425x756.jpg")) must_== true
    }

  }
}
