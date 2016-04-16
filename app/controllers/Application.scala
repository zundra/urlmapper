package controllers

/**
  * Created by zundra on 4/14/16.
  */

import javax.inject.Inject
import dbio.UrlMappingRepo
import models.UrlField
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await

class Application @Inject()(val messagesApi: MessagesApi, urlMappingRepo: UrlMappingRepo) extends Controller with I18nSupport {

  // GET /
  def main = Action.async { implicit rs =>
    urlMappingRepo.all
      .map{urlmappings =>
        Ok(views.html.mainPage(Application.createUrlMappingForm))
      }
  }

  // 302 Redirect to original URL
  def navigateToUrl(token: String) = Action.async { implicit rs =>
    urlMappingRepo.findByToken(token).map(rec => Redirect(rec.get.url))
  }

  // POST /
  def create = Action { implicit request =>
    val formValidationResult = Application.createUrlMappingForm.bindFromRequest
    formValidationResult.fold({ formWithErrors =>
      println(formWithErrors)
      BadRequest(views.html.mainPage(formWithErrors))
    }, { urlfield =>
      Redirect(routes.Application.show(urlMappingRepo.create(urlfield.url)))
    })
  }

  // GET /urlmappings
  def list = Action.async { implicit rs =>
    urlMappingRepo.all
      .map{urlmappings =>
        Ok(views.html.list(urlmappings))
      }
  }

  // #GET /urlmappings/:token
  def show(token: String) = Action.async { implicit rs =>
    urlMappingRepo.findByToken(token).map{ urlmapping =>
        urlmapping match {
          case Some(record) =>  Ok(views.html.show(record))
          case None => NotFound
        }

      }
  }
}

// Companion object to create the form and validate input
object Application extends Validations {
  val createUrlMappingForm = Form(
    mapping("url" -> text)
    (UrlField.apply)(UrlField.unapply) verifying("Invalid URL", fields => fields match {
      case urlField => isValidUrl(urlField.url)
    })
  )
}