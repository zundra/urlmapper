package models

/**
  * Created by zundra on 4/14/16.
  */

class UrlField(urlStr: String) {
  val url = normalize(urlStr)

  // Normalize the URL to add a protocol if the protocol is missing
  def normalize(urlStr: String): String = {
    if (!urlStr.startsWith("http://") && !urlStr.startsWith("https://")) {
      s"http://$urlStr"
    } else {
      urlStr
    }
  }
}

// Companion object for deconstruction
object UrlField {
  def apply(url: String) = new UrlField(url)

  def unapply(record: UrlField) = {
    record match {
      case (record: UrlField) => Some(record.url)
      case _ => None
    }
  }
}