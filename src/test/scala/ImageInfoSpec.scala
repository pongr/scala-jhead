package com.pongr.jhead

import org.specs2.mutable._

import java.io._
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class jheadInfoSpec extends Specification  {

  def getDateTime(s: String) = {
    val formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss")
    formatter.parseDateTime(s)
  }

  "EmailSpec test" should {

    "extract EXIF headers from canon-ixus" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/canon-ixus.jpg"))).cleanImage

      image._2 must_== Nil

      val info = image._1
      info.fileInfo.fileSize must_== Some(128037)
      info.gpsInfo must_== GpsInfo(None, None, None)
      info.generalInfo must_== GeneralInfo(Some("Canon"),
                                           Some("Canon DIGITAL IXUS"),
                                           Some(getDateTime("2001:06:09 15:17:32")),
                                           Some(640), Some(480),
                                           Some(1),
                                           None, None,
                                           Some(0))
      info.otherInfo must_== OtherInfo(Some("346/32"), // focal length
                                       Some("1/350"), // exposure time
                                       Some("262144/65536"), // apertureValue
                                       Some("3750/1000"), // subject distance
                                       Some("5.23mm"), // ccd width
                                       Some("0/3"), // exposure bias
                                       None, // Digital zoom
                                       None, // focallength35mmEquiv
                                       None, // white balance
                                       Some("2"),
                                       None, // exposure program
                                       None, // exposure mode
                                       None, // iSOequivalent
                                       None, // light source
                                       None, // distant range
                                       Some("\"?\""))
      info.resolutionInfo must_== ResolutionInfo(Some("180/1"), Some("180/1"), Some("2"))
      info.thumbInfo must_== ThumbnailInfo(Some(1524), Some(5342))
    }

    "extract EXIF headers from fujifilm-dx10" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/fujifilm-dx10.jpg"))).cleanImage
      image._2 must_== Nil

      val info = image._1

      info.fileInfo.fileSize must_== Some(133074)
      info.gpsInfo must_== GpsInfo(None, None, None)
      info.generalInfo must_== GeneralInfo(Some("FUJIFILM"),
                                           Some("DX-10"),
                                           Some(getDateTime("2001:04:12 20:33:14")),
                                           Some(1024), Some(768),
                                           Some(1),
                                           None, None,
                                           Some(1))
      info.otherInfo must_== OtherInfo(Some("58/10"),
                                       None,
                                       Some("41/10"),
                                       None,
                                       Some("4.76mm"),
                                       Some("0/10"),
                                       None, None, None,
                                       Some("5"),
                                       Some("2"),
                                       None,
                                       Some("150"),
                                       None,
                                       None,
                                       None)
      info.resolutionInfo must_== ResolutionInfo(Some("72/1"), Some("72/1"), Some("2"))
      info.thumbInfo must_== ThumbnailInfo(Some(856),Some(10274))
    }

    "extract EXIF headers from fujifilm-finepix40i" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/fujifilm-finepix40i.jpg"))).cleanImage

      image._2 must_== Nil

      val info = image._1
      info.fileInfo.fileSize must_== Some(43183)
      info.gpsInfo must_== GpsInfo(None, None, None)
      info.generalInfo must_== GeneralInfo(Some("FUJIFILM"),
                                           Some("FinePix40i"),
                                           Some(getDateTime("2000:08:04 18:22:57")),
                                           Some(600),
                                           Some(450),
                                           Some(1),
                                           None,
                                           None,
                                           Some(1))
      info.otherInfo must_== OtherInfo(Some("870/100"),
                                       None,
                                       Some("300/100"),
                                       None,
                                       Some("10.08mm"),
                                       Some("0/100"),
                                       None,
                                       None,
                                       None,
                                       Some("5"),
                                       Some("2"),
                                       None,
                                       Some("200"),
                                       None,
                                       None,
                                       None)
      info.resolutionInfo must_== ResolutionInfo(Some("72/1"),Some("72/1"),Some("2"))
      info.thumbInfo must_== ThumbnailInfo(Some(1074),Some(8691))
    }

    "extract GpsInfo headers" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/keyboard.jpg"))).cleanImage
      image._2 must_== Nil
      val info = image._1
      info.gpsInfo must_== GpsInfo(Some("N 41d 35m 12.01s"), Some("W 93d 37m 35.52s"), Some("348.00m"))

      info.gpsInfo.latitude must_== Some(41.586669444444446)
      info.gpsInfo.longitude must_== Some(-93.62653333333333)
    }

    "extract EXIF headers and error messages from photo contains non fatal error" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/MemoryError2.jpg"))).info
      image._2.isEmpty must_== false
      image._2.head.contains("Nonfatal Error") must_== true
    }

    "return error message from non-existent photo" in {
      val image = new JHead(new java.io.File("/fakepic.jpg")).info
      image._2.mkString(",").contains("Error : No such file") must_== true
    }

  }
}
