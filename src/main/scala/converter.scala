package com.pongr.jhead

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Converter(info: Seq[String]) {
  def getString(name: String, separator: String = ": "): Option[String] = {
    val r = ("^" + name + """\s*""" + separator).r
    info.filter (e => !r.findFirstIn(e.trim).isEmpty).headOption match {
      case Some(value) => Some(value.split(separator)(1).trim)
      case _ => None
    }
  }

  def getInt(name: String, ind: Int = 0, separator: String= ": "): Option[Int] = {
    getString(name, separator) match {
      case Some(value) =>
        val ints = "[0-9]+".r.findAllIn(value).toList
        try { Some(ints(ind).toInt) } catch { case e => None }
      case _ => None
    }
  }

  def getDateTime(name: String): Option[DateTime] = {
    getString(name) match {
      case Some(d) =>
        val formatter = DateTimeFormat.forPattern("yyyy:MM:dd HH:mm:ss")
        Some(formatter.parseDateTime(d))
      case _ => None
    }
  }


  def getBoolean(name: String): Option[Boolean] = {
    getString(name) match {
      case Some(v) if (v.toLowerCase == "yes" || v.toLowerCase == "true") => Some(true)
      case Some(v) if (v.toLowerCase == "no" || v.toLowerCase == "false") => Some(false)
      case _ => None
    }
  }

}
