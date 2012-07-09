package com.pongr.jhead

import org.specs2.mutable._

import java.io._
import org.apache.commons.io.IOUtils

class ImageResizerSpec extends Specification  {

  "ImageResizerSpec" should {

    "resize image to square" in {
      val resizer = new ImageMagickResizer
      val bytes = IOUtils.toByteArray(getClass.getResourceAsStream("/mini.jpg"))

      val thumb150 = resizer.resizeToSquare(bytes, 150)
      val thumb200 = resizer.resizeToSquare(bytes, 200)
      val thumb425 = resizer.resizeToSquare(bytes, 425)

      IOUtils.contentEquals(new ByteArrayInputStream(thumb150), getClass.getResourceAsStream("/mini-150x150.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb200), getClass.getResourceAsStream("/mini-200x200.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb425), getClass.getResourceAsStream("/mini-425x425.jpg")) must_== true
    }

    "resize image width" in {
      val resizer = new ImageMagickResizer
      val bytes = IOUtils.toByteArray(getClass.getResourceAsStream("/acer-rotated-cleaned.jpg"))

      val thumb150 = resizer.resizeToWidth(bytes, 150)
      val thumb200 = resizer.resizeToWidth(bytes, 200)
      val thumb425 = resizer.resizeToWidth(bytes, 425)

      IOUtils.contentEquals(new ByteArrayInputStream(thumb150._1), getClass.getResourceAsStream("/acer-150x267.jpg")) must_== true
      thumb150._2 must_== 267

      IOUtils.contentEquals(new ByteArrayInputStream(thumb200._1), getClass.getResourceAsStream("/acer-200x356.jpg")) must_== true
      thumb200._2 must_== 356

      IOUtils.contentEquals(new ByteArrayInputStream(thumb425._1), getClass.getResourceAsStream("/acer-425x756.jpg")) must_== true
      thumb425._2 must_== 756
    }

  }
}
