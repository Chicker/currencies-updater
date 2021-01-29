package ru.chicker.db

import doobie._

trait QueryUtils {

  def columns(columns: Seq[String], prefix: String = ""): Fragment = {
    if (prefix.nonEmpty) Fragment.const(columns.map(s => prefix + "." + s).mkString(","))
    else Fragment.const(columns.mkString(","))
  }
}
