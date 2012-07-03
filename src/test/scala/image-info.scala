package com.pongr.jhead

import org.specs2.mutable._

import java.io._
import org.apache.commons.io.IOUtils
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat



class ImageInfoSpec extends Specification  {

  val canonIxusBytes = IOUtils.toByteArray(getClass.getResourceAsStream("/canon-ixus.jpg"))
  
  def getDateTime(s: String) = {
    val formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss")
    formatter.parseDateTime(s)
  }


  "EmailSpec test" should {

    "extract header exif headers from canon-ixus" in {

      val image = JHead(canonIxusBytes) 

      // image.info.fileInfo.fileDateTime must_== Some(getDateTime("2001:06:14 07:21:39"))
      image.info.fileInfo.fileSize must_== Some(128037)
                                      
      image.info.gpsInfo must_== GpsInfo(None, None, None)
      
      image.info.generalInfo must_== GeneralInfo(Some("Canon"),
                                                 Some("Canon DIGITAL IXUS"),
                                                 Some(getDateTime("2001:06:09 15:17:32")),
                                                 Some(640),
                                                 Some(480),
                                                 Some(1),
                                                 None,
                                                 None,
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

  }
}
