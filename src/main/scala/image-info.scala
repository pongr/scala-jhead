/*
 * Copyright (c) 2012 Pongr, Inc.
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pongr.jhead

import org.joda.time.DateTime

trait Constructor[T] {
  def parser(info: Seq[String]) = new Parser(info)
  def create: Parser => T
  def apply(info: Seq[String]): T = create (parser(info))
}

object ImageInfo {
  def apply(lines: Seq[String]): ImageInfo =
    ImageInfo(FileInfo(lines),
              GpsInfo(lines),
              GeneralInfo(lines),
              OtherInfo(lines),
              ResolutionInfo(lines),
              ThumbnailInfo(lines))
}

/**
 * A class contains minimal information of exif headers.
 */
case class ImageInfo(
  fileInfo: FileInfo,

  gpsInfo: GpsInfo,

  generalInfo: GeneralInfo,
  
  otherInfo: OtherInfo,

  resolutionInfo: ResolutionInfo,

  thumbInfo: ThumbnailInfo
)

object FileInfo extends Constructor[FileInfo] {
  def create = c => FileInfo(c.getString("File name"), c.getDateTime("File date"), c.getInt("File size"))
}
case class FileInfo(
  fileName: Option[String],
  fileDateTime: Option[DateTime],
  fileSize: Option[Int]
)


object GpsInfo extends Constructor[GpsInfo] {
  def create = c => GpsInfo(c.getString("GPS Latitude"), c.getString("GPS Longitude"), c.getString("GPS Altitude"))
}
case class GpsInfo(gpsLat: Option[String], gpsLong: Option[String], gpsAlt: Option[String])



// XResolution = 72/1
// YResolution = 72/1
// ResolutionUnit = 2
object ResolutionInfo extends Constructor[ResolutionInfo] {
  def create = c => ResolutionInfo(c.getString("XResolution", "="), 
                                   c.getString("YResolution", "="), 
                                   c.getString("ResolutionUnit", "="))
}
case class ResolutionInfo(x: Option[String], y: Option[String], rUnit: Option[String])


// ThumbnailOffset = 694
// ThumbnailLength = 52075
object ThumbnailInfo extends Constructor[ThumbnailInfo] {
  def create = c => ThumbnailInfo(c.getInt("ThumbnailOffset", separator = "="),
                                  c.getInt("ThumbnailLength", separator = "="))
}
case class ThumbnailInfo(offset: Option[Int], length: Option[Int])
// largestExifOffset: Option[Int],
// atEnd: Option[String],
// sizeOffset: Option[Int]


object GeneralInfo extends Constructor[GeneralInfo] {
  def create = c => GeneralInfo(c.getString("Camera make"),
                                c.getString("Camera model"),
                                c.getDateTime("Date/Time"),
                                c.getInt("Resolution", 0),
                                c.getInt("Resolution", 1),
                                c.getInt("Orientation", separator = "="),
                                c.getString("Color/bw"),
                                c.getString("Jpeg process"),
                                c.getInt("Flash", separator = "="))
}

case class GeneralInfo(
  cameraMake: Option[String],
  cameraModel: Option[String],
  dateTime: Option[DateTime],
  width: Option[Int],
  height: Option[Int],
  orientation: Option[Int],
  isColor: Option[String],
  jpegProcess: Option[String],
  flash: Option[Int]
)


object OtherInfo extends Constructor[OtherInfo] {
  def create = c => OtherInfo(c.getString("FocalLength", separator="="),
                              c.getString("ExposureTime", separator="="),
                              c.getString("ApertureValue", separator="="),
                              c.getString("SubjectDistance", separator="="),
                              c.getString("CCD width"),
                              c.getString("ExposureBiasValue", separator="="),
                              c.getString("Digital Zoom"),
                              c.getString("FocalLengthIn35mmFilm", separator="="),
                              c.getString("Whitebalance"),
                              c.getString("MeteringMode", separator="="),
                              c.getString("ExposureProgram", separator="="),
                              c.getString("Exposure Mode"),
                              c.getString("ISO equiv."),
                              c.getString("Light Source"),
                              c.getString("Focus range"),
                              c.getString("UserComment", separator = "="))
}

case class OtherInfo(
  focalLength: Option[String],
  exposureTime: Option[String],
  apertureValue: Option[String],
  subjectDistance: Option[String],
  ccdWidth: Option[String],
  exposureBias: Option[String],
  digitalZoomRatio: Option[String],
  focalLength35mmEquiv: Option[String], // Exif 2.2 tag - usually not present.
  whitebalance: Option[String],
  meteringMode: Option[String],
  exposureProgram: Option[String],
  exposureMode: Option[String],
  iSOequivalent: Option[String],
  lightSource: Option[String],
  distanceRange: Option[String],
  comments: Option[String]
  /*commentWidthchars: Option[String],    // If nonzero], widechar comment], indicates number of chars.*/
  /*dateTimeOffsets: Option[Seq[String]],*/
  /*numDateTimeTags: Option[String]*/
)
