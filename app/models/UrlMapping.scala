package models

/**
  * Created by zundra on 4/14/16.
  */

import play.api.libs.json.Json

// Main URL persistence class
case class UrlMapping(id: Option[Long], url: String, token: String, map_idx: Long)

// UrlMapping JSON Protocol
object UrlMapping {
  implicit val json = Json.format[UrlMapping]
}

