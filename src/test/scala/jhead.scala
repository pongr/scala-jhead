package com.pongr.jhead

import org.specs2.mutable._

import java.io._
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class JHeadSpec extends Specification  {

  "JHeadSpec" should {

    "remove all exif headers" in {
      val jhead = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/canon-ixus.jpg")))

      // all jpg
      jhead.purejpg

      val info = JHead(jhead.getBytes).info._1
      info.gpsInfo must_== GpsInfo(None,None,None)
      info.generalInfo must_== GeneralInfo(None,None,None,Some(640),Some(480),None,None,None,None)
      info.otherInfo must_== OtherInfo(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None)
      info.resolutionInfo must_== ResolutionInfo(None,None,None)
      info.thumbInfo must_== ThumbnailInfo(None,None)
    }

    "rotate and remove exif headers" in {
      val jhead = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/acer.jpg")))

      jhead.info._1.generalInfo.width must_== Some(2560)
      jhead.info._1.generalInfo.height must_== Some(1440)

      val image = jhead.cleanImage

      val rotated = getClass.getResourceAsStream("/acer-rotated-cleaned.jpg")
      IOUtils.contentEquals(new ByteArrayInputStream(image._3), rotated) must_== true

      image._1.generalInfo.width must_== Some(1440)
      image._1.generalInfo.height must_== Some(2560)
    }


    "convert bmp" in {
      val jhead = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/passat.bmp")), convert = true)
      val info = jhead.info._1
      info.generalInfo.width must_== Some(500)
      info.generalInfo.height must_== Some(316)
      IOUtils.contentEquals(jhead.getInputStream, getClass.getResourceAsStream("/passat-bmp.jpg")) must_== true
    }

    "convert gif" in {
      val jhead = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/passat.gif")), convert = true)
      val info = jhead.info._1
      info.generalInfo.width must_== Some(785)
      info.generalInfo.height must_== Some(400)
      IOUtils.contentEquals(jhead.getInputStream, getClass.getResourceAsStream("/passat-gif.jpg")) must_== true
    }

    "convert png" in {
      val jhead = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/passat.png")), convert = true)
      val info = jhead.info._1
      info.generalInfo.width must_== Some(450)
      info.generalInfo.height must_== Some(200)
      IOUtils.contentEquals(jhead.getInputStream, getClass.getResourceAsStream("/passat-png.jpg")) must_== true
    }

    "resize images squres" in {
      val resizer = new ImageMagickResizer
      val bytes = IOUtils.toByteArray(getClass.getResourceAsStream("/mini.jpg"))

      val thumb150 = resizer.downsizeToSquare(bytes, 150)
      val thumb200 = resizer.downsizeToSquare(bytes, 200)
      val thumb425 = resizer.downsizeToSquare(bytes, 425)

      IOUtils.contentEquals(new ByteArrayInputStream(thumb150), getClass.getResourceAsStream("/mini-150x150.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb200), getClass.getResourceAsStream("/mini-200x200.jpg")) must_== true
      IOUtils.contentEquals(new ByteArrayInputStream(thumb425), getClass.getResourceAsStream("/mini-425x425.jpg")) must_== true
    }

    "resize images widths" in {
      val resizer = new ImageMagickResizer
      val bytes = IOUtils.toByteArray(getClass.getResourceAsStream("/acer-rotated-cleaned.jpg"))

      val thumb150 = resizer.downsizeToWidth(bytes, 150)
      val thumb200 = resizer.downsizeToWidth(bytes, 200)
      val thumb425 = resizer.downsizeToWidth(bytes, 425)

      IOUtils.contentEquals(new ByteArrayInputStream(thumb150._1), getClass.getResourceAsStream("/acer-150x267.jpg")) must_== true
      thumb150._2 must_== 267

      IOUtils.contentEquals(new ByteArrayInputStream(thumb200._1), getClass.getResourceAsStream("/acer-200x356.jpg")) must_== true
      thumb200._2 must_== 356

      IOUtils.contentEquals(new ByteArrayInputStream(thumb425._1), getClass.getResourceAsStream("/acer-425x756.jpg")) must_== true
      thumb425._2 must_== 756
    }

  }
}
