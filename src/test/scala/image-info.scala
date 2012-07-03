package com.pongr.jhead

import org.specs2.mutable._

import java.io._
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class ImageInfoSpec extends Specification  {

  def getDateTime(s: String) = {
    val formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss")
    formatter.parseDateTime(s)
  }


  "EmailSpec test" should {

    "extract EXIF headers from canon-ixus" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/canon-ixus.jpg")))

      image.info.fileInfo.fileSize must_== Some(128037)
      image.info.gpsInfo must_== GpsInfo(None, None, None)
      image.info.generalInfo must_== GeneralInfo(Some("Canon"),
                                                 Some("Canon DIGITAL IXUS"),
                                                 Some(getDateTime("2001:06:09 15:17:32")),
                                                 Some(640), Some(480),
                                                 Some(1),
                                                 None, None,
                                                 Some(0))
      image.info.otherInfo must_== OtherInfo(Some("346/32"), // focal length
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
      image.info.resolutionInfo must_== ResolutionInfo(Some("180/1"), Some("180/1"), Some("2"))
      image.info.thumbInfo must_== ThumbnailInfo(Some(1524), Some(5342))
    }

    "extract EXIF headers from fujifilm-dx10" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/fujifilm-dx10.jpg")))

      image.info.fileInfo.fileSize must_== Some(133074)
      image.info.gpsInfo must_== GpsInfo(None, None, None)
      image.info.generalInfo must_== GeneralInfo(Some("FUJIFILM"),
                                                 Some("DX-10"),
                                                 Some(getDateTime("2001:04:12 20:33:14")),
                                                 Some(1024), Some(768),
                                                 Some(1),
                                                 None, None,
                                                 Some(1))
      image.info.otherInfo must_== OtherInfo(Some("58/10"),
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
      image.info.resolutionInfo must_== ResolutionInfo(Some("72/1"), Some("72/1"), Some("2"))
      image.info.thumbInfo must_== ThumbnailInfo(Some(856),Some(10274))
    }

    "extract EXIF headers from fujifilm-finepix40i" in {
      val image = JHead(IOUtils.toByteArray(getClass.getResourceAsStream("/fujifilm-finepix40i.jpg")))
      image.info.fileInfo.fileSize must_== Some(43183)
      image.info.gpsInfo must_== GpsInfo(None, None, None)
      image.info.generalInfo must_== GeneralInfo(Some("FUJIFILM"),
                                                 Some("FinePix40i"),
                                                 Some(getDateTime("2000:08:04 18:22:57")),
                                                 Some(600),
                                                 Some(450),
                                                 Some(1),
                                                 None,
                                                 None,
                                                 Some(1))
      image.info.otherInfo must_== OtherInfo(Some("870/100"),
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
      image.info.resolutionInfo must_== ResolutionInfo(Some("72/1"),Some("72/1"),Some("2"))
      image.info.thumbInfo must_== ThumbnailInfo(Some(1074),Some(8691))
    }

  }
}
