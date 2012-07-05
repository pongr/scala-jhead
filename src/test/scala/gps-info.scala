package com.pongr.jhead

import org.specs2.mutable._

class GpsInfoSpec extends Specification  {

  "GpsInfoSpec" should {

    "convert gps latitude and longitude from string" in {

      var gpsInfo = GpsInfo(Some("N 43d 38m 19.39s"), Some("W 116d 14m  28.86s"), None)
      gpsInfo.latitude must_== Some(43.63871944444445)
      gpsInfo.longitude must_== Some(-116.24135)


      gpsInfo = GpsInfo(Some("N 41d 35.24m  0s"), Some("W 93d 37.55m  0s"), None)
      gpsInfo.latitude must_== Some(41.58733333333333)
      gpsInfo.longitude must_== Some(-93.62583333333333)


      gpsInfo = GpsInfo(Some("N 42d 21m  4s"), Some("W 71d 11m 59s"), None)
      gpsInfo.latitude must_== Some(42.35111111111111)
      gpsInfo.longitude must_== Some(-71.19972222222222)

      println(gpsInfo.latitude, gpsInfo.longitude)
      1 must_== 1
    }

  }
}
