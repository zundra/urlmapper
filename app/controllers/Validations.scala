package controllers

/**
  * Created by zundra on 4/14/16.
  */

import org.apache.commons.validator.routines.UrlValidator

trait Validations {
  def isValidUrl(url: String): Boolean = {
    new UrlValidator(Array("http", "https")).isValid(url)
  }
}

