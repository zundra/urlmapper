package dbio

/**
  * Created by zundra on 4/14/16.
  */

import models.UrlMapping
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import scala.concurrent.duration._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.{ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection
import utils.TokenGenerator
import play.modules.reactivemongo.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

// DBIO Persistence
class UrlMappingRepo @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends ReactiveMongoComponents {
  import UrlMapping._

  val db = reactiveMongoApi.db
  def collection: JSONCollection = db.collection[JSONCollection]("urlmappings")

  // On app startup, find the largest current index.  This is used to seed the counter below
  val idx = Await.result(findLastIndex(), atMost = 10.second) match {
    case Some(result: UrlMapping) => result.map_idx
    case None => 0
  }

  // Thread safe counter to increment mapping idx
  val counter: AtomicLong = new AtomicLong(idx)

  // List all records...primarily for testing
  def all: Future[List[UrlMapping]] = {
    collection.
      find(Json.obj()).
      sort(Json.obj("map_idx" -> -1))
      .cursor[UrlMapping]()
      .collect[List]()
  }

  // Look up URL record by token
  def findByToken(token: String): Future[Option[UrlMapping]] = {
    collection.
      find(Json.obj("token" -> token)).
      sort(Json.obj("map_idx" -> -1))
      .one[UrlMapping]
  }

  // Create a new URL mapping record
  def create(url: String): String = {
    val idx = counter.incrementAndGet()
    val newToken = TokenGenerator.encode(idx)
    val record = UrlMapping(None, url, newToken, idx)
    val json = Json.toJson(record)

    collection.insert(json.as[JsObject])

    newToken
  }

  // Find the largest largest current index.  This method is called on app startup
  def findLastIndex(): Future[Option[UrlMapping]] = {
    collection.
      find(Json.obj()).
      sort(Json.obj("map_idx" -> -1))
      .one[UrlMapping]
  }
}