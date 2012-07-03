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

      val info = JHead(jhead.getBytes).info
      info.gpsInfo must_== GpsInfo(None,None,None)
      info.generalInfo must_== GeneralInfo(None,None,None,Some(640),Some(480),None,None,None,None)
      info.otherInfo must_== OtherInfo(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None)
      info.resolutionInfo must_== ResolutionInfo(None,None,None)
      info.thumbInfo must_== ThumbnailInfo(None,None)

    }

  }
}
